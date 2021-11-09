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

import static java.lang.String.format;

import java.util.List;

import de.lab4inf.axela.script.Node;
import de.lab4inf.axela.script.NodeVisitor;

/**
 * Node for a function call.
 * 
 * 
 * @author nwulff
 * @since 20.11.2020
 */
public class FunctionCall extends BasicNode<List<Node<?>>> {

	/**
	 * @param symbol
	 * @param value
	 */
	public FunctionCall(String symbol, List<Node<?>> value) {
		super(symbol, value);
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
		List<Node<?>> args = getPayload();
		return format("%s(%s)", getId(), args);
	}

}
