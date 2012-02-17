package src.elford.james.please.codegen;

public class JavaObjectInstantiation extends RawJavaCodeBlock implements JavaCodeBlock{
	public JavaObjectInstantiation(String type, JavaCodeBlock[] arguments) {
		StringBuilder sb = new StringBuilder("new ").append(type);
		sb.append("(");
		int i=0;
		for (JavaCodeBlock arg : arguments) {
			sb.append(arg);
			if (++i < arguments.length) 
				sb.append(", ");
		}
		sb.append(")");
		this.code = sb;
	}
}
