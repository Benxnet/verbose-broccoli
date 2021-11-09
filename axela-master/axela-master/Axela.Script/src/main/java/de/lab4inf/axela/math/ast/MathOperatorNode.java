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

import de.lab4inf.axela.script.*;

/**
 * Basic MathOperator Node
 * 
 * @author nwulff
 * @since 26.10.2020
 */
public abstract class MathOperatorNode extends BasicNode<AstOperator> implements OperatorNode {
	protected static final NumericNode<Long> ZERO = NumericNode.ZERO;
	protected static final NumericNode<Long> ONE = NumericNode.ONE;

	/**
	 * @param op of this node
	 */
	public MathOperatorNode(AstOperator op) {
		super(op.toString(), op);
	}

	/**
	 * @param parent of this node
	 * @param op     of this node
	 */
	public MathOperatorNode(Node<?> parent, AstOperator op) {
		super(parent, op.toString(), op);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see de.lab4inf.axela.script.Node#accept(de.lab4inf.axela.script.NodeVisitor)
	 */
	@Override
	public <R> R accept(NodeVisitor<R> visitor) {
		return visitor.visit(this);
	}
}
