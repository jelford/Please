package test.gennedcode;

import gen.elford.james.please.Please;
import gen.elford.james.please.PleaseInterface;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class PrimitiveArguments {
	private PleaseInterface please;
	private test.candidates.PrimitiveArguments cut;
	
	@Before
	public void setUp() {
		this.please = new Please();
		cut = new test.candidates.PrimitiveArguments();
	}
	
	@After
	public void tearDown() {
		this.please = null;
		cut = null;
	}
	
	@Test
	public void testCanHandlePrimitiveByte() {
		byte expected = 1;
		byte ret = please.call(cut).buyte(expected);
		assertThat(ret, is(equalTo(expected)));
	}
	
	@Test
	public void testCanHandlePrimitiveShort() {
		short expected = 1;
		short ret = please.call(cut).suret(expected);
		assertThat(ret, is(equalTo(expected)));
	}
	
	@Test
	public void testCanHandlePrimitiveInt() {
		int expected = 1;
		int ret = please.call(cut).innt(expected);
		assertThat(ret, is(equalTo(expected)));
	}
	
	@Test
	public void testCanHandlePrimitiveLong() {
		long expected = 1;
		long ret = please.call(cut).lorng(expected);
		assertThat(ret, is(equalTo(expected)));
	}
	
	@Test
	public void testCanHandlePrimitiveFloat() {
		float expected = 1.0f;
		float ret = please.call(cut).flote(expected);
		assertThat(ret, is(equalTo(expected)));
	}
	
	@Test
	public void testCanHandlePrimitiveDouble() {
		double expected = 1.0;
		double ret = please.call(cut).dubul(expected);
		assertThat(ret, is(equalTo(expected)));
	}
	
	@Test
	public void testCanHandlePrimitiveBoolean() {
		boolean expected = true;
		boolean ret = please.call(cut).booleighan(expected);
		assertThat(ret, is(equalTo(expected)));
	}
	
	@Test
	public void testCanHandlePrimitiveChar() {
		char expected = 'b';
		char ret = please.call(cut).charr(expected);
		assertThat(ret, is(equalTo(expected)));
	}
}
