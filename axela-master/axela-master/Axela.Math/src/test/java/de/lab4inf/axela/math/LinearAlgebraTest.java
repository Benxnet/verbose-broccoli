package de.lab4inf.axela.math;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Random;
import java.lang.Math;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.lab4inf.axela.core.Axela;
import de.lab4inf.axela.facts.FactBase;
import de.lab4inf.axela.math.LinearAlgebra.MatVecCalc;

class LinearAlgebraTest {
	final int size = 10;
	static Axela engine;
	final double tolerance = 5.E-12;
	Random rd = new Random();
	double[] vec1 = createRndVec(10 * size);
	double[] vec2 = createRndVec(10 * size);
	double[][] mat1 = createRndMat(10 * size, 10 * size);
	double[][] mat2 = createRndMat(10 * size, 10 * size);

	double[][] mat3 = createRndMat(5 * size, 10 * size);
	double[][] mat4 = createRndMat(10 * size, 7 * size);

	double[][] mat5 = createRndMat(10 * size, 7 * size);
	double[] vec3 = createRndVec(7 * size);

	double[] vec10 = { 1., 2. };
	double[] vec11 = { 4., 5. };
	double[][] mat10 = { { 1., 2. }, { 3., 4. } };
	double[][] mat11 = { { 5., 6. }, { 7., 8. } };

	double[][] matN = new double[0][0];
	double[][] matN2 = new double[0][1];
	double[] vecN = new double[0];

	@BeforeAll
	static void setUp() {
		engine = Axela.getEngine();
		engine.register(new LinearAlgebraPlugin());
	}

	@Test
	void testSolvePlusVecVec() {
		double[] expected = add(vec1, vec2);
		double[] returned = engine.solve("PLUS", new FactBase<double[], double[]>(vec1, vec2));
		assertVectorEquals(expected, returned, tolerance);
	}

	@Test
	void testSolvePlusVecVec2() {
		double[] expected = { 5., 7. };
		double[] returned = engine.solve("PLUS", new FactBase<double[], double[]>(vec10, vec11));
		assertVectorEquals(expected, returned, tolerance);
	}

	@Test
	void testSolveMultVecVec() {
		double expected = mult(vec1, vec2);
		double returned = engine.solve("MULT", new FactBase<double[], double[]>(vec1, vec2));
		assertEqualsDelta(expected, returned, tolerance);
	}

	@Test
	void testSolveMultVecVec2() {
		double expected = 14.;
		double returned = engine.solve("MULT", new FactBase<double[], double[]>(vec10, vec11));
		assertEqualsDelta(expected, returned, tolerance);
	}

	@Test
	void testSolvePlusMatMat() {
		double[][] expected = add(mat1, mat2);
		double[][] returned = engine.solve("PLUS", new FactBase<double[][], double[][]>(mat1, mat2));
		assertMatrixEquals(expected, returned, tolerance);
	}

	@Test
	void testSolvePlusMatMat2() {
		double[][] expected = { { 6., 8. }, { 10., 12. } };
		double[][] returned = engine.solve("PLUS", new FactBase<double[][], double[][]>(mat10, mat11));
		assertMatrixEquals(expected, returned, tolerance);
	}

	@Test
	void testSolveMultMatMat() {
		double[][] expected = mult(mat1, mat2);
		double[][] returned = engine.solve("MULT", new FactBase<double[][], double[][]>(mat1, mat2));
		assertMatrixEquals(expected, returned, tolerance);
	}

	@Test
	void testSolveMultMatMatSmall() {
		double[][] small1 = { { 0.12345, 0.11255 }, { 1.1233, 0.0001 } };
		double[][] small2 = { { 1.1582, 0.12547 }, { 0.000005, 0.12575 } };
		double[][] expected = { { 0.14298035275, 0.029642434 }, { 1.3010060605, 0.140953026 } };
		double[][] returned = engine.solve("MULT", new FactBase<double[][], double[][]>(small1, small2));
		assertMatrixEquals(expected, returned, tolerance);
	}

	@Test
	void testSolveMultMatMat3() {
		double[][] expected = { { 19., 22. }, { 43., 50. } };
		double[][] returned = engine.solve("MULT", new FactBase<double[][], double[][]>(mat10, mat11));
		assertMatrixEquals(expected, returned, tolerance);
	}

	@Test
	void testSolveMultMatVec() {
		double[] expected = mult(mat1, vec1);
		double[] returned = engine.solve("MULT", new FactBase<double[][], double[]>(mat1, vec1));
		assertVectorEquals(expected, returned, tolerance);
	}

	@Test
	void testSolveMultMatVec2() {
		double[] expected = mult(mat5, vec3);
		double[] returned = engine.solve("MULT", new FactBase<double[][], double[]>(mat5, vec3));
		assertVectorEquals(expected, returned, tolerance);
	}

