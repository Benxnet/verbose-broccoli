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

import java.util.Objects;

/**
 * Enumeration of some common operators.
 * 
 * @author nwulff
 * @since 26.10.2020
 */
public enum AstOperator {
	/** symbol for dummy operator. */
	NULL("°", -1, true, true),
	/** symbol for assignment. */
	ASSIGN("=", 0, false, true),
	/** symbol for addition. */
	PLUS("+", 1, true, true),
	/** symbol for subtraction. */
	MINUS("-", 1, false, true),
	/** symbol for multiplication. */
	TIMES("*", 2, true, true),
	/** symbol for division. */
	DIVIDE("/", 2, false, true),
	/** symbol for power. */
	POWER("^", 3, false, false);

	AstOperator(String symbol, int precedence, boolean commutative, boolean leftToRight) {
		this.symbol = Objects.requireNonNull(symbol);
		this.precedence = precedence;
		this.commutative = commutative;
		this.leftToRight = leftToRight;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return symbol;
	}

	/**
	 * The precedence of this operator.
	 * 
	 * @return precedence value
	 */
	public int getPrecedence() {
		return precedence;
	}

	/**
	 * Indicate if operator is commutative
	 * 
	 * @return commutative flag
	 */
	public boolean isCommutative() {
		return commutative;
	}

	/**
	 * Indicate if this operator goes left to right
	 * 
	 * @return left to right flag
	 */
	public boolean isLeftToRight() {
		return leftToRight;
	}

	private final boolean leftToRight;
	private final boolean commutative;
	private final int precedence;
	private final String symbol;
}
