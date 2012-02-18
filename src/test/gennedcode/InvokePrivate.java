package test.gennedcode;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.junit.Test;

import test.candidates.PrimitiveReturnTypes;

public class InvokePrivate extends TestCase {
	@Test
	public void testCanInvokePrivateMethods() throws Exception {
		PrimitiveReturnTypes ctm = new PrimitiveReturnTypes();
		byte ret = 1;
		
		Method m = ctm.getClass()
				.getDeclaredMethod("buyte", new Class[] {});
		m.setAccessible(true);
		assertThat((byte) (Byte) m.invoke(ctm, new Object[] {}),
				is(equalTo(ret)));

		

	}
}