	@Test
	void testSolveMultMatVec3() {
		double[] expected = { 5., 11. };
		double[] returned = engine.solve("MULT", new FactBase<double[][], double[]>(mat10, vec10));
		assertVectorEquals(expected, returned, tolerance);
	}

	@Test
	void testSolveMultMatMat2() {
		double[][] expected = mult(mat3, mat4);
		double[][] returned = engine.solve("MULT", new FactBase<double[][], double[][]>(mat3, mat4));
		assertMatrixEquals(expected, returned, tolerance);
	}

	@Test
	void testAddVecVecNull() {
		try {
			engine.solve("PLUS", new FactBase<double[], double[]>(null, vec3));
			fail("no exception with NP facts thrown");
		} catch (NullPointerException error) {
			String msg = error.getMessage();
			assertNotNull(msg, "empty exception is meaningless");
			assertTrue(error.getMessage().contains("fact is a Nullpointer!"));
		}
	}

	@Test
	void testAddVecVecNull2() {
		try {
			engine.solve("PLUS", new FactBase<double[], double[]>(vec1, null));
			fail("no exception with NP facts thrown");
		} catch (NullPointerException error) {
			String msg = error.getMessage();
			assertNotNull(msg, "empty exception is meaningless");
			assertTrue(error.getMessage().contains("fact is a Nullpointer!"));
		}
	}

	@Test
	void testAddVecVecNull3() {
		try {
			engine.solve("PLUS", new FactBase<double[], double[]>(vecN, vec3));
			fail("no exception with NP facts thrown");
		} catch (NullPointerException error) {
			String msg = error.getMessage();
			assertNotNull(msg, "empty exception is meaningless");
			assertTrue(error.getMessage().contains("length is zero"));
		}
	}

	@Test
	void testMultVecVecNull() {
		try {
			engine.solve("MULT", new FactBase<double[], double[]>(vec1, vecN));
			fail("no exception with NP facts thrown");
		} catch (NullPointerException error) {
			String msg = error.getMessage();
			assertNotNull(msg, "empty exception is meaningless");
			assertTrue(error.getMessage().contains("length is zero"));
		}
	}

	@Test
	void testAddMatMatNull() {
		try {
			engine.solve("PLUS", new FactBase<double[][], double[][]>(matN, mat4));
			fail("no exception with NP facts thrown");
		} catch (NullPointerException error) {
			String msg = error.getMessage();
			assertNotNull(msg, "empty exception is meaningless");
			assertTrue(error.getMessage().contains("length is zero"));
		}
	}

	@Test
	void testMultMatMatNull() {
		try {
			engine.solve("MULT", new FactBase<double[][], double[][]>(mat1, matN2));
			fail("no exception with NP facts thrown");
		} catch (NullPointerException error) {
			String msg = error.getMessage();
			assertNotNull(msg, "empty exception is meaningless");
			assertTrue(error.getMessage().contains("length is zero"));
		}
	}

	@Test
	void testMultMatVecNull() {
		try {
			engine.solve("MULT", new FactBase<double[][], double[]>(matN, vec1));
			fail("no exception with NP facts thrown");
		} catch (NullPointerException error) {
			String msg = error.getMessage();
			assertNotNull(msg, "empty exception is meaningless");
			assertTrue(error.getMessage().contains("length is zero"));
		}
	}

	@Test
	void testMultMatVecNull2() {
		try {
			engine.solve("MULT", new FactBase<double[][], double[]>(matN2, vec1));
			fail("no exception with NP facts thrown");
		} catch (NullPointerException error) {
			String msg = error.getMessage();
			assertNotNull(msg, "empty exception is meaningless");
			assertTrue(error.getMessage().contains("length is zero"));
		}
	}

	@Test
	void testMultMatVecNull3() {
		try {
			engine.solve("MULT", new FactBase<double[][], double[]>(mat1, vecN));
			fail("no exception with NP facts thrown");
		} catch (NullPointerException error) {
			String msg = error.getMessage();
			assertNotNull(msg, "empty exception is meaningless");
			assertTrue(error.getMessage().contains("length is zero"));
		}
	}
	
	@Test
	void testNoIris() {
		try {
			new MatVecCalc().solve("ASDASD", new FactBase<double[][], double[]>(mat1, vecN));
			fail("no exception with no Iris thrown");
		} catch (IllegalStateException error) {
			String msg = error.getMessage();
			assertNotNull(msg, "empty exception is meaningless");
			assertTrue(error.getMessage().contains("no Iris"));
		}
	}
	
