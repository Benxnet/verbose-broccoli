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

import java.util.HashMap;
import java.util.List;

import de.lab4inf.axela.math.ast.*;

/**
 * Extract double values form a node (hierarchy).
 * 
 * 
 * @author nwulff
 * @since 17.11.2020
 */
public class ValueVisitor extends BaseVisitor<Double> {
	HashMap<Node<?>, Node<?>> vars = new HashMap<>();
	FunctionVisitor funcVisit = new FunctionVisitor();

	@Override
	public Double visit(NumericNode<?> nn) {
		Number n = nn.getPayload();
		Double d = n.doubleValue();
		log("number node %s ", nn);
		return d;
	}

	@Override
	public Double visit(SymbolNode sn) {
		if (vars.containsKey(sn))
			return vars.get(sn).accept(this);
		return 0.;
	}

	@Override
	public Double visit(FunctionNode fn) {
		funcVisit.visit(fn);
		return 0.;
	}

	@Override
	public Double visit(FunctionCall fc) {
		double ret = 0.0;

		AxelaFunction func = funcVisit.visit(fc);
		if (func != null) {
			List<Node<?>> args = fc.getPayload();
			double[] argsDouble = new double[args.size()];
			for (int i = 0; i < argsDouble.length; i++) {
				argsDouble[i] = args.get(i).accept(this);
			}
			ret = func.apply(argsDouble);
		}

		return ret;
	}

	@Override
	public Double visit(MathOperatorNode opNode) {
		log("operator node %s ", opNode);
		Node<?> lhs = opNode.getLeft();
		Node<?> rhs = opNode.getRight();
		double l = lhs.accept(this);
		double r = rhs.accept(this);
		double ret = 0.0;

		switch (opNode.getPayload()) {
		case PLUS:
			ret = l + r;
			break;
		case MINUS:
			ret = l - r;
			break;
		case TIMES:
			ret = l * r;
			break;
		case DIVIDE:
			if (r == 0)
				throw new IllegalArgumentException("can't divide by zero");
			ret = l / r;
			break;
		case POWER:
			ret = Math.pow(l, r);
			break;
		case ASSIGN:
			vars.put(lhs, rhs);
			ret = r;
			break;
		default:
			throw new IllegalArgumentException(format("unknown op %s", opNode.getPayload()));
		}
		log("operator node %s returns %s", opNode, ret);
		return ret;
	}
}
