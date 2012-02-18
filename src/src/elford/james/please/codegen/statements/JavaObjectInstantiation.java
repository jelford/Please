package src.elford.james.please.codegen.statements;

import src.elford.james.please.codegen.RawJavaCodeBlock;
import src.elford.james.please.codegen.methodcall.EmptyMethodArgument;
import src.elford.james.please.codegen.methodcall.MethodArgument;
import src.elford.james.please.codegen.methodcall.MethodInvocationBuilder;
import src.elford.james.please.codegen.tinytypes.Identifier;

public class JavaObjectInstantiation extends RawJavaCodeBlock implements MethodInvocationBuilder {
	public JavaObjectInstantiation(String type, MethodArgument[] arguments) {
		StringBuilder sb = new StringBuilder("new ").append(type);
		sb.append("(");
		int i=0;
		for (MethodArgument arg : arguments) {
			sb.append(arg.asArgument(this));
			if (++i < arguments.length) 
				sb.append(", ");
		}
		sb.append(")");
		this.from(sb);
	}

	@Override
	public String representArgument(Identifier identifier) {
		return identifier.toString();
	}

	@Override
	public String representArgument(EmptyMethodArgument _) {
		return null;
	}
}
