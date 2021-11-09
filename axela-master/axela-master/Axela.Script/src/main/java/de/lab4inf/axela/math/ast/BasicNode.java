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

import static java.lang.String.format;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Objects;

import de.lab4inf.axela.script.Node;

/**
 * Basic implementation of the Node AST.
 * 
 * @author nwulff
 * @param <T> value type of this node
 * @since 26.10.2020
 */
public abstract class BasicNode<T> implements Node<T>, Comparable<BasicNode<?>> {
	protected static Logger logger = System.getLogger("Axela");
	private final String id;
	private T payload;
	private Node<?> parent;
	private Node<?> left, right;

	protected void log(String fmt, Object... args) {
		String msg = String.format(fmt, args);
		logger.log(Level.INFO, msg);
	}

	protected void warn(String fmt, Object... args) {
		String msg = String.format(fmt, args);
		logger.log(Level.WARNING, msg);
	}

	protected void error(String msg) {
		logger.log(Level.ERROR, msg);
		throw new IllegalStateException(msg);
	}

	protected void error(String fmt, Object... args) {
		String msg = String.format(fmt, args);
		error(msg);
	}

	BasicNode(String symbol, T value) {
		this.id = Objects.requireNonNull(symbol, "symbol is null");
		this.payload = Objects.requireNonNull(value, "value is null");
	}

	BasicNode(Node<?> parent, String symbol, T value) {
		this(symbol, value);
		setParent(parent);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */

	@Override
	public int compareTo(BasicNode<?> o) {
		return id.compareTo(o.id);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		boolean isEqual = false;
		if (null == o)
			return false;
		if (this == o)
			return true;
		if (getClass() == o.getClass()) {
			BasicNode<?> that = BasicNode.class.cast(o);
			isEqual = this.id.equals(that.id);
			if (isEqual) {
				if (null != left)
					isEqual = left.equals(that.left);
				if (null != right)
					isEqual &= right.equals(that.right);
			}
		}
		return isEqual;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return id.hashCode();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return format("%s(%s,%s)", id, getLeft(), getRight());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see de.lab4inf.axela.script.Node#getId()
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see de.lab4inf.axela.script.Node#getPayload()
	 */
	@Override
	public T getPayload() {
		return payload;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see de.lab4inf.axela.script.Node#getParent()
	 */
	@Override
	public Node<?> getParent() {
		return parent;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see de.lab4inf.axela.script.Node#getRoot()
	 */
	@Override
	public Node<?> getRoot() {
		Node<?> root, tmp = this;
		do {
			root = tmp;
			tmp = root.getParent();
		} while (tmp != null);
		return root;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see de.lab4inf.axela.script.Node#isChild(de.lab4inf.axela.script.Node)
	 */
	@Override
	public boolean isChild(Node<?> child) {
		return left.equals(child) || right.equals(child);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see de.lab4inf.axela.script.Node#setParent(de.lab4inf.axela.script.Node)
	 */
	@Override
	public void setParent(Node<?> parent) {
		Objects.requireNonNull(parent, "parent is null");
		this.parent = parent;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see de.lab4inf.axela.script.Node#getLeft()
	 */
	@Override
	public Node<?> getLeft() {
		return left;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see de.lab4inf.axela.script.Node#getRight()
	 */
	@Override
	public Node<?> getRight() {
		return right;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see de.lab4inf.axela.script.Node#setLeft(de.lab4inf.axela.script.Node)
	 */
	@Override
	public void setLeft(Node<?> node) {
		if (node.getParent() != this) {
			node.setParent(this);
		}
		left = node;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see de.lab4inf.axela.script.Node#setRight(de.lab4inf.axela.script.Node)
	 */
	@Override
	public void setRight(Node<?> node) {
		if (node.getParent() != this) {
			node.setParent(this);
		}
		right = node;
	}
}
