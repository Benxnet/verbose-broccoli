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

package de.lab4inf.axela.praktikumV;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.*;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.lab4inf.axela.core.Axela;
import de.lab4inf.axela.core.Iris;
import de.lab4inf.axela.math.ast.*;
import de.lab4inf.axela.praktikumIV.Praktikum4Test;
import de.lab4inf.axela.script.*;

/**
 * Praktikum V black-box test case.
 * 
 * 
 * @author nwulff
 * @since 30.11.2020
 */
public class Praktikum5Test extends Praktikum4Test {
	protected static Axela orignal;
	protected static String FCT = "Function";
	protected static String PRAKT5 = "prak5";

	/**
	 * Simple visitor using only matrix and vector nodes.
	 */
	static class StringVisitor extends BaseVisitor<String> {

		@Override
		protected <S> String error(String fmt, S state) {
			return state.toString();
		}

		@Override
		public String visit(MathOperatorNode an) {
			String ls = an.getLeft().accept(this);
			String rs = an.getRight().accept(this);
			return format("%s%s%s", ls, an.getPayload(), rs);
		}

		public String toString(double[] v) {
			int len = v.length - 1;
			StringBuilder parms = new StringBuilder("[");
			for (int k = 0; k < len; k++) {
				parms.append(v[k]);
				parms.append(",");
			}
			parms.append(v[len]);
			parms.append("]");
			return parms.toString();
		}

		public String toString(double[][] mat) {
			int len = mat.length - 1;
			StringBuilder parms = new StringBuilder("[");
			for (int k = 0; k < len; k++) {
				parms.append(toString(mat[k]));
				parms.append(",");
			}
			parms.append(toString(mat[len]));
			parms.append("]");
			return parms.toString();
		}

		@Override
		public String visit(VectorNode vn) {
			double[] v = vn.getPayload();
			return toString(v);
		}

		@Override
		public String visit(MatrixNode mn) {
			double[][] mat = mn.getPayload();
			return toString(mat);
		}
	}

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	public static void setAxelaProxy() throws Exception {
		assertNotNull(axela, "no Axela found");
		orignal = axela;
		Class<?>[] interfaces = { Axela.class };
		InvocationHandler handler = (proxy, method, args) -> {
			Throwable error = null;
			try {
				log("call: %s:%s", orignal.getClass().getSimpleName(), method.getName());
				return method.invoke(orignal, args);
			} catch (InvocationTargetException e) {
				error = e.getTargetException();
				warning("call has an error: %s", error);
				throw error;
			} finally {
				log("call %s %s", method.getName(), error != null ? "with exception " + error : "finished regulary");
			}
		};
		axela = (Axela) Proxy.newProxyInstance(axela.getClass().getClassLoader(), interfaces, handler);

		final String[] factSignature = new String[0];
		Iris<String, String[], String> strIris = (p, f) -> {
			List<Node<?>> nodes = axela.solve("Parse", f);
			StringVisitor sv = new StringVisitor();
			StringBuilder sb = new StringBuilder();
			for (Node<?> n : nodes) {
				String s = n.accept(sv);
				sb.append(s);
				sb.append(";");
			}
			return sb.toString();
		};
		// Testing the registration of an arbitrary visitors
		if (!axela.hasSolverFor(PRAKT5, factSignature)) {
			axela.registerSolver(PRAKT5, factSignature, strIris);
		}
	}

	/**
	 * Testing is the arbitrary visitor found
	 */
	@Test
	protected void testMatrixVectorString() throws Exception {
		double a11 = 1.1, a12 = 1.2, a21 = 2.1, a22 = 2.2;
		double x1 = 0.1, x2 = 0.2, y1, y2;
		String task = "A={{%.1f,%.1f},{%.1f,%.1f}};x={%.1f,%.1f};y=A*x;z=y*y;";
		String[] script = prepareTask(task, a11, a12, a21, a22, x1, x2);
		y1 = a11 * x1 + a12 * x2;
		y2 = a21 * x1 + a22 * x2;
		log("%f,%f", y1, y2);
		// TODO math calculation not required yet ...
		String expected = script[0].replace('{', '[').replace('}', ']');
		String returned = axela.solve(PRAKT5, script);
		log("Prak5: %s", returned);
		assertEquals(expected, returned);
	}

