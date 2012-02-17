package src.elford.james.please.codegen;

public interface JavaCodeBlock {
	
	@Override
	public String toString();

	public JavaCodeBlock append(JavaCodeBlock j);

	public void addTo(JavaCodeBlock jcb);

	JavaCodeBlock append(JavaScopedBlock j);
}
