package src.elford.james.please;

import java.io.PrintStream;
import java.util.List;

import src.elford.james.please.codegen.tinytypes.CClassName;
import src.elford.james.please.codegen.tinytypes.ClassName;

import com.impetus.annovention.listener.ClassAnnotationDiscoveryListener;

class ExposeAnnotationDiscoveryListener implements
		ClassAnnotationDiscoveryListener {

	private final PrintStream out;
	private final List<ClassName> classList;

	public ExposeAnnotationDiscoveryListener(PrintStream out, List<ClassName> classNamesToExpose) {
		this.out = out;
		this.classList = classNamesToExpose;
	}

	@Override
	public String[] supportedAnnotations() {
		return new String[] { Expose.class.getName() };
	}

	@Override
	public void discovered(String className, String annotation) {
		assert Expose.class.getName().equals(annotation);
		out.println("Generating interface to expose private methods of "
				+ className);
		classList.add(new CClassName(className));
	}
}