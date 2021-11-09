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
import de.lab4inf.axela.script.NodeVisitor;

/**
 * Base class for numeric literals.
 * 
 * @author nwulff
 * @param <T> tpye of this numeric node
 * @since 26.10.2020
 */
public class NumericNode<T extends Number> extends BasicNode<T> {
	/** neutral element of addition zero. */
	public static final LongNode ZERO = new LongNode(0);
	/** neutral element of multiplication one. */
	public static final LongNode ONE = new LongNode(1);
	/** neutral element of addition zero as double. */
	public static final DoubleNode DZERO = new DoubleNode(0.0);
	/** neutral element of multiplication one as double. */
	public static final DoubleNode DONE = new DoubleNode(1.0);

	/**
	 * Node for Numbers like Double, Long, Integer etc...
	 * 
	 * @param value of this numeric
	 */
	public NumericNode(T value) {
		super(value.toString(), value);
	}

	/**
	 * @param parent of this node
	 * @param value  of this node
	 */
	public NumericNode(Node<?> parent, T value) {
		super(parent, value.toString(), value);
	}

	/**
	 * Return the number of this value.
	 * 
	 * @return Number
	 */
	public Number getNumber() {
		return Number.class.cast(super.getPayload());
	}

	/**
	 * Return this numeric value as double
	 * 
	 * @return double value
	 */
	public double getDouble() {
		return getNumber().doubleValue();
	}

	/**
	 * Return this numeric value as long
	 * 
	 * @return long value
	 */
	public long getLong() {
		return getNumber().longValue();
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

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see de.lab4inf.axela.math.ast.BasicNode#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		boolean isEqual = false;
		if (null == o)
			return false;
		if (this == o)
			return true;
		if (getClass() == o.getClass()) {
			NumericNode<?> that = NumericNode.class.cast(o);
			isEqual = this.getNumber().equals(that.getNumber());
		}
		return isEqual;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see de.lab4inf.axela.math.ast.BasicNode#hashCode()
	 */
	@Override
	public int hashCode() {
		return getPayload().hashCode();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see de.lab4inf.axela.math.ast.BasicNode#toString()
	 */
	@Override
	public String toString() {
		return getPayload().toString();
	}

}
