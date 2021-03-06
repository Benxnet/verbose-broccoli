/*
 * Project: Axela.Math
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

package de.lab4inf.axela.math.ast;

/**
 * Node for integer literals.
 * 
 * @author nwulff
 * @since 26.10.2020
 */
public class LongNode extends NumericNode<Long> {

	/**
	 * @param value of this node
	 */
	public LongNode(int value) {
		this((long) value);
	}

	/**
	 * @param value of this node
	 */
	public LongNode(Long value) {
		super(value);
	}

}
