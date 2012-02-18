package src.elford.james.please.codegen.scoped;

import src.elford.james.please.codegen.JavaCodeBlock;
import src.elford.james.please.codegen.RawJavaCodeBlock;
import src.elford.james.please.codegen.tinytypes.ClassName;
import src.elford.james.please.codegen.tinytypes.Identifier;

public class TryBlockBuilder {
	JavaCodeBlock codeBlock;
	
	public TryBlockBuilder(JavaCodeBlock jcb) {
		this.codeBlock = new RawJavaCodeBlock().from("try {").append(jcb).appendRaw("; }");
	}

	public CatchBlockBuilder _catch(ClassName exceptionType, Identifier label) {
		return new CatchBlockBuilder(this, exceptionType, label);
	}

}
