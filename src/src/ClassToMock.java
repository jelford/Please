package src;

import gen.Expose;

@Expose
public class ClassToMock {
	private String pField;
	public String field;
	
	private Boolean doStuff(Boolean b) {
		return true;
	}
	
	private Integer someOtherStuff(Integer a, Integer b) {
		return Integer.valueOf(a.intValue() + b.intValue());
	}
}
