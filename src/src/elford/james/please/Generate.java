package src.elford.james.please;

import java.io.PrintStream;
import java.util.List;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.CtPrimitiveType;
import javassist.NotFoundException;
import javassist.bytecode.AccessFlag;
import javassist.bytecode.MethodInfo;

import com.impetus.annovention.ClasspathDiscoverer;

public class Generate {
	public static void main(String[] args) throws Exception {
		ClassPool cp = ClassPool.getDefault();
		CtClass please = cp.makeClass(Config.pleaseImplementationClass);
		CtClass pleaseInterface = cp
				.makeInterface(Config.pleaseInterfaceClass);

		Generate gen = new Generate(cp, please, pleaseInterface, System.out);
		
		List<ClassName> classNamesToExpose = 
				new TargetClassFinder(new ClasspathDiscoverer())
					.findTargetClassNames();
		
		for(ClassName clazz : classNamesToExpose) {
			gen.generateIntrospection(clazz);
		}

		please.addInterface(pleaseInterface);
		please.writeFile(Config.destination);
		pleaseInterface.writeFile(Config.destination);
	}
	
	private final PrintStream out;
	private final ClassPool cp;
	private final CtClass please;
	private final CtClass pleaseInterface;

	private Generate(ClassPool cp, CtClass please, CtClass pleaseInterface,
			PrintStream out) {
		this.cp = cp;
		this.please = please;
		this.pleaseInterface = pleaseInterface;
		this.out = out;
	}

	private void generateIntrospection(ClassName className) {
		try {
			String clazz = className.asString();
			CtClass ctm = cp.get(clazz);
			CtClass privateAccess = cp
					.makeInterface(Config.interfaceQualifiedNameFor(clazz));
			CtClass privateAccessImpl = cp
					.makeClass(Config.implementationQualifiedNameFor(clazz));

			CtField wrapped = new CtField(ctm, wrappedMethodField,
					privateAccessImpl);
			privateAccessImpl.addField(wrapped);
			CtConstructor constructor = CtNewConstructor.make(
					new CtClass[] { ctm }, new CtClass[0], privateAccessImpl);
			constructor.setBody(wrappedMethodField + " = $1;");

			privateAccessImpl.addConstructor(constructor);
			privateAccessImpl.addInterface(privateAccess);

			CtMethod[] ctmMethods = ctm.getDeclaredMethods();
			for (CtMethod method : ctmMethods) {
				String name = method.getName();

				MethodInfo info = method.getMethodInfo();
				int accessFlags = info.getAccessFlags();
				if (AccessFlag.isPrivate(accessFlags)) {

					// Get return detauls
					boolean hasReturn = !method.getReturnType().equals(
							CtClass.voidType);
					CtClass rType = method.getReturnType();
					if (rType.isPrimitive() && hasReturn)
						rType = cp.get(((CtPrimitiveType) rType)
								.getWrapperName());

					// Construct the interface
					privateAccess.addMethod(CtNewMethod.abstractMethod(rType,
							name, noPrimitives(cp, method.getParameterTypes()),
							method.getExceptionTypes(), privateAccess));

					StringBuffer mBody = new StringBuffer();

					mBody.append(startFunctionBody);
					mBody.append(startTryBlock);

					// Method m = ...
					mBody.append(introspectMethodWithSignature(method));

					// Expose the method
					mBody.append(setMethodAccessible);

					// Return statement, with appropriate casting
					if (hasReturn) {
						mBody.append(returnStatement(rType));
					}

					// Invoke the method
					mBody.append(invokeMethod(method));

					// Close the try block and deal with exceptions
					mBody.append(endTryBlock);

					// A return type (for if there is an exception)
					if (hasReturn)
						mBody.append(returnGuard);

					mBody.append(endFunctionBody);

					out.println("Wrapping method [" + method.getName() + "] : "
							+ mBody);

					CtMethod wrappedPrivateMethod = CtNewMethod.make(rType,
							name, noPrimitives(cp, method.getParameterTypes()),
							method.getExceptionTypes(), mBody.toString(),
							privateAccessImpl);

					privateAccessImpl.addMethod(wrappedPrivateMethod);
				}
			}

			please.addMethod(CtNewMethod.make(privateAccess, "call",
					new CtClass[] { ctm }, new CtClass[0], "return new "
							+ privateAccessImpl.getName() + "($1);", please));
			pleaseInterface.addMethod(CtNewMethod.abstractMethod(privateAccess,
					"call", new CtClass[] { ctm }, new CtClass[0],
					pleaseInterface));

			privateAccess.writeFile(Config.destination);
			privateAccessImpl.writeFile(Config.destination);
		} catch (Exception e) {
			throw new Error(e);
		}
	}

	private final String startFunctionBody = "{";
	private final String startTryBlock = "try {";

	// If an error arrises in the introspection, we might as well just throw it;
	// there's no sensible error handling we could do anyway.
	private final String endTryBlock = "} catch (Exception e) { throw new Error(e); } ";

	private final String endFunctionBody = "}";
	private final String setMethodAccessible = "m.setAccessible(true);";
	private final String methodLocalVar = "m";
	private final String wrappedMethodField = "wrapped";
	private final String returnGuard = "return null;";

	private String invokeMethod(CtMethod method) throws NotFoundException {
		final int numberOfParameters = method.getParameterTypes().length;
		StringBuilder invocation = new StringBuilder();

		invocation.append(methodLocalVar);
		invocation.append(".invoke(");
		invocation.append(wrappedMethodField);
		invocation.append(", ");

		if (numberOfParameters > 0) {
			invocation.append("new Object[] {");

			for (int i = 0; i < numberOfParameters; ++i) {
				invocation.append("$" + (Integer.toString(i + 1)) + ", ");
			}

			if (numberOfParameters > 0)
				invocation.replace(invocation.length() - 2,
						invocation.length(), "");

			invocation.append("}");
		} else {
			invocation.append("null");
		}

		invocation.append(");");

		return invocation.toString();
	}

	private String returnStatement(CtClass returnType) throws NotFoundException {
		StringBuilder rStatement = new StringBuilder();
		rStatement.append("return ");

		rStatement.append("(");
		rStatement.append(returnType.getName());
		rStatement.append(") ");

		return rStatement.toString();
	}

	private String introspectMethodWithSignature(CtMethod method)
			throws NotFoundException {
		StringBuilder introspectMethod = new StringBuilder();
		introspectMethod.append("java.lang.reflect.Method " + methodLocalVar
				+ " = " + wrappedMethodField
				+ ".getClass().getDeclaredMethod(\"" + method.getName()
				+ "\", ");

		if (method.getParameterTypes().length > 0) {
			introspectMethod.append("new Class[] {");
			for (CtClass paramType : method.getParameterTypes()) {
				String typeName;
				typeName = paramType.getName();
				
				introspectMethod.append(typeName + ".class");
				introspectMethod.append(",");
			}
			introspectMethod.replace(introspectMethod.length() - 1,
					introspectMethod.length(), "");
			introspectMethod.append("}");
		} else {
			introspectMethod.append("null");
		}
		introspectMethod.append(");");

		return introspectMethod.toString();
	}
	
	private CtClass[] noPrimitives(ClassPool cp, CtClass[] r) throws NotFoundException {
		CtClass[] ret = new CtClass[r.length];
		for (int i=0; i<r.length; ++i) {
			if (r[i].isPrimitive())
				ret[i] = cp.get(((CtPrimitiveType) r[i]).getWrapperName());
			else
				ret[i] = r[i];
		}
		return ret;
	}
}
