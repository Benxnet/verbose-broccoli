package de.lab4inf.axela.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.lab4inf.axela.core.Axela;
import de.lab4inf.axela.core.Iris;
import de.lab4inf.axela.facts.FactBase;
import de.lab4inf.axela.engine.AxelaEngine.Key;

public class AxelaEngineTest {
	String FACTS_ARE_A_NULL_POINTER = "facts are a NullPointer!";
	String PROBLEM_IS_A_NULL_POINTER = "problem is a NullPointer!";
	String IRIS_IS_A_NULL_POINTER = "iris is a NullPointer!";
	String SOLUTION_IS_A_NULL_POINTER = "solution is a NullPointer!";
	String NO_IRIS_FOR_PROBLEM_FOUND = "no Iris for problem: %s found";
	String NO_AXELA_FOUND = "no valid Axela implementation found";

	double[] doubleArray = { 0., 1. };

	protected static Axela axela;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	public void setUp() throws Exception {
		if (null == axela) {
			axela = Axela.getEngine();
			axela.registerSolver("SUM", new double[0], new TestIris());
		}
		assertNotNull(axela, "no Axela found");
	}

	/**
	 * Test method for
	 * {@link de.lab4inf.axela.engine.AxelaEngine#solve(java.lang.String, java.lang.String[])}.
	 */
	@Test
	void testRegisterSameIris() {
		try {
			axela.registerSolver("SUM", new double[0], new TestIris());
			fail("no exception with iris already registered thrown");
		} catch (IllegalStateException error) {
			String msg = error.getMessage();
			assertNotNull(msg, "empty exception is meaningless");
			assertTrue(error.getMessage().contains("iris already registered"), error.getMessage());
		}
	}

	@Test
	void testRegisterNullProblem() {
		try {
			axela.registerSolver(null, new double[0], new TestIris());
			fail("no exception with iris already registered thrown");
		} catch (NullPointerException error) {
			String msg = error.getMessage();
			assertNotNull(msg, "empty exception is meaningless");
			assertTrue(error.getMessage().contains(PROBLEM_IS_A_NULL_POINTER), error.getMessage());
		}
	}

	@Test
	void testRegisterNullFact() {
		try {
			axela.registerSolver("SUM", null, new TestIris());
			fail("no exception with iris already registered thrown");
		} catch (NullPointerException error) {
			String msg = error.getMessage();
			assertNotNull(msg, "empty exception is meaningless");
			assertTrue(error.getMessage().contains(FACTS_ARE_A_NULL_POINTER), error.getMessage());
		}
	}

	@Test
	void testRegisterNullIris() {
		try {
			axela.registerSolver("SUM", new double[0], null);
			fail("no exception with iris already registered thrown");
		} catch (NullPointerException error) {
			String msg = error.getMessage();
			assertNotNull(msg, "empty exception is meaningless");
			assertTrue(error.getMessage().contains(IRIS_IS_A_NULL_POINTER), error.getMessage());
		}
	}

	@Test
	void testSolveNullProblem() {
		try {
			axela.solve(null, doubleArray);
			fail("no exception with iris already registered thrown");
		} catch (NullPointerException error) {
			String msg = error.getMessage();
			assertNotNull(msg, "empty exception is meaningless");
			assertTrue(error.getMessage().contains(PROBLEM_IS_A_NULL_POINTER), error.getMessage());
		}
	}

	@Test
	void testSolveNullFact() {
		try {
			axela.solve("SUM", null);
			fail("no exception with iris already registered thrown");
		} catch (NullPointerException error) {
			String msg = error.getMessage();
			assertNotNull(msg, "empty exception is meaningless");
			assertTrue(error.getMessage().contains(FACTS_ARE_A_NULL_POINTER), error.getMessage());
		}
	}

	@Test
	void testSolveNullSolution() {
		try {
			axela.solve("SUM", doubleArray);
			fail("no exception with iris already registered thrown");
		} catch (NullPointerException error) {
			String msg = error.getMessage();
			assertNotNull(msg, "empty exception is meaningless");
			assertTrue(error.getMessage().contains(SOLUTION_IS_A_NULL_POINTER), error.getMessage());
		}
	}

