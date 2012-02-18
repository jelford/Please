package src.elford.james.please.codegen;

import javassist.CtClass;
import src.elford.james.please.codegen.arrays.JavaArrayBuilder;
import src.elford.james.please.codegen.methodcall.EmptyMethodArgument;
import src.elford.james.please.codegen.methodcall.MethodArgument;
import src.elford.james.please.codegen.methodcall.MethodArgumentLookupBuilder;
import src.elford.james.please.codegen.scoped.TryBlockBuilder;
import src.elford.james.please.codegen.statements.JavaObjectInstantiation;
import src.elford.james.please.codegen.statements.JavaReturnStatement;
import src.elford.james.please.codegen.statements.LocalAssignBuilder;
import src.elford.james.please.codegen.tinytypes.Identifier;

/**
 * Provides convenience methods for most things you might
 * want to write. Method bodies, try blocks, ...
 * 
 * @author james
 *
 */
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
	
	public static MethodArgumentLookupBuilder first(int howMany) {
		return new MethodArgumentLookupBuilder(howMany);
	}
	
	public static JavaCodeBlock[] valuesFrom(MethodArgument[] args) {
		
		JavaCodeBlock[] jcbArray = new JavaCodeBlock[args.length];
		for (int i = 0; i < args.length; i++) {
			jcbArray[i] = new RawJavaCodeBlock().from(args[i].toString());
		}
		return jcbArray;
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
	
	public static JavaObjectInstantiation _new(String type, MethodArgument ... arguments) {
		return new JavaObjectInstantiation(type, arguments);
	}
	
	public static JavaCodeBlock _throw(JavaObjectInstantiation jcb) {
		return new RawJavaCodeBlock().from("throw " + jcb);
	}
	
	public static JavaCodeBlock doNothing() {
		return new RawJavaCodeBlock();
	}
	
	public static EmptyMethodArgument noArgument() {
		return new EmptyMethodArgument();
	}
	
	public final static String javaReflectedMethodType = 
								"java.lang.reflect.Method";
}
