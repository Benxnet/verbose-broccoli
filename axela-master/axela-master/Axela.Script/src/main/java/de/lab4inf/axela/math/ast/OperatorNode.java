/*
 * Project: Axela.Script
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

import de.lab4inf.axela.script.Node;

/**
 * Node for a mathematical operator.
 * 
 * @author nwulff
 * @since 27.11.2020
 */
public interface OperatorNode extends Node<AstOperator> {
	/**
	 * Get the precedence of this node.
	 * 
	 * @return the operator precedence
	 */
	default int getPrecedence() {
		return getPayload().getPrecedence();
	}

	/**
	 * Is this operator commutative.
	 * 
	 * @return commutative flag
	 */
	default boolean isCommutative() {
		return getPayload().isCommutative();
	}

	/**
	 * Is this operator left to right associative.
	 * 
	 * @return left to right flag
	 */
	default boolean isLeftToRight() {
		return getPayload().isLeftToRight();
	}

}
