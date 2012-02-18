package src.elford.james.please.codegen.statements;

import src.elford.james.please.codegen.JavaCodeBlock;
import src.elford.james.please.codegen.RawJavaCodeBlock;

public class MethodInvocationCodeBlock extends RawJavaCodeBlock implements JavaCodeBlock {

	public MethodInvocationCodeBlock(StringBuilder methodInvocation) {
		this.from(methodInvocation);
	}

}
