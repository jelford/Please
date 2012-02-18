package src.elford.james.please.codegen.tinytypes;

public class CClassName implements ClassName {

	private String wrappedString;

	public CClassName(String className) {
		this.wrappedString = className;
	}
	
	@Override
	public String asString() {
		return wrappedString;
	}

}
