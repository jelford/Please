package test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.junit.Test;

import src.ClassToMock;

public class InvokePrivate extends TestCase {
	@Test
	public void testCanInvokePrivateMethods() {
		ClassToMock ctm = new ClassToMock();
		try {
			Method m = ctm.getClass().getDeclaredMethod("doStuff", new Class[] {Boolean.class});
			m.setAccessible(true);
			assertTrue((Boolean) m.invoke(ctm, new Object[] {Boolean.valueOf(true)}));
			
			
			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
