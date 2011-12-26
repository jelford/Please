package src.elford.james.please;

import java.io.IOException;
import java.io.PrintStream;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import javassist.bytecode.AccessFlag;
import javassist.bytecode.MethodInfo;

import com.impetus.annovention.ClasspathDiscoverer;
import com.impetus.annovention.Discoverer;
import com.impetus.annovention.listener.ClassAnnotationDiscoveryListener;

public class Generate {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ClassPool cp = ClassPool.getDefault();
		CtClass please = cp.makeClass("gen.elford.james.please.Please");

		Discoverer disco = new ClasspathDiscoverer();
		disco.addAnnotationListener(new ExposeAnnotationDiscoveryListener(cp, please, System.out));
		disco.discover();
		
		please.writeFile("gen");

	}

	static class ExposeAnnotationDiscoveryListener implements
			ClassAnnotationDiscoveryListener {

		private final ClassPool cp;
		private final CtClass please;
		private final PrintStream out;
		
		private final String startFunctionBody = "{";
		private final String startTryBlock = "try {";
		
		
		private final String endTryBlock = 
			"} catch (SecurityException e) {" +
				"e.printStackTrace();" +
			"} catch (NoSuchMethodException e) {" +
				"e.printStackTrace();" +
			"} catch (IllegalArgumentException e) {" +
				"e.printStackTrace();" +
			"} catch (IllegalAccessException e) {" +
				"e.printStackTrace();" +
			"} catch (java.lang.reflect.InvocationTargetException e) {" +
				"e.printStackTrace();" +
			"}";
		
		private final String endFunctionBody = "}";
		private final String setMethodAccessible = "m.setAccessible(true);";
		private final String methodName = "m";
		private final String wrappedMethodField = "wrapped";
		
		
		public ExposeAnnotationDiscoveryListener(ClassPool cp, CtClass please, PrintStream out) {
			this.cp = cp;
			this.please = please;
			this.out = out;
		}
		
		@Override
		public String[] supportedAnnotations() {
			return new String[] { Expose.class.getName() };
		}

		@Override
		public void discovered(String clazz, String annotation) {
			assert Expose.class.getName().equals(annotation);
			out.println(clazz);

			try {

				CtClass ctm = cp.get(clazz);
				CtClass privateAccess = cp
						.makeInterface("gen.elford.james.please.Interface." + clazz + "Private");
				CtClass privateAccessImpl = cp
						.makeClass("gen.elford.james.please.Implementation." + clazz + "PrivateImpl");
				
				CtField wrapped = new CtField(ctm, "wrapped", privateAccessImpl);
				privateAccessImpl.addField(wrapped);
				CtConstructor constructor = CtNewConstructor.make(
						new CtClass[] { ctm }, new CtClass[0], privateAccessImpl);
				constructor.setBody(""+wrapped.getName()+"=$1;");
				
				privateAccessImpl.addConstructor(constructor);
				privateAccessImpl.addInterface(privateAccess);
				
				
				CtMethod[] ctmMethods = ctm.getDeclaredMethods();
				for (CtMethod method : ctmMethods) {
					String name = method.getName();
					boolean hasReturn = !method.getReturnType().equals(CtClass.voidType);
					
					MethodInfo info = method.getMethodInfo();
					int accessFlags = info.getAccessFlags();
					if (AccessFlag.isPrivate(accessFlags)) {
						// Construct the interface
						privateAccess.addMethod(CtNewMethod.abstractMethod(
								method.getReturnType(), name,
								method.getParameterTypes(),
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
							mBody.append(returnStatement(method));
						}
						
						// Invoke the method
						mBody.append(invokeMethod(method));
								
								
						// Close the try block and deal with exceptions
						mBody.append(endTryBlock);
						
						// A return type (for if there is an exception)
						mBody.append(returnGuard(hasReturn));
							
						mBody.append(endFunctionBody);
						
						out.println(mBody);

							
						CtMethod wrappedPrivateMethod = CtNewMethod.make(
								method.getReturnType(), name,
								method.getParameterTypes(),
								method.getExceptionTypes(), mBody.toString(),
								privateAccessImpl);	

						privateAccessImpl.addMethod(wrappedPrivateMethod);
					}
				}

				

				please.addMethod(CtNewMethod.make(privateAccess, "Call",
						new CtClass[] { ctm }, new CtClass[0],
						"return new " + privateAccessImpl.getName() + "($1);", please));

				privateAccess.writeFile("gen");
				privateAccessImpl.writeFile("gen");
			} catch (NotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (CannotCompileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		private String returnGuard(boolean hasReturn) {
			return (hasReturn ? "return null;" : "");
		}

		private String invokeMethod(CtMethod method) throws NotFoundException {
			StringBuilder invocation = new StringBuilder();
			invocation.append(methodName + ".invoke(wrapped, new Object[] {");
			for (int i=0; i < method.getParameterTypes().length; ++i) {
				invocation.append("$"+(Integer.toString(i+1))+", ");
			}
			invocation.replace(invocation.length()-2, invocation.length(), "");
	
			invocation.append("});");
			
			
			return invocation.toString();
		}

		private String returnStatement(CtMethod method) throws NotFoundException {
			//boolean isPrimitive = method.getReturnType().isPrimitive();
			
			return "return ("+method.getReturnType().getName()+") ";
		}

		private String introspectMethodWithSignature(CtMethod method) throws NotFoundException {
			StringBuilder introspectMethod = new StringBuilder();
			introspectMethod.append(
					"java.lang.reflect.Method " + methodName + " = " + 
						"wrapped.getClass().getDeclaredMethod(\""+method.getName()+"\", ");
			
			
			if (method.getParameterTypes().length > 0) {
				introspectMethod.append("new Class[] {");
				for(CtClass paramType : method.getParameterTypes()) {
					introspectMethod.append(paramType.getName() + ".class");
					introspectMethod.append(",");
				}
				introspectMethod.replace(introspectMethod.length()-1, introspectMethod.length(), "");
				introspectMethod.append("}");
			} else {
				introspectMethod.append("null");
			}
			introspectMethod.append(");");
			
			
				
			return introspectMethod.toString();
		}
		

	}
}
