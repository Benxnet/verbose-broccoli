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

package de.lab4inf.axela.script;

/**
 * Generic binary node abstraction for an abstract syntax tree (AST).
 * 
 * @author nwulff
 * @param <T> the payload type of this Node.
 * @since 26.10.2020
 */
public interface Node<T> extends Cloneable {

	/**
	 * Get the (unique) id of this node.
	 * 
	 * @return id of this node
	 */
	String getId();

	/**
	 * Get the payload value of this node.
	 * 
	 * @return value of this node
	 */
	T getPayload();

	/**
	 * Get the parent of this node.
	 * 
	 * @return parent of this node
	 */
	Node<?> getParent();

	/**
	 * Find the root Node of this node.
	 * 
	 * @return root node
	 */
	Node<?> getRoot();

	/**
	 * Set the parent of this node.
	 * 
	 * @param parent of this node
	 */
	void setParent(Node<?> parent);

	/**
	 * Check if the given child belongs to this node.
	 * 
	 * @param child to check
	 * @return boolean flag
	 */
	boolean isChild(Node<?> child);

	/**
	 * Accept a visitor via call of visitor.visit(this).
	 * 
	 * @param <R>     the type the visitor returns
	 * @param visitor for this node
	 * @return the return value of the visitor
	 */
	<R> R accept(NodeVisitor<R> visitor);

	/**
	 * Indicate if this is a root node without parent.
	 * 
	 * @return boolean flag
	 */
	default boolean isRoot() {
		return null == getParent();
	}

	/**
	 * Get the left child of this node.
	 * 
	 * @return left child
	 */
	Node<?> getLeft();

	/**
	 * Get the left child of this node.
	 * 
	 * @return left child
	 */
	Node<?> getRight();

	/**
	 * Set the left child of this node.
	 * 
	 * @param node to set left
	 */
	void setLeft(Node<?> node);

	/**
	 * Set the left child of this node.
	 * 
	 * @param node to set left
	 */
	void setRight(Node<?> node);

}
