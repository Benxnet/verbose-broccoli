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

import java.util.List;
import java.util.Objects;

import de.lab4inf.axela.script.Node;
import de.lab4inf.axela.script.NodeVisitor;

/**
 * Node for parsed function definitions.
 * 
 * 
 * @author nwulff
 * @since 31.10.2020
 */
public class FunctionNode extends BasicNode<Node<?>> {
	List<Node<?>> args;

	/**
	 * Constructor with name and body as AST
	 * 
	 * @param name of the function
	 * @param args formal arguments of the function
	 * @param expr body as AST
	 */
	public FunctionNode(String name, List<Node<?>> args, Node<?> expr) {
		super(name, expr);
		this.args = Objects.requireNonNull(args);
		if (args.size() == 0)
			error("args null");
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

	@Override
	public String toString() {
		return format("%s(%s)=%s", getId(), args, getPayload());
	}

	/**
	 * Get the arguments.
	 * 
	 * @return the arguments
	 */
	public List<Node<?>> getArgs() {
		return args;
	}
}
