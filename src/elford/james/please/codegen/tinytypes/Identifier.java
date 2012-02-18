package elford.james.please.codegen.tinytypes;

import elford.james.please.codegen.methodcall.MethodArgument;
import elford.james.please.codegen.methodcall.MethodInvocationBuilder;
import elford.james.please.codegen.statements.MethodInvocationCodeBlockBuilder;

public class Identifier implements MethodArgument {
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
	
	@Override
	public String asArgument(MethodInvocationBuilder builder) {
		return builder.representArgument(this);
	}
}
