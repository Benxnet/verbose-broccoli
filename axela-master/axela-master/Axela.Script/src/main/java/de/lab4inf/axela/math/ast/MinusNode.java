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

import de.lab4inf.axela.script.Node;

/**
 * Minus node
 * 
 * @author nwulff
 * @since 30.10.2020
 */
public class MinusNode extends MathOperatorNode {

	/**
	 * Pojo constructor.
	 */
	public MinusNode() {
		super(AstOperator.MINUS);
	}

	/**
	 * Constructor with left and right child.
	 * 
	 * @param left  node of this
	 * @param right node of this
	 *
	 */
	public MinusNode(Node<?> left, Node<?> right) {
		this();
		setLeft(left);
		setRight(right);
	}
}
