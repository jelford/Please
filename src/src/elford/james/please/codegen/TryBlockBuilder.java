package src.elford.james.please.codegen;

public class TryBlockBuilder {
	JavaCodeBlock codeBlock;
	
	public TryBlockBuilder(JavaCodeBlock jcb) {
		this.codeBlock = new RawJavaCodeBlock().from("try {").append(jcb).appendRaw("; }");
	}

	public CatchBlockBuilder _catch(String exceptionType, Identifier label) {
		return new CatchBlockBuilder(this, exceptionType, label);
	}

}
