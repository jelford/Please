package elford.james.please.codegen.scoped;

import elford.james.please.codegen.JavaCodeBlock;

public interface CatchBlock extends JavaScopedBlock {

	JavaScopedBlock _finally(JavaCodeBlock doNothing);

}
