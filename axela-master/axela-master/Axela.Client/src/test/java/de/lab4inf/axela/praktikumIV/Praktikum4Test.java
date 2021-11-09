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

package de.lab4inf.axela.praktikumIV;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Locale;

import javax.script.*;

import org.junit.jupiter.api.*;

import de.lab4inf.axela.AbstractAxelaTester;
import de.lab4inf.axela.core.Axela;
import de.lab4inf.axela.script.AxelaScriptEngineFactory;

/**
 * Praktikum IV black-box test case.
 * 
 * 
 * @author nwulff
 * @since 30.11.2020
 */
public class Praktikum4Test extends AbstractAxelaTester {
	protected static final String NONSENCE_NOT_DETECTED = "nonsence not detected";
	protected static final String SCRIPT = "Script";
	protected static final ScriptEngineManager manager;
	protected static ScriptEngine engine;
	protected static Axela axela;
	static {
		manager = new ScriptEngineManager();
		// the Axela engine should now be found!
		try {
			axela = Axela.getEngine();
		} catch (Throwable e) {
			// this will be tested later on ...
		}
	}

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		assertNotNull(axela, "no Axela found");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	public void setUp() throws Exception {
		if (null == engine) {
			List<ScriptEngineFactory> factories = manager.getEngineFactories();
			for (ScriptEngineFactory factory : factories) {
				log("found ScriptEngine: %s\t Extension %s", factory.getEngineName(), factory.getExtensions());
				if (factory.getEngineName().contains("Axela")) {
					engine = factory.getScriptEngine();
				}
			}
			log("");
		}
		// ok give the script engine a chance if not found ...
		if (null == engine) {
			warning("no AxelaScriptEngine found");
			engine = new AxelaScriptEngineFactory().getScriptEngine();
		}
		// TODO:
		// if an Axela ScriptEngine has been found it should also
		// register via the Axela.Plugin mechanism all it's solvers!!
		assertNotNull(engine, "no Axela ScriptEngine found");
	}

	protected final static String[] prepareTask(String fmt, Object... args) {
		String task = format(Locale.US, fmt, args);
		log("submit task: %s", task);
		return new String[] { task };
	}