	@Test
	void testNotPossible() {
		try {
			new MatVecCalc().solve("PLUS", new FactBase<double[][], double[]>(mat1, vecN));
			fail("no exception with not possible thrown");
		} catch (IllegalArgumentException error) {
			String msg = error.getMessage();
			assertNotNull(msg, "empty exception is meaningless");
			assertTrue(error.getMessage().contains("not possible"));
		}
	}

	@Test
	void testAddVecVecDim() {
		try {
			engine.solve("PLUS", new FactBase<double[], double[]>(vec1, vec3));
			fail("no exception with NP facts thrown");
		} catch (IllegalArgumentException error) {
			String msg = error.getMessage();
			assertNotNull(msg, "empty exception is meaningless");
			assertTrue(error.getMessage().contains("dimensions wrong!"));
		}
	}

	@Test
	void testMultVecVecDim() {
		try {
			engine.solve("MULT", new FactBase<double[], double[]>(vec1, vec3));
			fail("no exception with NP facts thrown");
		} catch (IllegalArgumentException error) {
			String msg = error.getMessage();
			assertNotNull(msg, "empty exception is meaningless");
			assertTrue(error.getMessage().contains("dimensions wrong!"));
		}
	}

	@Test
	void testAddMatMatDim() {
		try {
			engine.solve("PLUS", new FactBase<double[][], double[][]>(mat3, mat4));
			fail("no exception with NP facts thrown");
		} catch (IllegalArgumentException error) {
			String msg = error.getMessage();
			assertNotNull(msg, "empty exception is meaningless");
			assertTrue(error.getMessage().contains("dimensions wrong!"));
		}
	}

	@Test
	void testMultMatMatDim() {
		try {
			engine.solve("MULT", new FactBase<double[][], double[][]>(mat1, mat3));
			fail("no exception with NP facts thrown");
		} catch (IllegalArgumentException error) {
			String msg = error.getMessage();
			assertNotNull(msg, "empty exception is meaningless");
			assertTrue(error.getMessage().contains("dimensions wrong!"));
		}
	}

	@Test
	void testMultMatVecDim() {
		try {
			engine.solve("MULT", new FactBase<double[][], double[]>(mat4, vec1));
			fail("no exception with NP facts thrown");
		} catch (IllegalArgumentException error) {
			String msg = error.getMessage();
			assertNotNull(msg, "empty exception is meaningless");
			assertTrue(error.getMessage().contains("dimensions wrong!"));
		}
	}

	@Test
	void testMultiplyHilbertMatrix() {
		double delta = 1.E-8;
		for (int n = 1; n <= 7; n++) {
			double[][] a = createHilbertMatrix(n);
			double[][] b = createInverseHilbertMatrix(n);
			double[][] expected = createIdMatrix(n);
			//Normal
			double[][] returned = engine.solve("MULT", new FactBase<double[][], double[][]>(a, b));
			double[][] returned2 = engine.solve("MULT", new FactBase<double[][], double[][]>(a, b, false));
			//Parallel
			double[][] returned3 = engine.solve("MULT", new FactBase<double[][], double[][]>(a, b, true));
			assertMatrixEquals(expected, returned, delta);
			assertMatrixEquals(expected, returned2, delta);
			assertMatrixEquals(expected, returned3, delta);
		}
	}

	@Test
	void testSolveMultMatSpeed() {
		System.out.printf("+------+-----------+------------------+------------------+---------+--------+%n");
		System.out.printf("|  dim |  baseline |     optimized    |   parallelized   | speedUp | Amdahl |%n");
		System.out.printf("+------+-----------+----------+-------+---------+--------+---------+--------+%n");
		System.out.printf("|   n  |   b[μs]   |   s[μs]  |  b/s  |  p[μs]  |   b/p  |   s/p   |  pi[%%] |%n");
		System.out.printf("+------+-----------+----------+-------+---------+--------+---------+--------+%n");
		String format = "|%5d |%10d |%9d |%6.2f |%8d |%7.2f |%8.2f |%7.0f |%n";
		int proc = Runtime.getRuntime().availableProcessors();

		for (int i = 128; i <= 512; i = i * 2) {
			double[][] a = createRndMat(i, i);
			double[][] b = createRndMat(i, i);
			// naive
			long start1 = System.nanoTime();
			double[][] expected = mult(a, b);
			long end1 = System.nanoTime();

			// optimized
			long start2 = System.nanoTime();
			double[][] returned = engine.solve("MULT", new FactBase<double[][], double[][]>(a, b, false));
			long end2 = System.nanoTime();
			assertMatrixEquals(expected, returned, tolerance);

			// parallelized
			long start3 = System.nanoTime();
			double[][] returned2 = engine.solve("MULT", new FactBase<double[][], double[][]>(a, b, true));
			long end3 = System.nanoTime();
			assertMatrixEquals(expected, returned2, tolerance);

			double opt = ((double) (end1 - start1)) / ((double) (end2 - start2));
			double paraOpt = ((double) (end1 - start1)) / ((double) (end3 - start3));
			double speedUp = ((double) (end2 - start2)) / ((double) (end3 - start3));

			double amdahl = (proc - (proc * speedUp)) / (speedUp * (-proc + 1));
			if (amdahl < 0)
				amdahl = 0;

			System.out.printf(format, i, (end1 - start1) / 1000, (end2 - start2) / 1000, opt,
					(end3 - start3) / 1000, paraOpt, speedUp, amdahl * 100);
		}
		System.out.printf("+------+-----------+----------+-------+---------+--------+---------+--------+%n");
	}

