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

import java.util.*;

import de.lab4inf.axela.script.Node;

/**
 * Iterator for binary node trees
 * 
 * @author nwulff
 * @since 02.11.2020
 */
public class NodeIterator implements Iterator<Node<?>> {
	/**
	 * Enumeration of different iterator traversal orders.
	 */
	public static enum IterationOrder {
		/** iterate the binary tree in order. */
		INORDER,
		/** iterate the binary tree in pre-order. */
		PREORDER,
		/** iterate the binary tree in post-order. */
		POSTORDER,
		/** iterate the binary tree in level-order. */
		LEVELORDER;
	}

	private IteratorAlgorithm algo;

	/**
	 * Constructor with the root of the tree to start iteration.
	 * 
	 * @param root of the iterator
	 * 
	 */
	public NodeIterator(Node<?> root) {
		this(root, IterationOrder.INORDER);
	}

	/**
	 * Constructor with the root of the tree to start iteration.
	 * 
	 * @param root  of the iterator
	 * @param order of the iteration
	 * 
	 */
	public NodeIterator(Node<?> root, IterationOrder order) {
		Objects.requireNonNull(root, "root node is null");
		switch (order) {
		case INORDER:
			algo = new InOrder(root);
			break;
		case LEVELORDER:
			algo = new LevelOrder(root);
			break;
		case POSTORDER:
			algo = new PostOrder(root);
			break;
		case PREORDER:
			algo = new PreOrder(root);
			break;
		default:
			break;
		}
		Objects.requireNonNull(algo, "no valid iterator");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public final boolean hasNext() {
		return algo.hasNext();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#next()
	 */
	@Override
	public final Node<?> next() {
		return algo.next();
	}

	private abstract class IteratorAlgorithm implements Iterator<Node<?>> {
		protected List<Node<?>> lifo = new ArrayList<>();

		IteratorAlgorithm(Node<?> root) {
			order(root);
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext() {
			return !lifo.isEmpty();
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see java.util.Iterator#next()
		 */
		@Override
		public Node<?> next() {
			return lifo.remove(0);
		}

		/**
		 * internal ordering method of the algorithm
		 * 
		 * @param node to traverse
		 */
		abstract void order(Node<?> node);
	}

	private class InOrder extends IteratorAlgorithm {

		InOrder(Node<?> root) {
			super(root);
		}

		@Override
		void order(Node<?> node) {
			if (null == node)
				return;
			Node<?> left = node.getLeft();
			Node<?> right = node.getRight();
			order(left);
			lifo.add(node);
			order(right);
		}
	}

	private class PreOrder extends IteratorAlgorithm {
		PreOrder(Node<?> root) {
			super(root);
		}

		@Override
		void order(Node<?> node) {
			if (null == node)
				return;
			Node<?> left = node.getLeft();
			Node<?> right = node.getRight();
			lifo.add(node);
			order(left);
			order(right);
		}
	}

	private class PostOrder extends IteratorAlgorithm {
		PostOrder(Node<?> root) {
			super(root);
		}

		@Override
		void order(Node<?> node) {
			if (null == node)
				return;
			Node<?> left = node.getLeft();
			Node<?> right = node.getRight();
			order(left);
			order(right);
			lifo.add(node);
		}
	}

	private class LevelOrder extends IteratorAlgorithm {

		LevelOrder(Node<?> root) {
			super(root);
		}

		@Override
		void order(Node<?> node) {
			Node<?> next, left, right;
			if (null == node)
				return;
			List<Node<?>> tmp = new ArrayList<>();
			tmp.add(node);
			while (tmp.size() > 0) {
				next = tmp.remove(0);
				lifo.add(next);
				left = next.getLeft();
				right = next.getRight();
				if (null != left)
					tmp.add(left);
				if (null != right)
					tmp.add(right);
			}
		}
	}
}
