package src.elford.james.please;

import java.util.ArrayList;
import java.util.List;


import com.impetus.annovention.ClasspathDiscoverer;
import com.impetus.annovention.Discoverer;

class TargetClassFinder implements
		ITargetClassFinder {

	private ClasspathDiscoverer annotationFinder;

	public TargetClassFinder(ClasspathDiscoverer classpathDiscoverer) {
		this.annotationFinder = classpathDiscoverer;
	}

	@Override
	public List<ClassName> findTargetClassNames() {
		Discoverer disco = this.annotationFinder;
		
		List<ClassName> classNamesToExpose = new ArrayList<ClassName>();
		disco.addAnnotationListener(new ExposeAnnotationDiscoveryListener(
				System.out, classNamesToExpose));
		disco.discover();
		
		return classNamesToExpose;
	}
	
}