package gen;

import java.io.IOException;

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
	public static void main(String[] args) {
		System.out.println("Hello world");

		ClassPool cp = ClassPool.getDefault();
		CtClass please = cp.makeClass("generated.Please");

		Discoverer disco = new ClasspathDiscoverer();
		disco.addAnnotationListener(new ExposeAnnotationDiscoveryListener(cp, please));
		disco.discover();
		
		try {
			please.writeFile("gen");
		} catch (CannotCompileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	static class ExposeAnnotationDiscoveryListener implements
			ClassAnnotationDiscoveryListener {

		private ClassPool cp;
		private CtClass please;
		
		public ExposeAnnotationDiscoveryListener(ClassPool cp, CtClass please) {
			this.cp = cp;
			this.please = please;
		}
		
		@Override
		public String[] supportedAnnotations() {
			return new String[] { Expose.class.getName() };
		}

		@Override
		public void discovered(String clazz, String annotation) {
			assert Expose.class.getName().equals(annotation);
			System.out.println(clazz);

			try {

				CtClass ctm = cp.get(clazz);
				CtClass privateAccess = cp
						.makeInterface("generated.Interface." + clazz + "Private");
				CtClass privateAccessImpl = cp
						.makeClass("generated." + clazz + "PrivateImpl");
				
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
					boolean hasReturn = !method.getReturnType().getName().equals("void");
					boolean isPrimitive = method.getReturnType().isPrimitive();
					
					MethodInfo info = method.getMethodInfo();
					int accessFlags = info.getAccessFlags();
					if (AccessFlag.isPrivate(accessFlags)) {
						// Construct the interface
						privateAccess.addMethod(CtNewMethod.abstractMethod(
								method.getReturnType(), name,
								method.getParameterTypes(),
								method.getExceptionTypes(), privateAccess));
						
						
						
						StringBuffer mBody = new StringBuffer();
						mBody.append(
						"{" +
							"try {"+
								// Get a hold on the method
								"java.lang.reflect.Method m = wrapped.getClass().getDeclaredMethod(\""+method.getName()+"\", ");
										if (method.getParameterTypes().length > 0) {
											mBody.append("new Class[] {");
											for(CtClass paramType : method.getParameterTypes()) {
												mBody.append(paramType.getName() + ".class");
												mBody.append(",");
											}
											mBody.replace(mBody.length()-1, mBody.length(), "");
											mBody.append("}");
										} else {
											mBody.append("null");
										}
								mBody.append(
										");");
								
								// Expose the method
								mBody.append("m.setAccessible(true);"); 
								
								// Cast for return
								if (hasReturn) {
									mBody.append("return ("+method.getReturnType().getName()+") ");
								}
								
								// Invoke the method
								mBody.append("m.invoke(wrapped, new Object[] {");
								for (int i=0; i < method.getParameterTypes().length; ++i) {
									mBody.append("$"+(Integer.toString(i+1))+", ");
								}
								mBody.replace(mBody.length()-2, mBody.length(), "");
			
								mBody.append("});");
								
								
							// Exception handling
							mBody.append(	
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
							"}" + 
							(hasReturn ? "return null;" : "") +
							
						"}");
						
						System.out.println(mBody);
							
							
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

	}
}
