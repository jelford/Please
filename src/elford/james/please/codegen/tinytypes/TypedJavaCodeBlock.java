package elford.james.please.codegen.tinytypes;

import elford.james.please.codegen.JavaCodeBlock;
import elford.james.please.codegen.RawJavaCodeBlock;

public class TypedJavaCodeBlock implements JavaCodeBlock {
	private String type;
	private JavaCodeBlock wrappedJcb;
	
	public TypedJavaCodeBlock(String type, StringBuilder code) {
		this.wrappedJcb = new RawJavaCodeBlock().from(code);
		this.type = type;
	}

	public String getType() {
		return this.type;
	}
	
	@Override
	public String toString() {
		return this.wrappedJcb.toString();
	}

	@Override
	public TypedJavaCodeBlock append(JavaCodeBlock j) {
		this.wrappedJcb.append(j);
		return this;
	}

}
