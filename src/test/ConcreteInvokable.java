package test;

public class ConcreteInvokable implements Invokable {
	private boolean called = false;

	@Override
	public void invoke() {
		this.called = true;
	}
	
	public boolean hasBeenCalled() {
		return this.called;
	}
	
}
