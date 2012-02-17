package src.elford.james.please.codegen;

public class Identifier {
	String token;

	public Identifier(String name) {
		this.token = name;
	}

	public MethodInvocationCodeBlockBuilder call(String methodName) {
		return new MethodInvocationCodeBlockBuilder(this, methodName);
	}

	@Override
	public String toString() {
		return this.token;
	}
	
	public JavaCodeBlock asArgument() {
		return new RawJavaCodeBlock().from(this.token);
	}
}
