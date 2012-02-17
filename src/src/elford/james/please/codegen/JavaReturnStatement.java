package src.elford.james.please.codegen;

import javassist.CtClass;

public class JavaReturnStatement extends RawJavaCodeBlock implements
		JavaCodeBlock {

	public JavaReturnStatement(CtClass type, JavaCodeBlock expression) {
		StringBuilder rStatement = new StringBuilder();
		rStatement.append("return ");

		rStatement.append("(");
		rStatement.append(type.getName());
		rStatement.append(") ");
		this.from(rStatement);
		this.append(expression);
	}
	
	public JavaReturnStatement(Object _) {
		this.from("return null");
	}

}
