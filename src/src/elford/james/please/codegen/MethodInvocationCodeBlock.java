package src.elford.james.please.codegen;

public class MethodInvocationCodeBlock extends RawJavaCodeBlock implements JavaCodeBlock {

	public MethodInvocationCodeBlock(StringBuilder methodInvocation) {
		this.from(methodInvocation);
	}

}
