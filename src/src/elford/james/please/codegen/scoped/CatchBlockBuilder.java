package src.elford.james.please.codegen.scoped;

import src.elford.james.please.codegen.JavaCodeBlock;
import src.elford.james.please.codegen.tinytypes.ClassName;
import src.elford.james.please.codegen.tinytypes.Identifier;

public class CatchBlockBuilder implements CatchBlock, JavaScopedBlock {
	
	ClassName type;
	
	StringBuilder code;

	private TryBlockBuilder tryCode;

	private Identifier exceptionLabel;
	
	CatchBlockBuilder(TryBlockBuilder tryBlockBuilder, ClassName exceptionType, Identifier label) {
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
	public String toString() {
		return this.code.toString();
	}
}
