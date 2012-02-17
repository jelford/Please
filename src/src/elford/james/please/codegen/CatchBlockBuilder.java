package src.elford.james.please.codegen;

public class CatchBlockBuilder implements CatchBlock, JavaScopedBlock {
	
	String type;
	
	StringBuilder code;

	private TryBlockBuilder tryCode;

	private Identifier exceptionLabel;
	
	CatchBlockBuilder(TryBlockBuilder tryBlockBuilder, String exceptionType, Identifier label) {
		this.tryCode = tryBlockBuilder;
		this.type = exceptionType;
		this.exceptionLabel = label;
	}

	public CatchBlock should(JavaCodeBlock jcb) {
		StringBuilder sb = new StringBuilder();
		sb.append(this.tryCode.codeBlock);
		sb.append("catch (");
		sb.append(type).append(" ").append(exceptionLabel).append(") {");
		sb.append(jcb);
		sb.append("; }");
		this.code = sb;
		return this;
	}
	
	public JavaScopedBlock _finally(JavaCodeBlock jcb) {
		this.code.append(" finally { ").append(jcb).append(" }");
		return this;
	}

	@Override
	public JavaScopedBlock append(JavaCodeBlock j) {
		this.code.append(j);
		return this;
	}

	@Override
	public void addTo(JavaCodeBlock jcb) {
		jcb.append(this);
	}

	@Override
	public JavaScopedBlock append(JavaScopedBlock j) {
		this.code.append(j);
		return this;
	}
	
	@Override
	public String toString() {
		return this.code.toString();
	}
}
