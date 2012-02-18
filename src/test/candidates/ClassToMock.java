package test.candidates;

import src.elford.james.please.Expose;

@Expose
@SuppressWarnings("unused")
public class ClassToMock {
	private String pField;
	public String field;
	
	private Boolean doStuff(Boolean b) {
		return true;
	}
	
	private Integer someOtherStuff(Integer a, Integer b) {
		return Integer.valueOf(a.intValue() + b.intValue());
	}
	
	private Integer add(Integer a, Integer b) {
		return Integer.valueOf(a.intValue() + b.intValue());
	}
}
