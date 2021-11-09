package de.lab4inf.axela.math;

import de.lab4inf.axela.core.Iris;
import de.lab4inf.axela.facts.FactBase;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;

public abstract class LinearAlgebra<Solution, F1, F2> implements Iris<String, FactBase<F1, F2>, Solution> {

	String FACTS_ARE_A_NULL_POINTER = "facts are a NullPointer!";
	String PROBLEM_IS_A_NULL_POINTER = "problem is a NullPointer!";
	String NO_IRIS_FOR_PROBLEM_FOUND = "no Iris for problem: %s found";
	String ZERO_LENGTH = "length is zero";

	@Override
	public Solution solve(String problem, FactBase<F1, F2> facts) {
		Objects.requireNonNull(problem, PROBLEM_IS_A_NULL_POINTER);
		Objects.requireNonNull(facts, FACTS_ARE_A_NULL_POINTER);

		if (problem.equals("PLUS")) {
			return add(facts.getFact1(), facts.getFact2());
		}
		if (problem.equals("MULT")) {
			return mult(facts.getFact1(), facts.getFact2(), facts.getSpeedUp());
		}

		throw new IllegalStateException(format(NO_IRIS_FOR_PROBLEM_FOUND, problem));
	}

	protected abstract Solution add(F1 a, F2 b);

	protected abstract Solution mult(F1 a, F2 b, boolean speedUp);

	protected abstract void check(F1 a, F2 b);

	static public class VectorCalc extends LinearAlgebra<Object, double[], double[]> {
		@Override
		protected double[] add(double[] a, double[] b) {
			check(a, b);
			if (a.length != b.length)
				throw new IllegalArgumentException("dimensions wrong!");

			int len = a.length;
			double[] result = new double[len];
			for (int i = 0; i < len; i++) {
				result[i] = a[i] + b[i];
			}
			return result;
		}

		@Override
		protected Double mult(double[] a, double[] b, boolean speedUp) {
			check(a, b);
			if (a.length != b.length)
				throw new IllegalArgumentException("dimensions wrong!");

			int len = a.length;
			double result = 0;
			for (int i = 0; i < len; i++) {
				result += a[i] * b[i];
			}
			return result;
		}

		@Override
		protected void check(double[] a, double[] b) {
			if (a.length == 0 || b.length == 0)
				throw new NullPointerException(ZERO_LENGTH);
		}
	}

	static public class MatrixCalc extends LinearAlgebra<double[][], double[][], double[][]> {
		@Override
		protected double[][] add(double[][] a, double[][] b) {
			check(a, b);
			if (a.length != b.length || a[0].length != b[0].length)
				throw new IllegalArgumentException("dimensions wrong!"); // Errorhandling illegal addition

			int len_x = a.length;
			int len_y = a[0].length;
			double[][] result = new double[len_x][len_y];
			for (int i = 0; i < len_x; i++) {
				for (int k = 0; k < len_y; k++) {
					result[i][k] = a[i][k] + b[i][k];
				}
			}
			return result;
		}

		@Override
		protected double[][] mult(double[][] a, double[][] b, boolean speedUp) {
			check(a, b);
			if (a[0].length != b.length)
				throw new IllegalArgumentException("dimensions wrong!"); // Zeilenzahl von a muss gleich Spaltenzahl
																			// von, B sein, ansonsten keine
																			// multiplikation moeglich

			// Algorithmus 3
			ThreadPoolExecutor pool;
			if (speedUp)
				pool = (ThreadPoolExecutor) Executors.newCachedThreadPool(); // Multithread
			else
				pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(1); // Singlethread

			double[][] result = new double[a.length][b[0].length];
			double[][] transB = transponse(b);

			for (int i = 0; i < a.length; i++) {
				final int outI = i;
				Runnable task = new Runnable() {
					@Override
					public void run() {
						matMultInnerLoop(a[outI], result[outI], transB, outI);
					}
				};
				pool.execute(task);
			}

			pool.shutdown();

			try {
				if (!pool.awaitTermination(300, TimeUnit.SECONDS)) {
					throw new IllegalStateException("Task timed out");
				}
			} catch (InterruptedException e) {
				pool.shutdownNow();
				e.printStackTrace();
			}

			return result;

		}

		private void matMultInnerLoop(double[] a, double[] result, double[][] transB, int i) {
			for (int j = 0; j < transB.length; j++) {
				result[j] = 0;
				for (int k = 0; k < a.length; k++) {
					result[j] += a[k] * transB[j][k];
				}
			}
		}

		protected double[][] transponse(double[][] a) {
			double[][] b = new double[a[0].length][a.length];
			for (int i = 0; i < a.length; i++) {
				for (int j = 0; j < a[0].length; j++) {
					b[j][i] = a[i][j];
				}
			}
			return b;
		}

		@Override
		protected void check(double[][] a, double[][] b) {
			if (a.length == 0 || b.length == 0 || a[0].length == 0 || b[0].length == 0)
				throw new NullPointerException(ZERO_LENGTH);
		}
	}

	static public class MatVecCalc extends LinearAlgebra<double[], double[][], double[]> {
		@Override
		protected double[] add(double[][] a, double[] b) {
			throw new IllegalArgumentException("not possible");
		}

		@Override
		protected double[] mult(double[][] a, double[] b, boolean speedUp) {
			check(a, b);

			if (a[0].length != b.length)
				throw new IllegalArgumentException("dimensions wrong!"); // illegal multiplication handling

			double[] result = new double[a.length]; // result placeholder
			for (int i = 0; i < a.length; i++) { // iterate through lines
				double temp = 0;
				for (int k = 0; k < a[0].length; k++) { // iterate through columns
					temp += a[i][k] * b[k]; // calculate result
				}
				result[i] = temp; // apply result
				temp = 0; // reset temp
			}

			return result;
		}

		@Override
		protected void check(double[][] a, double[] b) {
			if (a.length == 0 || b.length == 0 || a[0].length == 0)
				throw new NullPointerException(ZERO_LENGTH);
		}
	}
}