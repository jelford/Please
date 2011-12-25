package test;

import generated.Please;
import generated.Interface.src.ClassToMockPrivate;

import java.io.OutputStream;
import java.io.PrintStream;

import junit.framework.TestCase;

import org.junit.Test;

import src.ASecondClass;
import src.ClassToMock;

public class GenTest extends TestCase {

	@Test
	public void testBasic() {
		Please please = new Please();
		ClassToMock ctm = new ClassToMock();
		ClassToMockPrivate priv = please.Call(ctm);
		assertTrue(priv.doStuff(true));
	}
	
	@Test
	public void testWithTwoArguments() {
		Please please = new Please();
		
		ClassToMock ctm = new ClassToMock();
		assertEquals(Integer.valueOf(5), please.Call(ctm).someOtherStuff(1, 4));
	}
	
	@Test
	public void testCanHandleVoidReturn() {
		Please please = new Please();
		ASecondClass asc = new ASecondClass();
		please.Call(asc).someFunction(asc);
	}
}
