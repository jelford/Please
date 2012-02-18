package elford.james.please.codegen.statements;

import elford.james.please.codegen.JavaCodeBlock;
import elford.james.please.codegen.RawJavaCodeBlock;

public class LocalAssignment extends RawJavaCodeBlock implements JavaCodeBlock {

	public LocalAssignment(StringBuilder sb) {
		this.from(sb);
	}

}
