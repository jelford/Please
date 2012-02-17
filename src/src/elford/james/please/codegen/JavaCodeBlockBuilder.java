package src.elford.james.please.codegen;

public class JavaCodeBlockBuilder {

	final StringBuilder code;
	
	public JavaCodeBlockBuilder() {
		this.code = new StringBuilder();
	}
	
	public void method(JavaCodeBlock body) {
		code.append("{");
		code.append(body);
		code.append("}");
		
	}
	
	public void method(JavaCodeBlock ... body) {
		this.method(new RawJavaCodeBlock(body));
	}

}
