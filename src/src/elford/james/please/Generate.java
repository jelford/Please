package src.elford.james.please;

import java.io.PrintStream;

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
import com.impetus.annovention.Discoverer;
import com.impetus.annovention.listener.ClassAnnotationDiscoveryListener;

public class Generate {
	public static void main(String[] args) throws Exception {
		ClassPool cp = ClassPool.getDefault();
		CtClass please = cp.makeClass("gen.elford.james.please.Please");
		CtClass pleaseInterface = cp
				.makeInterface("gen.elford.james.please.PleaseInterface");

		Discoverer disco = new ClasspathDiscoverer();
		Generate gen = new Generate(cp, please, pleaseInterface, System.out);
		disco.addAnnotationListener(new ExposeAnnotationDiscoveryListener(
				System.out, gen));
		disco.discover();

		please.addInterface(pleaseInterface);
		please.writeFile("gen");
		pleaseInterface.writeFile("gen");
	}

	static class ExposeAnnotationDiscoveryListener implements
			ClassAnnotationDiscoveryListener {

		private final PrintStream out;
		private final Generate gen;

		public ExposeAnnotationDiscoveryListener(PrintStream out, Generate gen) {
			this.out = out;
			this.gen = gen;
		}

		@Override
		public String[] supportedAnnotations() {
			return new String[] { Expose.class.getName() };
		}

		@Override
		public void discovered(String clazz, String annotation) {
			assert Expose.class.getName().equals(annotation);
			out.println("Generating interface to expose private methods of "
					+ clazz);
			gen.generateIntrospection(clazz);
		}
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

	private void generateIntrospection(String clazz) {
		try {

			CtClass ctm = cp.get(clazz);
			CtClass privateAccess = cp
					.makeInterface("gen.elford.james.please.Interface." + clazz
							+ "Private");
			CtClass privateAccessImpl = cp
					.makeClass("gen.elford.james.please.Implementation."
							+ clazz + "PrivateImpl");

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

			privateAccess.writeFile("gen");
			privateAccessImpl.writeFile("gen");
		} catch (Exception e) {
			throw new Error(e);
		}
	}

	private final String startFunctionBody = "{";
	private final String startTryBlock = "try {";

	private final String endTryBlock = "} catch (SecurityException e) {"
			+ "e.printStackTrace();" + "} catch (NoSuchMethodException e) {"
			+ "e.printStackTrace();" + "} catch (IllegalArgumentException e) {"
			+ "e.printStackTrace();" + "} catch (IllegalAccessException e) {"
			+ "e.printStackTrace();"
			+ "} catch (java.lang.reflect.InvocationTargetException e) {"
			+ "e.printStackTrace();" + "}";

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