	@Test
	protected void testAddition1() throws Exception {
		double x = 0.5, y = 1.3;
		String[] script = prepareTask("%.2f + %.2f", x, y);
		double expected = Double.valueOf(x + y);
		double returned = axela.solve(SCRIPT, script);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	protected void testAddition2() throws Exception {
		double x = 0.5, y = 1.3;
		long z = -5;
		String[] script = prepareTask("%.2f + %.2f + %d", x, y, z);
		double expected = Double.valueOf(x + y + z);
		double returned = axela.solve(SCRIPT, script);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	protected void testSubstraction1() throws Exception {
		double x = .25, y = rnd();
		String[] script = prepareTask("%.2f - %.2f", x, y);
		double expected = Double.valueOf(x - y);
		double returned = axela.solve(SCRIPT, script);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	protected void testSubstraction2() throws Exception {
		double x = rnd(2), y = -1.25;
		String[] script = prepareTask("%.2f - %.2f", x, y);
		double expected = Double.valueOf(x - y);
		double returned = axela.solve(SCRIPT, script);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	protected void testMultiplication1() throws Exception {
		double x = rnd(), y = rnd();
		String[] script = prepareTask("%.2f * %.2f", x, y);
		double expected = Double.valueOf(x * y);
		double returned = axela.solve(SCRIPT, script);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	void testMultiplication2() throws Exception {
		for (int k = 0; k < 4; k++) {
			double x = rnd(), y = rnd(), z = rnd();
			String[] script = prepareTask("%.2f * %.2f * %.2f", x, y, z);
			double expected = Double.valueOf(x * y * z);
			double returned = axela.solve(SCRIPT, script);
			assertEquals(expected, returned, tolerance);
		}
	}

	@Test
	void testDivision1() throws Exception {
		double x = .75, y = -1.25;
		String[] script = prepareTask("%.2f / %.2f", x, y);
		double expected = Double.valueOf(x / y);
		double returned = axela.solve(SCRIPT, script);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	void testPower1() throws Exception {
		double x = rnd(), y = rnd(1.0);
		String[] script = prepareTask("%.2f ^ %.2f", x, y);
		double expected = Double.valueOf(Math.pow(x, y));
		double returned = axela.solve(SCRIPT, script);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	void testPower2() throws Exception {
		double x = rnd(3.0), y = rnd(2.0);
		String[] script = prepareTask("%.2f ** %.2f", x, y);
		double expected = Double.valueOf(Math.pow(x, y));
		double returned = axela.solve(SCRIPT, script);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	void testPower3() throws Exception {
		double x = rnd(2.0), y = rnd(2.0);
		String[] script = prepareTask("%.2f*%.2f ** (%.2f - %.2f)", y, x, x, y);
		double expected = y * Math.pow(x, (x - y));
		double returned = axela.solve(SCRIPT, script);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	// @Disabled
	void testPower4() throws Exception {
		double x = rnd(1.0), y = rnd(1.0), z = rnd(1.0);
		// test for right associative
		// String[] script = prepare("(%.3f) ^ ((%.3f) ^ (%.3f))", x, y, z);
		String[] script = prepareTask("%.3f ^ %.3f ^ %.3f", x, y, z);
		double expected = Math.pow(x, Math.pow(y, z));
		double returned = axela.solve(SCRIPT, script);
		assertEquals(expected, returned, tolerance, script[0]);
	}

	@Test
	void testMixedExpression() throws Exception {
		double x = rnd(), y = rnd(), z = rnd(), u = rnd(), v = rnd();
		// precedence of the operators should be honored
		String[] script = prepareTask("%.2f + %.2f * %.2f / %.2f + %.2f", x, y, z, u, v);
		double expected = Double.valueOf(x + y * z / u + v);
		double returned = axela.solve(SCRIPT, script);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	void testBracketExpression() throws Exception {
		double x = rnd(), y = rnd(), z = rnd(), u = rnd(), v = rnd();
		// brackets should be resolved
		String[] script = prepareTask("(%.2f + %.2f)*%.2f - (%.2f+%.2f)", x, y, z, u, v);
		double expected = Double.valueOf((x + y) * z - (u + v));
		double returned = axela.solve(SCRIPT, script);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	void testWrongBracket1() throws Exception {
		double x = 0.5, y = 1.3;
		String[] script = prepareTask("((%.2f + %.2f", x, y);
		try {
			axela.solve(SCRIPT, script);
			fail(NONSENCE_NOT_DETECTED);
		} catch (IllegalArgumentException e) {
			// that's fine ...
		}
	}

	@Test
	void testWrongBracket2() throws Exception {
		double x = 0.5, y = 1.3;
		String[] script = prepareTask("((%.2f + )) %.2f", x, y);
		try {
			axela.solve(SCRIPT, script);
			fail(NONSENCE_NOT_DETECTED);
		} catch (IllegalArgumentException e) {
			// that's fine ...
		}
	}

	@Test
	void testWrongBracket3() throws Exception {
		double x = 0.5, y = 1.3;
		String[] script = prepareTask("(%.2f + )) (%.2f", x, y);
		try {
			axela.solve(SCRIPT, script);
			fail(NONSENCE_NOT_DETECTED);
		} catch (IllegalArgumentException e) {
			// that's fine ...
		}
	}

	@Test
	void testManyExpressions() throws Exception {
		String[] script = { "3+7", "11-5", "2**3" };
		// the last task should be returned without saving to variables
		double expected = 8.0;
		double returned = axela.solve(SCRIPT, script);
		assertEquals(expected, returned, tolerance);
	}

}
