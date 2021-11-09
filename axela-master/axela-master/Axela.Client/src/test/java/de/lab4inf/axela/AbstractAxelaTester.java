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

package de.lab4inf.axela;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;

/**
 * Common base class for Axela tests.
 * 
 * 
 * @author nwulff
 * @since 30.11.2020
 */
public abstract class AbstractAxelaTester {
	// protected static final System.Logger logger = System.getLogger("Axela");
	private static final double SCALING[] = { 1., 10., 100., 1.E3, 1.E4, 1.E5, 1.E6, 1.E7, 1.E8 };
	protected double tolerance = 5.E-12;

	protected final static void log(String fmt, Object... args) {
		System.out.printf(Locale.US, format("%s\n", fmt), args);
	}

	protected final static void warning(String fmt, Object... args) {
		System.err.printf(Locale.US, format("%s\n", fmt), args);
	}

	protected final static double rnd(double scale, int precission) {
		if (precission < 0 || precission >= SCALING.length) {
			throw new IllegalArgumentException(format("illegal precission: %d", precission));
		}
		double rnd = 2 * scale * (Math.random() - 0.5);
		long round = (long) (rnd * SCALING[precission]);
		rnd = round / SCALING[precission];
		return rnd;
	}

	protected final static double rnd() {
		return rnd(10.0);
	}

	protected final static double rnd(double scale) {
		return rnd(scale, 2);
	}

	protected final static double rnd(int precission) {
		return rnd(10.0, precission);
	}

	protected final static void assertRelativeEquals(double expected, double returned, double eps) {
		if (Math.abs(expected) > 1.0) {
			eps = Math.abs(expected);
		}
		assertEquals(expected, returned, eps);
	}

	protected final static void assertVectorEquals(double[] expected, double[] returned, double eps) {
		assertEquals(expected.length, returned.length);
		for (int j = 0; j < expected.length; j++)
			assertRelativeEquals(expected[j], returned[j], eps);
	}

	protected final static void assertMatrixEquals(double[][] expected, double[][] returned, double eps) {
		assertEquals(expected.length, returned.length);
		for (int j = 0; j < expected.length; j++)
			assertVectorEquals(expected[j], returned[j], eps);
	}
}
