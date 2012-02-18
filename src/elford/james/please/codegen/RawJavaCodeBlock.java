package elford.james.please.codegen;


/**
 * Concrete implementation of a JavaCodeBlock. Provides
 * convenience methods for manipulating the underlying raw
 * code.
 * 
 * @author james
 *
 */
public class RawJavaCodeBlock implements JavaCodeBlock, JavaCodeBuilder {
	StringBuilder code;

	@Override
	public RawJavaCodeBlock append(JavaCodeBlock j) {
		this.code.append(j);
		return this;
	}

	public RawJavaCodeBlock() {
		this.code = new StringBuilder();
	}

	public RawJavaCodeBlock(JavaCodeBlock... expressions) {
		this.code = new StringBuilder();
		int i = 0;
		for (JavaCodeBlock jcb : expressions) {
			this.append(jcb);
			if (++i < expressions.length)
				this.code.append(";");
		}
	}

	@Override
	public String toString() {
		return this.code.toString();
	}

	public RawJavaCodeBlock from(String raw) {
		this.code = new StringBuilder(raw);
		return this;
	}

	public RawJavaCodeBlock from(StringBuilder sb) {
		this.code = sb;
		return this;
	}

	public RawJavaCodeBlock appendRaw(String string) {
		this.code.append(string);
		return this;
	}

	public RawJavaCodeBlock prependRaw(String string) {
		this.code = new StringBuilder(string).append(this.code);
		return this;
	}



}
