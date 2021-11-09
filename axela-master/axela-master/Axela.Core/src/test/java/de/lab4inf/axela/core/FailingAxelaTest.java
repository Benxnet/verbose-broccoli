/*
 * Project: Axela.Core
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

package de.lab4inf.axela.core;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

/**
 * Internal Test which checks the failure of the Axela.getEngine() method
 * without implementation within the core module.
 * 
 * @author nwulff
 * @since 29.09.2020
 */
final class FailingAxelaTest {

	@Test
	void testGetNoEngine() {
		try {
			Axela.getEngine();
			fail("Expected no Axela engine within the core module but an exception to be thrown");
		} catch (IllegalStateException e) {
			assertTrue(e.getMessage().contains(Axela.NO_AXELA_FOUND));
		}
	}

}
