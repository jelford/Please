package elford.james.please.codegen.statements;

import elford.james.please.codegen.JavaCodeBlock;
import elford.james.please.codegen.RawJavaCodeBlock;

public class MethodInvocationCodeBlock extends RawJavaCodeBlock implements JavaCodeBlock {

	public MethodInvocationCodeBlock(StringBuilder methodInvocation) {
		this.from(methodInvocation);
	}

}
