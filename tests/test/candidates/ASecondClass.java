package test.candidates;

import elford.james.please.Expose;
import tests.Invokable;

@Expose
@SuppressWarnings("unused")
public class ASecondClass {
	
	private Invokable invokable;

	public ASecondClass(Invokable i) {
		this.invokable = i;
	}
	
	private void someFunction() {
		this.invokable.invoke();
	}
}
