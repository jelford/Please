package test.candidates;

import src.elford.james.please.Expose;
import tests.Invokable;

@Expose
public class ASecondClass {
	
	private Invokable invokable;

	public ASecondClass(Invokable i) {
		this.invokable = i;
	}

	private void someFunction() {
		this.invokable.invoke();
	}
}
