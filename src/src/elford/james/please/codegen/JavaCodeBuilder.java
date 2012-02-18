package src.elford.james.please.codegen;

import src.elford.james.please.codegen.scoped.JavaScopedBlock;

public interface JavaCodeBuilder {
	JavaCodeBlock append(JavaCodeBlock j);
	JavaCodeBlock append(JavaScopedBlock j);
	
}
