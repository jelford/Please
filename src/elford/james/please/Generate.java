package elford.james.please;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import javassist.CannotCompileException;
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

import elford.james.codegen.JavaLanguage;
import elford.james.codegen.TerminatingJavaCodeBlock;
import elford.james.codegen.UnterminatedJavacodeBlock;
import elford.james.codegen.tinytypes.CClassName;
import elford.james.codegen.tinytypes.ClassName;
import elford.james.codegen.tinytypes.Identifier;
import elford.james.codegen.tinytypes.MethodInvokation;
import static elford.james.codegen.JavaLanguage.*;

public class Generate {
	public static void main(String[] args) throws Exception {
		ClassPool cp = ClassPool.getDefault();
		CtClass please = cp.makeClass(Config.pleaseImplementationClass);
		CtClass pleaseInterface = cp
				.makeInterface(Config.pleaseInterfaceClass);

		JavaLanguage language = new JavaLanguage();
		Generate gen = new Generate(cp, language, please, pleaseInterface, System.out);
		
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

	private Generate(ClassPool cp, JavaLanguage language, CtClass please, CtClass pleaseInterface,
			PrintStream out) {
		this.cp = cp;
		this.please = please;
		this.pleaseInterface = pleaseInterface;
		this.out = out;
	}

	private void generateIntrospection(ClassName className) throws NotFoundException, CannotCompileException, IOException {
		String clazz = className.toString();
		CtClass ctm = cp.get(clazz);
		CtClass privateAccess = cp
				.makeInterface(Config.interfaceQualifiedNameFor(clazz));
		CtClass privateAccessImpl = cp
				.makeClass(Config.implementationQualifiedNameFor(clazz));

		CtField wrapped = new CtField(ctm, wrappedMethodField.toString(),
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

				// Get return defaults
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

				TerminatingJavaCodeBlock mBody = methodBody(
						declare(methodLocalVar).as(CClassName.from(javaReflectedMethodType)),
						_try(
								set(methodLocalVar).to(introspectMethodWithSignature(method)),
								setMethodAccessible(),
								hasReturn ? _return(cast(invokeMethod(method)).as(CClassName.from(rType.getName()))) : invokeMethod(method)
						)._catch(CClassName.from("Exception"), exceptionName).then(reThrowAsError(exceptionName))
						._finally(doNothing()),
						_return(literal(null))
				);

				out.println("Wrapping method [" + method.getName() + "] : "
						+ mBody);

				try {
					CtMethod wrappedPrivateMethod = CtNewMethod.make(rType,
							name, noPrimitives(cp, method.getParameterTypes()),
							method.getExceptionTypes(), mBody.representTerminating(),
							privateAccessImpl);
					
					out.println("Signature of wrapping method: " + wrappedPrivateMethod.getSignature());

					privateAccessImpl.addMethod(wrappedPrivateMethod);
					
				} catch (javassist.CannotCompileException e) {
					out.println("Wrapping failed! Method body was:");
					out.println(mBody.representTerminating());
					throw e;
				}
				
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
	}

	// If an error arrises in the introspection, we might as well just throw it;
	// there's no sensible error handling we could do anyway.
	private TerminatingJavaCodeBlock reThrowAsError(Identifier e) { return _throw(CClassName.from("Error"), e); }

	private TerminatingJavaCodeBlock setMethodAccessible() { 
		return methodLocalVar.call("setAccessible").withArguments(literal(true));
	}
	private final Identifier methodLocalVar = new Identifier("m");
	private final Identifier wrappedMethodField = new Identifier("wrapped");
	private final Identifier exceptionName = new Identifier("e");

	private TerminatingJavaCodeBlock invokeMethod(CtMethod method) throws NotFoundException {
		final int numberOfParameters = method.getParameterTypes().length;
		MethodInvokation invocation = methodLocalVar.call("invoke").withArguments(
									wrappedMethodField,
									numberOfParameters > 0 ? 
											array().ofType(CClassName.from("Object"))
											.containing(methodArguments(from(1).to(numberOfParameters+1)))
									: literal(null)
									);
		

		return invocation;
	}
	

	private UnterminatedJavacodeBlock introspectMethodWithSignature(CtMethod method)
			throws NotFoundException {
		StringBuilder introspectMethod = new StringBuilder();
		introspectMethod.append(wrappedMethodField
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
		introspectMethod.append(")");
		
		final String returnedString = introspectMethod.toString();
		return new UnterminatedJavacodeBlock() {
			
			@Override
			public String representUnterminating() {
				return returnedString;
			}
		};
	}
	
	/**
	 * 
	 * @param cp
	 * @param r
	 * @return The given CtClass[] with all Primitive types substituted for
	 * reference types.
	 * @throws NotFoundException
	 */
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