	@Test
	void testSolveNoIris() {
		try {
			axela.solve("ASDASD", doubleArray);
			fail("no exception with iris already registered thrown");
		} catch (IllegalArgumentException error) {
			String msg = error.getMessage();
			assertNotNull(msg, "empty exception is meaningless");
			assertTrue(error.getMessage().contains("no Iris for problem"), error.getMessage());
		}
	}

	@Test
	void testFindNoIris() {
		try {
			axela.findSolverFor("ASDASD", doubleArray);
			fail("no exception with iris already registered thrown");
		} catch (IllegalStateException error) {
			String msg = error.getMessage();
			assertNotNull(msg, "empty exception is meaningless");
			assertTrue(error.getMessage().contains("no Iris for problem"), error.getMessage());
		}
	}

	@Test
	void testHasAndFindIris() {
		axela.registerSolver("PLUS", new double[0], new TestIris());
		axela.registerSolver("MINUS", new double[0], new TestIris());
		axela.registerSolver("MULT", new double[0], new TestIris());

		axela.registerSolver("SUM", new int[0], new TestIris2());
		axela.registerSolver("PLUS", new int[0], new TestIris2());
		axela.registerSolver("MINUS", new int[0], new TestIris2());
		axela.registerSolver("MULT", new int[0], new TestIris2());
		// HasSolverFor
		// Test Iris 1 with double
		double[] fac = { 1., 2. };
		assertEquals(axela.hasSolverFor("MINUS", fac), true);
		assertEquals(axela.hasSolverFor("PLUS", fac), true);
		assertEquals(axela.hasSolverFor("MULT", fac), true);
		assertEquals(axela.hasSolverFor("SUM", fac), true);
		// Test Iris 2 with int
		int[] fac2 = { 1, 2 };
		assertEquals(axela.hasSolverFor("MINUS", fac2), true);
		assertEquals(axela.hasSolverFor("PLUS", fac2), true);
		assertEquals(axela.hasSolverFor("MULT", fac2), true);
		assertEquals(axela.hasSolverFor("SUM", fac2), true);

		// FindSolverFor
		// double
		assertEquals(axela.findSolverFor("MINUS", fac).getClass(), new TestIris().getClass());
		assertEquals(axela.findSolverFor("PLUS", fac).getClass(), new TestIris().getClass());
		assertEquals(axela.findSolverFor("MULT", fac).getClass(), new TestIris().getClass());
		assertEquals(axela.findSolverFor("SUM", fac).getClass(), new TestIris().getClass());
		// int
		assertEquals(axela.findSolverFor("MINUS", fac2).getClass(), new TestIris2().getClass());
		assertEquals(axela.findSolverFor("PLUS", fac2).getClass(), new TestIris2().getClass());
		assertEquals(axela.findSolverFor("MULT", fac2).getClass(), new TestIris2().getClass());
		assertEquals(axela.findSolverFor("SUM", fac2).getClass(), new TestIris2().getClass());
	}

	@Test
	void testKeyEqualsSame() {
		FactBase<Integer, Integer> facts = new FactBase<>(1, 2);
		AxelaEngine keyEngine = new AxelaEngine();
		Key key = keyEngine.new Key("PROB", facts);
		assertEquals(key.equals(key), true);
	}

	@Test
	void testKeyEqualsNull() {
		AxelaEngine keyEngine = new AxelaEngine();
		Key key = keyEngine.new Key("PROB", 123);
		assertEquals(key.equals(null), false);
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	void testKeyEqualsWrongClass() {
		AxelaEngine keyEngine = new AxelaEngine();
		Key key = keyEngine.new Key("PROB", 123);
		assertEquals(key.equals("test"), false);
	}

	class TestIris implements Iris<String, double[], Double> {

		@Override
		public Double solve(String problem, double[] facts) {
			return null;
		}

	}

	class TestIris2 implements Iris<String, int[], Double> {

		@Override
		public Double solve(String problem, int[] facts) {
			return null;
		}

	}

}