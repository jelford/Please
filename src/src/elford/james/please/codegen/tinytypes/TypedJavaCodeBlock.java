package src.elford.james.please.codegen.tinytypes;

import src.elford.james.please.codegen.JavaCodeBlock;
import src.elford.james.please.codegen.JavaCodeBuilder;
import src.elford.james.please.codegen.RawJavaCodeBlock;
import src.elford.james.please.codegen.scoped.JavaScopedBlock;

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
		j.addTo(this.wrappedJcb);
		return this;
	}

	@Override
	public void addTo(JavaCodeBuilder jcb) {
		jcb.append(this);
	}

	@Override
	public TypedJavaCodeBlock append(JavaScopedBlock j) {
		j.addTo(this.wrappedJcb);
		return this;
	}
}
