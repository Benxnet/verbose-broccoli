package de.lab4inf.axela.facts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Objects;
import java.util.Random;

import org.junit.jupiter.api.Test;


class FactBaseTest {
	Random rd = new Random();

	@Test
	void testNullFact() {
		try {
			@SuppressWarnings("unused")
			FactBase<Integer,Integer> facts = new FactBase<>(123,null);
			fail("no exception with NP facts thrown");
		} catch (NullPointerException error) {
			String msg = error.getMessage();
			assertNotNull(msg, "empty exception is meaningless");
			assertTrue(error.getMessage().contains("fact is a Nullpointer!"));
		}
	}
	
	@Test
	void testNullFact2() {
		try {
			@SuppressWarnings("unused")
			FactBase<Integer,Integer> facts = new FactBase<>(null,123);
			fail("no exception with NP facts thrown");
		} catch (NullPointerException error) {
			String msg = error.getMessage();
			assertNotNull(msg, "empty exception is meaningless");
			assertTrue(error.getMessage().contains("fact is a Nullpointer!"));
		}
	}
	
	@Test
	void testNullFacts() {
		try {
			@SuppressWarnings("unused")
			FactBase<Integer,Integer> facts = new FactBase<>(null,null);
			fail("no exception with NP facts thrown");
		} catch (NullPointerException error) {
			String msg = error.getMessage();
			assertNotNull(msg, "empty exception is meaningless");
			assertTrue(error.getMessage().contains("fact is a Nullpointer!"));
		}
	}

	@Test
	void testGetFact() {
		Integer one = rd.nextInt(), two = rd.nextInt();
		FactBase<Integer,Integer> facts = new FactBase<>(one,two, true);
		FactBase<Integer,Integer> facts2 = new FactBase<>(one,two, false);
		FactBase<Integer,Integer> facts3 = new FactBase<>(one,two);
		assertEquals(one, facts.getFact1());
		assertEquals(two, facts.getFact2());
		assertEquals(true, facts.getSpeedUp());
		assertEquals(false, facts2.getSpeedUp());
		assertEquals(false, facts3.getSpeedUp());
	}
	
	@Test
	void testSignature() {
		Integer one = rd.nextInt(), two = rd.nextInt();
		int expected = Objects.hash(one.getClass(), two.getClass());
		FactBase<Integer,Integer> facts = new FactBase<>(one,two);
		int returned = facts.getSignature();
		assertEquals(expected, returned);
	}
	
	@Test
	void testSignatureSame() {
		Integer one = rd.nextInt(), two = rd.nextInt();
		FactBase<Integer,Integer> facts1 = new FactBase<>(one,two);
		FactBase<Integer,Integer> facts2 = new FactBase<>(one,two);
		assertEquals(facts2.getSignature(), facts1.getSignature());
	}
	
	@Test
	void testHash() {
		Integer one = rd.nextInt(), two = rd.nextInt();
		int expected = Objects.hash(one, two);
		FactBase<Integer,Integer> facts = new FactBase<>(one,two);
		int returned = facts.hashCode();
		assertEquals(expected, returned);
	}
	
	@Test
	void testHashSame() {
		Integer one = rd.nextInt(), two = rd.nextInt();
		FactBase<Integer,Integer> facts1 = new FactBase<>(one,two);
		FactBase<Integer,Integer> facts2 = new FactBase<>(one,two);
		assertEquals(facts2.hashCode(), facts1.hashCode());
	}
	
	@Test
	void testEquals() {
		Integer one = rd.nextInt(), two = rd.nextInt();
		FactBase<Integer,Integer> facts1 = new FactBase<>(one,two);
		FactBase<Integer,Integer> facts2 = new FactBase<>(one,two);
		assertEquals(facts1.equals(facts2), true);
	}
	
	@Test
	void testEqualsFalse() {
		FactBase<Integer,Integer> facts1 = new FactBase<>(123,456);
		FactBase<Integer,Integer> facts2 = new FactBase<>(123,789);
		assertEquals(facts1.equals(facts2), false);
	}
	
	@Test
	void testEqualsSame() {
		FactBase<Integer,Integer> facts1 = new FactBase<>(123,456);
		assertEquals(facts1.equals(facts1), true);
	}
	
	@Test
	void testEqualsNull() {
		FactBase<Integer,Integer> facts1 = new FactBase<>(123,456);
		assertEquals(facts1.equals(null), false);
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@Test
	void testEqualsWrongClass() {
		FactBase<Integer,Integer> facts1 = new FactBase<>(123,456);
		assertEquals(facts1.equals("test"), false);
	}


}
