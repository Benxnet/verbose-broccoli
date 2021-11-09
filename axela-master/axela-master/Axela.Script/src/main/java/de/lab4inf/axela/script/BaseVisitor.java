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

package de.lab4inf.axela.script;

import static java.lang.String.format;

import de.lab4inf.axela.math.ast.*;

/**
 * Base class for an Axela node visitor with default implementations of the
 * NodeVisitor interface.
 * 
 * 
 * @author nwulff
 * @param <T> type of the visitors payload
 * @since 31.10.2020
 */
public abstract class BaseVisitor<T> implements NodeVisitor<T> {
	protected static boolean loggingEnabled = true;

	/**
	 * Factory method to create a Node.
	 * 
	 * @param x value to take
	 * @return suitable Node for x
	 */
	protected LongNode node(long x) {
		return new LongNode(x);
	}

	/**
	 * Factory method to create a Node.
	 * 
	 * @param x value to take
	 * @return suitable Node for x
	 */
	protected DoubleNode node(double x) {
		return new DoubleNode(x);
	}

	/**
	 * Utility method to extract the payload of a node.
	 * 
	 * @param n node of the payload
	 * @return payload
	 */
	protected long asLong(Node<?> n) {
		return LongNode.class.cast(n).getPayload();
	}

	/**
	 * Utility method to extract the payload of a node.
	 * 
	 * @param n node of the payload
	 * @return payload
	 */
	protected double asDouble(Node<?> n) {
		return DoubleNode.class.cast(n).getPayload();
	}

	/**
	 * Utility method to extract the payload of a node.
	 * 
	 * @param n node of the payload
	 * @return payload
	 */
	protected double asNumber(Node<?> n) {
		Number x = NumericNode.class.cast(n).getNumber();
		return x.doubleValue();
	}

	/**
	 * Common logging method, this version only System.out
	 * 
	 * @param msg to print
	 */
	protected void log(String msg) {
		if (loggingEnabled)
			System.out.println(msg);
	}

	/**
	 * Common logging method.
	 * 
	 * @param fmt  format to use
	 * @param args varargs of Objects to print
	 */
	protected void log(String fmt, Object... args) {
		log(format(fmt, args));
	}

	/**
	 * Common logging warn method, this version only System.err
	 * 
	 * @param msg to print
	 */
	protected void warn(String msg) {
		System.err.println(msg);
	}

	/**
	 * Common logging warn method.
	 * 
	 * @param fmt  format to use
	 * @param args varargs of Objects to print
	 */
	protected void warn(String fmt, Object... args) {
		warn(format(fmt, args));
	}

	/**
	 * Common logging error method, throwing an exception.
	 * 
	 * @param fmt format to use
	 * @return T (which will never happen because of exception)
	 */
	protected T error(String msg) {
		warn(msg);
		throw new IllegalStateException(msg);
	}

	/**
	 * Common logging error method, throwing an exception.
	 * 
	 * @param <S>   type of the state
	 * @param fmt   format to use
	 * @param state to print
	 * @return T (which will never happen because of exception)
	 */
	protected <S> T error(String fmt, S state) {
		return error(format(fmt, state));
	}

	/**
	 * Calculate recursive the number of nodes in a binary tree.
	 * 
	 * @param op node to evaluate
	 * @return the summed up nodes
	 */
	public int numberOfNodes(Node<?> op) {
		if (null == op)
			return 0;
		int size = 1;
		size += numberOfNodes(op.getLeft());
		size += numberOfNodes(op.getRight());
		return size;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see de.lab4inf.axela.script.NodeVisitor#visit(de.lab4inf.axela.math.ast.FunctionCall)
	 */
	@Override
	public T visit(FunctionCall fct) {
		return error("not implemented for ", fct);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see de.lab4inf.axela.script.NodeVisitor#visit(de.lab4inf.axela.math.ast.FunctionNode)
	 */
	@Override
	public T visit(FunctionNode fct) {
		return error("not implemented for ", fct);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see de.lab4inf.axela.script.NodeVisitor#visit(de.lab4inf.axela.math.ast.MathOperatorNode)
	 */
	@Override
	public T visit(MathOperatorNode op) {
		return error("not implemented for ", op);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see de.lab4inf.axela.script.NodeVisitor#visit(de.lab4inf.axela.math.ast.SymbolNode)
	 */
	@Override
	public T visit(SymbolNode sn) {
		return error("not implemented for ", sn);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see de.lab4inf.axela.script.NodeVisitor#visit(de.lab4inf.axela.math.ast.NumericNode)
	 */
	@Override
	public T visit(NumericNode<?> n) {
		return error("not implemented for ", n);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see de.lab4inf.axela.script.NodeVisitor#visit(de.lab4inf.axela.math.ast.VectorNode)
	 */
	@Override
	public T visit(VectorNode vec) {
		return error("not implemented for ", vec);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see de.lab4inf.axela.script.NodeVisitor#visit(de.lab4inf.axela.math.ast.MatrixNode)
	 */
	@Override
	public T visit(MatrixNode mat) {
		return error("not implemented for ", mat);
	}

}
