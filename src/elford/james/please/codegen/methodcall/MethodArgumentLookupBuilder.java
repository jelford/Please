package elford.james.please.codegen.methodcall;

public class MethodArgumentLookupBuilder {
	
	
	private int[] argList;

	public MethodArgumentLookupBuilder(int howMany) {
		this.argList = new int[howMany];
		for (int i=0; i<howMany; i++) {
			this.argList[i] = i;
		}
	}

	public MethodArgument[] methodArguments() {
		int[] arguments = this.argList;
		int numberOfArgs = arguments.length;
		MethodArgument[] args = new MethodArgument[numberOfArgs];
		
		int i = 0;
		for (final int arg : arguments) {
			args[i++] = new MethodArgument() {

				@Override
				public String asArgument(MethodInvocationBuilder builder) {
					return toString();
				}
				
				@Override
				public String toString() {
					return ("$"+Integer.toString(arg+1));
				}
			};
		}
		
		return args;
	}
}
