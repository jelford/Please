package src.elford.james.please.codegen.statements;

import src.elford.james.please.codegen.JavaCodeBlock;
import src.elford.james.please.codegen.RawJavaCodeBlock;

public class LocalAssignment extends RawJavaCodeBlock implements JavaCodeBlock {

	public LocalAssignment(StringBuilder sb) {
		this.from(sb);
	}

}
