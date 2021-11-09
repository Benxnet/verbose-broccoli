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

import java.util.Objects;

import de.lab4inf.axela.math.ast.*;

/**
 * Interface for a Node visitor pattern.
 * 
 * @author nwulff
 * @param <T> generic type of this Visitor return
 * @since 26.10.2020
 */
public interface NodeVisitor<T> {

	/**
	 * Visit a node.
	 * 
	 * @param vec to visit
	 * @return result of type T
	 */
	T visit(VectorNode vec);

	/**
	 * Visit a node.
	 * 
	 * @param mat to visit s * @return result of type T
	 */
	T visit(MatrixNode mat);

	/**
	 * Visit a node.
	 * 
	 * @param fct to visit
	 * @return result of type T
	 */
	T visit(FunctionNode fct);

	/**
	 * Visit a node.
	 * 
	 * @param n numeric value to visit
	 * @return result of type T
	 */
	T visit(FunctionCall fn);

	/**
	 * Visit a node.
	 * 
	 * @param op operator node to visit
	 * @return result of type T
	 */
	T visit(MathOperatorNode op);

	/**
	 * Visit a node.
	 * 
	 * @param sn symbolic constant or variable to visit
	 * @return result of type T
	 */
	T visit(SymbolNode sn);

	/**
	 * Visit a node.
	 * 
	 * @param n numeric value to visit
	 * @return result of type T
	 */
	T visit(NumericNode<?> n);

	class ChainVisitor<T> implements NodeVisitor<T> {
		NodeVisitor<Node<?>> first;
		NodeVisitor<T> second;

		ChainVisitor(NodeVisitor<Node<?>> first, NodeVisitor<T> second) {
			this.first = Objects.requireNonNull(first);
			this.second = Objects.requireNonNull(second);
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see de.lab4inf.axela.script.NodeVisitor#visit(de.lab4inf.axela.math.ast.FunctionNode)
		 */
		@Override
		public T visit(FunctionNode fct) {
			return first.visit(fct).accept(second);
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see de.lab4inf.axela.script.NodeVisitor#visit(de.lab4inf.axela.math.ast.FunctionCall)
		 */
		@Override
		public T visit(FunctionCall fct) {
			return first.visit(fct).accept(second);
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see de.lab4inf.axela.script.NodeVisitor#visit(de.lab4inf.axela.math.ast.MathOperatorNode)
		 */
		@Override
		public T visit(MathOperatorNode op) {
			return first.visit(op).accept(second);
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see de.lab4inf.axela.script.NodeVisitor#visit(de.lab4inf.axela.math.ast.SymbolNode)
		 */
		@Override
		public T visit(SymbolNode sn) {
			Objects.requireNonNull(sn);
			Node<?> n = sn.accept(first);
			Objects.requireNonNull(n);
			return n.accept(second);
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see de.lab4inf.axela.script.NodeVisitor#visit(de.lab4inf.axela.math.ast.NumericNode)
		 */
		@Override
		public T visit(NumericNode<?> n) {
			return first.visit(n).accept(second);
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see de.lab4inf.axela.script.NodeVisitor#visit(de.lab4inf.axela.math.ast.VectorNode)
		 */
		@Override
		public T visit(VectorNode vec) {
			return first.visit(vec).accept(second);
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see de.lab4inf.axela.script.NodeVisitor#visit(de.lab4inf.axela.math.ast.MatrixNode)
		 */
		@Override
		public T visit(MatrixNode mat) {
			return first.visit(mat).accept(second);
		}
	}
}
