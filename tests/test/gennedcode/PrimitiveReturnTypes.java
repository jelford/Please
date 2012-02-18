package test.gennedcode;

import gen.elford.james.please.Please;
import gen.elford.james.please.PleaseInterface;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class PrimitiveReturnTypes {
	private PleaseInterface please;
	private test.candidates.PrimitiveReturnTypes cut;
	
	@Before
	public void setUp() {
		this.please = new Please();
		this.cut = new test.candidates.PrimitiveReturnTypes();
	}
	
	@After
	public void tearDown() {
		this.please = null;
		this.cut = null;
	}
	
	@Test
	public void testCanHandlePrimitiveByte() {
		byte ret = please.call(cut).buyte();
		byte expected = 1;
		assertThat(ret, is(equalTo(expected)));
	}

	@Test
	public void testCanHandlePrimitiveShort() {
		short ret = please.call(cut).suret();
		short expected = 1;
		assertThat(ret, is(equalTo(expected)));
	}
	
	@Test
	public void testCanHandlePrimitiveInt() {
		int ret = please.call(cut).innt();
		int expected = 1;
		assertThat(ret, is(equalTo(expected)));
	}
	
	@Test
	public void testCanHandlePrimitiveLong() {
		long ret = please.call(cut).lorng();
		long expected = 1;
		assertThat(ret, is(equalTo(expected)));
	}
	
	@Test
	public void testCanHandlePrimitiveFloat() {
		float ret = please.call(cut).flote();
		float expected = 1.0f;
		assertThat(ret, is(equalTo(expected)));
	}
	
	@Test
	public void testCanHandlePrimitiveDouble() {
		double ret = please.call(cut).dubul();
		double expected = 1.0;
		assertThat(ret, is(equalTo(expected)));
	}
	
	@Test
	public void testCanHandlePrimitiveBoolean() {
		boolean ret = please.call(cut).booleighan();
		boolean expected = true;
		assertThat(ret, is(equalTo(expected)));
	}
	
	@Test
	public void testCanHandlePrimitiveChar() {
		char ret = please.call(cut).charr();
		char expected = 'a';
		assertThat(ret, is(equalTo(expected)));
	}

}
