/*
 * Project: Axela.Client
 *
 * Copyright (c) 2020,  Prof. Dr. Nikolaus Wulff
 * University of Applied Sciences, Muenster, Germany
 * Lab for computer sciences (Lab4Inf).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package de.lab4inf.axela.simple;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.lab4inf.axela.core.Axela;

/**
 * Testing the Concatinator Iris implementation.
 * 
 * @author nwulff
 * @since 14.10.2020
 */
class ConcatinatorTest {
	String returned, expected;
	static Axela engine;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUp() {
		engine = Axela.getEngine();
		Concatinator.init(engine);
	}

	/**
	 * Test method for
	 * {@link de.lab4inf.axela.simple.Concatinator#solve(java.lang.String, java.lang.String[])}.
	 */
	@Test
	void testSolveNP() {
		String[] facts = null;
		try {
			engine.solve("CONCAT", facts);
			fail("no exception with NP facts thrown");
		} catch (NullPointerException error) {
			String msg = error.getMessage();
			assertNotNull(msg, "empty exception is meaningless");
			assertTrue(error.getMessage().contains("facts are a NullPointer!"));
		}
	}

	@Test
	void testSolveZero() {
		String[] facts = {};
		try {
			engine.solve("CONCAT", facts);
			fail("no exception with NP facts thrown");
		} catch (IllegalArgumentException error) {
			String msg = error.getMessage();
			assertNotNull(msg, "empty exception is meaningless");
			assertTrue(error.getMessage().contains("no facts given"), error.getMessage());
		}
	}

	@Test
	void testSolveOne() {
		String[] facts = { "Hello" };
		expected = "Hello";
		returned = engine.solve("CONCAT", facts);
		assertEquals(expected, returned);
	}

	@Test
	void testSolveMany() {
		String[] facts = { "Hello", "World", "from", "Iris" };
		expected = "Hello World from Iris";
		returned = engine.solve("CONCAT", facts);
		assertEquals(expected, returned);
	}

	@Test
	void testProblemNP() {
		String[] facts = { "Hello", "World", "from", "Iris" };
		try {
			engine.solve(null, facts);
			fail("no exception with NP problem thrown");
		} catch (NullPointerException error) {
			String msg = error.getMessage();
			assertNotNull(msg, "empty exception is meaningless");
			assertTrue(error.getMessage().contains("problem is a NullPointer!"));
		}
	}

	@Test
	void testWrongProblem() {
		String[] facts = { "Hello", "World", "from", "Iris" };
		try {
			engine.solve("WhatEverYouWant", facts);
			fail("no exception with wrong problem thrown");
		} catch (IllegalArgumentException error) {
			String msg = error.getMessage();
			assertNotNull(msg, "empty exception is meaningless");
			assertTrue(error.getMessage().contains("no Iris for problem"), error.getMessage());
		}
	}

}
