package src.elford.james.please.codegen.arrays;

import src.elford.james.please.codegen.JavaCodeBlock;
import src.elford.james.please.codegen.methodcall.MethodInvocationBuilder;

public class JavaArrayBuilder implements JavaArrayTypeBuilder, JavaArrayContentsBuilder, JavaArray {

	StringBuilder code;
	private String type;
	
	@Override
	public JavaArray containing(JavaCodeBlock ... objects) {
		this.code = new StringBuilder("new ").append(type).append("[] {");
		
		int i=0;
		for (JavaCodeBlock jcb : objects) {
			this.code.append(jcb);
			if (++i < objects.length)
				this.code.append(", ");
		}
		
		this.code.append(" }");
		
		return this;
	}

	@Override
	public JavaArrayContentsBuilder ofType(String type) {
		this.type = type;
		return this;
	}

	@Override
	public String asArgument(MethodInvocationBuilder builder) {
		return this.code.toString();
	}


}
