package test.gennedcode;

import gen.elford.james.please.Please;
import gen.elford.james.please.PleaseInterface;
import gen.elford.james.please.interfaces.test.candidates.ClassToMockPrivate;
import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import test.candidates.ASecondClass;
import test.candidates.ClassToMock;
import tests.ConcreteInvokable;

public class GenTest extends TestCase {
	private PleaseInterface please;
	
	@Before
	public void setUp() {
		this.please = new Please();
	}
	
	@After
	public void tearDown() {
		this.please = null;
	}
	
	@Test
	public void testBasic() {
		ClassToMock ctm = new ClassToMock();
		ClassToMockPrivate priv = please.call(ctm);
		assertTrue(priv.doStuff(true));
	}
	
	@Test
	public void testWithTwoArguments() {
		ClassToMock ctm = new ClassToMock();
		assertEquals(Integer.valueOf(5), please.call(ctm).someOtherStuff(1, 4));
	}
	
	@Test
	public void testadditionOnClassToMock() {
		ClassToMock ctm = new ClassToMock();
		assertThat(please.call(ctm).add(5, 4), is(equalTo(9)));
	}
	
	@Test
	public void testCanHandleVoidReturn() {
		ConcreteInvokable i = new ConcreteInvokable();
		ASecondClass asc = new ASecondClass(i);
		please.call(asc).someFunction();
		assertThat(i.hasBeenCalled(), is(true));
		
	}
}