	/**
	 * Helper Methods
	 */

	private void assertEqualsDelta(double a, double b, double delta) {
		if (Math.abs(a) > 1) {
			delta *= Math.abs(a);
		}
		assertEquals(a, b, delta);
	}

	private void assertVectorEquals(double[] a, double[] b, double delta) {
		int n = a.length;
		assertEquals(n, b.length, "vector dimensions differ");
		for (int i = 0; i < n; i++) {
			assertEqualsDelta(a[i], b[i], delta);
		}
	}

	private void assertMatrixEquals(double[][] a, double[][] b, double delta) {
		int n = a.length;
		assertEquals(n, b.length, "matrix dimensions differ");
		for (int i = 0; i < n; i++) {
			assertVectorEquals(a[i], b[i], delta);
		}
	}

	private double[][] createRndMat(int a, int b) {
		double[][] mat = new double[a][b];
		for (int i = 0; i < a; i++) {
			mat[i] = createRndVec(b);
		}
		return mat;
	}

	private double[] createRndVec(int a) {
		double[] vec = new double[a];
		for (int i = 0; i < a; i++) {
			vec[i] = rd.nextDouble();
		}
		return vec;
	}

	private double[] add(double[] a, double[] b) {
		int n = a.length;
		assertEquals(n, b.length);
		double[] result = new double[n];
		for (int i = 0; i < n; i++) {
			result[i] = a[i] + b[i];
		}
		return result;
	}

	private double[][] add(double[][] a, double[][] b) {
		int n1 = a.length;
		int n2 = a[0].length;
		assertEquals(n1, b.length);
		assertEquals(n2, b[0].length);
		double[][] result = new double[n1][n2];
		for (int i = 0; i < n1; i++) {
			for (int j = 0; j < n2; j++) {
				result[i][j] = a[i][j] + b[i][j];
			}
		}
		return result;
	}

	private double mult(double[] a, double[] b) {
		double result = 0;
		for (int i = 0; i < a.length; i++) {
			result = result + a[i] * b[i];
		}
		return result;
	}

	private double[][] mult(double[][] firstMatrix, double[][] secondMatrix) {
		assertEquals(firstMatrix[0].length, secondMatrix.length);
		double[][] result = new double[firstMatrix.length][secondMatrix[0].length];

		for (int row = 0; row < result.length; row++) {
			for (int col = 0; col < result[row].length; col++) {
				result[row][col] = 0;
				for (int i = 0; i < secondMatrix.length; i++) {
					result[row][col] += firstMatrix[row][i] * secondMatrix[i][col];
				}
			}
		}

		return result;
	}

	private double[] mult(double[][] a, double[] x) {
		int m = a.length;
		int n = a[0].length;
		assertEquals(n, x.length);
		double[] y = new double[m];
		for (int i = 0; i < m; i++)
			for (int j = 0; j < n; j++)
				y[i] += a[i][j] * x[j];
		return y;
	}

	private double[][] createIdMatrix(int n) {
		double h[][] = new double[n][n];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i == j)
					h[i][j] = 1;
				else
					h[i][j] = 0;
			}
		}

		return h;
	}

	private double[][] createInverseHilbertMatrix(int n) {
		double h[][] = new double[n][n];

		for (int i = 1; i < n + 1; i++) {
			for (int j = 1; j < n + 1; j++) {
				h[i - 1][j - 1] = (Math.pow(-1, i + j) * fac(i + n - 1) * fac(j + n - 1))
						/ (Math.pow(fac(i - 1), 2) * Math.pow(fac(j - 1), 2) * fac(n - j) * fac(n - i) * (i + j - 1));
			}
		}

		return h;
	}

	private double fac(int n) {
		double fact = 1;
		for (int i = 2; i <= n; i++) {
			fact = fact * i;
		}
		return fact;
	}

	private double[][] createHilbertMatrix(int n) {
		double h[][] = new double[n][n];

		for (int i = 1; i < n + 1; i++) {
			for (int j = 1; j < n + 1; j++) {
				h[i - 1][j - 1] = 1.0 / (i + j - 1.0);
			}
		}

		return h;
	}
}
