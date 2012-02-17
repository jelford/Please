package src.elford.james.please.codegen;

public class MethodInvocationCodeBlockBuilder {

	private StringBuilder code;

	public MethodInvocationCodeBlockBuilder(Identifier identifier,
			String methodName) {
		this.code = new StringBuilder(identifier.toString()).append(".").append(methodName).append("(");
	}
	
	public MethodInvocationCodeBlock with(JavaCodeBlock ... args) {
		int i = 0;
		for (JavaCodeBlock arg : args) {
			this.code.append(arg);
			if (++i < args.length)
				this.code.append(", ");
		}
		return new MethodInvocationCodeBlock(this.code.append(")"));
	}

}
