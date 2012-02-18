package src.elford.james.please.codegen.statements;

import src.elford.james.please.codegen.tinytypes.Identifier;
import src.elford.james.please.codegen.tinytypes.TypedJavaCodeBlock;

public class LocalAssignBuilder {

	StringBuilder identifier;
	public LocalAssignBuilder(Identifier i) {
		this.identifier = new StringBuilder().append(i);
	}
	
	public LocalAssignment to(TypedJavaCodeBlock value) {
		StringBuilder sb = new StringBuilder(value.getType())
			.append(" ")
			.append(this.identifier)
			.append(" = ")
			.append(value);
		
		return new LocalAssignment(sb);
	}
}
