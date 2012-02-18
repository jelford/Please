package elford.james.please.codegen.tinytypes;

public class CClassName implements ClassName {

	private String wrappedString;

	public CClassName(String className) {
		this.wrappedString = className;
	}
	
	@Override
	public String toString() {
		return wrappedString;
	}
	
	public static ClassName from(String string) {
		return new CClassName(string);
	}

}
