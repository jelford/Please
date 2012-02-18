package src.elford.james.please.codegen.methodcall;

import src.elford.james.please.codegen.tinytypes.Identifier;

public interface MethodInvocationBuilder {
	String representArgument(EmptyMethodArgument ema);
	String representArgument(Identifier identifier);
}
