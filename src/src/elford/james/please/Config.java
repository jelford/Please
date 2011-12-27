package src.elford.james.please;



public class Config {

	public static final String pleaseInterfaceClass = "gen.elford.james.please.PleaseInterface";
	public static final String pleaseImplementationClass = "gen.elford.james.please.Please";
	public static final String destination = "gen";
	public static final String interfacePrefix = "gen.elford.james.please.interfaces";
	public static final String implementationPrefix = "gen.elford.james.please.implementations";
	public static String interfaceQualifiedNameFor(String clazz) {
		return interfacePrefix + "." + clazz + "Private";
	}
	public static String implementationQualifiedNameFor(String clazz) {
		return implementationPrefix + "." + clazz + "PrivateWrapper";
	}
	
	
	

}
