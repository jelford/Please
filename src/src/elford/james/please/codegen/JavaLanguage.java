package src.elford.james.please.codegen;

import java.util.ArrayList;
import java.util.List;

import javassist.CtClass;

import src.elford.james.please.Range;

public class JavaLanguage {
	
	public static JavaCodeBlock methodBody(JavaCodeBlock ... expressions) {
		return new RawJavaCodeBlock(expressions).prependRaw("{").appendRaw("; }");
	}
	
	public static TryBlockBuilder _try(JavaCodeBlock jcb) {
		return new TryBlockBuilder(jcb);
	}
	
	public static TryBlockBuilder _try(JavaCodeBlock ... sequence) {
		JavaCodeBlock jcb = new RawJavaCodeBlock();
		for(JavaCodeBlock j : sequence) {
			if (jcb != null)
				jcb.append(j);
		}
		return _try(jcb);
	}
	
	public static JavaArrayBuilder array() {
		return new JavaArrayBuilder();
	}
	
	public static JavaCodeBlock[] methodArguments(Range numberOfArguments) {
		int numberOfArgs = numberOfArguments.size();
		JavaCodeBlock[] args = new JavaCodeBlock[numberOfArgs];
		
		List<JavaCodeBlock> argList = new ArrayList<JavaCodeBlock>();
		for (int i=0; i < numberOfArgs; i++) {
			argList.add(new RawJavaCodeBlock().from("$"+Integer.toString(i)));
		}
		args = argList.toArray(args);
		
		return args;
	}
	
	public static JavaReturnStatement _return(CtClass type, JavaCodeBlock expression) {
		return new JavaReturnStatement(type, expression);
	}
	
	public static JavaReturnStatement _return(Object _) {
		return new JavaReturnStatement(null);
	}
	
	public static LocalAssignBuilder set(Identifier i) {
		return new LocalAssignBuilder(i);
	}
	
	public static JavaObjectInstantiation _new(String type, JavaCodeBlock ... arguments) {
		return new JavaObjectInstantiation(type, arguments);
	}
	
	public static JavaCodeBlock _throw(JavaObjectInstantiation jcb) {
		return new RawJavaCodeBlock().from("throw " + jcb);
	}
	
	public static JavaCodeBlock doNothing() {
		return new RawJavaCodeBlock();
	}
	
	public final static String javaReflectedMethodType = 
								"java.lang.reflect.Method";
}
