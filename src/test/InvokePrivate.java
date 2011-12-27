package test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import test.candidates.PrimitiveReturnTypes;

public class InvokePrivate extends TestCase {
	@Test
	public void testCanInvokePrivateMethods() {
		PrimitiveReturnTypes ctm = new PrimitiveReturnTypes();
		byte ret = 1;
		try {
			Method m = ctm.getClass()
					.getDeclaredMethod("buyte", new Class[] {});
			m.setAccessible(true);
			assertThat((byte) (Byte) m.invoke(ctm, new Object[] {}),
					is(equalTo(ret)));

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