	@Test
	protected void testAssign1() throws Exception {
		double x = rnd(2);
		String[] script = prepareTask("x = %.2f;", x);
		double expected = x;
		double returned = axela.solve(SCRIPT, script);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	protected void testAssign2() throws Exception {
		double x = rnd(2), y = rnd(2);
		String[] script = prepareTask("x = %.2f; y=%.2f;", x, y);
		double expected = y;
		double returned = axela.solve(SCRIPT, script);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	protected void testAssign3() throws Exception {
		double x = rnd(2), y = rnd(2);
		String[] script = prepareTask("x = %.2f; y=%.2f; z = x - y", x, y);
		double expected = x - y;
		double returned = axela.solve(SCRIPT, script);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	protected void testFunction1() throws Exception {
		double x = rnd(2);
		String[] script = prepareTask("x = %.2f; f(x) = x*x; z =f(x)", x);
		double expected = x * x;
		double returned = axela.solve(SCRIPT, script);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	protected void testFunction2() throws Exception {
		double x = rnd(2), y = rnd(2);
		String[] script = prepareTask("a = %.2f; b= %.2f; f(y,x) = x**y; z =f(a,b)", x, y);
		double expected = Math.pow(y, x);
		double returned = axela.solve(SCRIPT, script);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	protected void testFunction3() throws Exception {
		double x = rnd(2), y = rnd(2);
		String[] script = prepareTask("x = %.2f; y= %.2f; f(a) = a+2; z=f(y)", x, y);
		double expected = y + 2;
		double returned = axela.solve(SCRIPT, script);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	protected void testFunction4() throws Exception {
		double x = rnd(2), y = rnd(2);
		String[] script = prepareTask("x = %.2f; y= %.2f; f(x) = x+2; g(x)=x*x; z=f(y)+g(x)", x, y);
		double expected = y + 2 + x * x;
		double returned = axela.solve(SCRIPT, script);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	protected void testFunction5() throws Exception {
		double x = rnd(2), y = rnd(2);
		String[] script = prepareTask("x = %.2f; y= %.2f; f(x) = x+2; g(x)=x*x; h(z)=g(f(z)); z=h(x)", x, y);
		double expected = (x + 2) * (x + 2);
		double returned = axela.solve(SCRIPT, script);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	protected void testFunction6() throws Exception {
		double x = rnd(2), y = rnd(2);
		String[] script = prepareTask("x = %.2f; y= %.2f; f(x) = x+2; z=f(3*y)", x, y);
		double expected = 3 * y + 2;
		double returned = axela.solve(SCRIPT, script);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	protected void testFunction7() throws Exception {
		double x = rnd(2);
		String[] script = prepareTask("x = %.2f; f(x) = sin(x); z = f(x);", x);
		double expected = Math.sin(x);
		double returned = axela.solve(SCRIPT, script);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	protected void testFunction8() throws Exception {
		double x = rnd(0.5);
		String[] script = prepareTask("x = %.2f; f(x) = sin(asin(x)); z = f(x);", x);
		double expected = x;
		double returned = axela.solve(SCRIPT, script);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	protected void testFunction9() throws Exception {
		double x = rnd(2);
		String[] script = prepareTask("x = %.2f; f(x) = sin(x)**2 + cos(x)**2; z = f(x);", x);
		double expected = 1;
		double returned = axela.solve(SCRIPT, script);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	protected void testFunction10() throws Exception {
		double x = rnd(2.0);
		String[] script = prepareTask("x = %.2f; z = ln(exp(3*x)) + log(10**x);", x);
		double expected = 4 * x;
		double returned = axela.solve(SCRIPT, script);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	protected void testFunction11() throws Exception {
		double x, returned, expected;
		String[] script = prepareTask("f(x) = x*x + 2");
		axela.solve(SCRIPT, script);
		AxelaFunction fct = axela.solve(FCT, prepareTask("f"));
		assertNotNull(fct, "no function returned");
		for (int j = 0; j < 5; j++) {
			x = rnd(2.0);
			expected = x * x + 2;
			returned = fct.apply(x);
			assertEquals(expected, returned, tolerance);
		}
	}

	@Test
	protected void testFunction12() throws Exception {
		double x, y, returned, expected;
		String[] script = prepareTask("f(x) = x*x + 2; g(y,x)=exp(y*sin(x))");
		axela.solve(SCRIPT, script);
		AxelaFunction fct1 = axela.solve(FCT, prepareTask("f"));
		AxelaFunction fct2 = axela.solve(FCT, prepareTask("g"));
		assertNotNull(fct1, "no function f returned");
		assertNotNull(fct2, "no function g returned");
		for (int j = 0; j < 5; j++) {
			x = rnd(2.0);
			expected = x * x + 2;
			returned = fct1.apply(x);
			assertEquals(expected, returned, tolerance);
			x = rnd(2.0);
			y = rnd(2.0);
			expected = Math.exp(x * Math.sin(y));
			returned = fct2.apply(x, y);
			assertEquals(expected, returned, tolerance);
		}
	}

	@Test
	protected void testFunction13() throws Exception {
		double x, y, z, returned, expected;
		String[] script = prepareTask("f(z,y,x) = x+y*z;");
		axela.solve(SCRIPT, script);
		AxelaFunction fct = axela.solve(FCT, prepareTask("f"));
		assertNotNull(fct, "no function returned");
		for (int j = 0; j < 5; j++) {
			x = rnd(2);
			y = rnd(2);
			z = rnd(2);
			expected = z + y * x;
			returned = fct.apply(x, y, z);
			assertEquals(expected, returned, tolerance);
		}
	}

	@Test
	protected void testFunction14() throws Exception {
		double x, returned, expected;
		String[] script = prepareTask("f(x,y,z) = x+y*z;");
		axela.solve(SCRIPT, script);
		AxelaFunction fct1 = axela.solve(FCT, prepareTask("f"));
		assertNotNull(fct1, "no function f returned");
		AxelaFunction fct2 = axela.solve(FCT, prepareTask("cos"));
		assertNotNull(fct2, "no function cos returned");
		for (int j = 0; j < 5; j++) {
			x = rnd(2);
			expected = Math.cos(x);
			returned = fct2.apply(x);
			assertEquals(expected, returned, tolerance);
		}
	}
}
