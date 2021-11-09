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

import java.util.*;

import javax.script.*;

import de.lab4inf.axela.core.Axela;
import de.lab4inf.axela.core.Iris;
import de.lab4inf.axela.math.ast.FunctionCall;
import de.lab4inf.axela.math.ast.SymbolNode;
import de.lab4inf.axela.script.javacc.AxelaJavaccParser;

/**
 * Axela implementation of the ScriptEngine interface.
 * 
 * 
 * @author nwulff
 * @since 17.11.2020
 */
public class AxelaScriptEngine extends AbstractAxelaScriptEngine implements Axela.Plugin {
	final static String PARSE = "Parse";
	final static String SCRIPT = "Script";
	final static String FCT = "Function";
	final AxelaParser parser;
	final AxelaScriptEngine self;

	/**
	 * Constructor with a script factory.
	 * 
	 * @param factory   of this engine
	 * @param globalCtx global context of the factory
	 * 
	 */
	public AxelaScriptEngine(AxelaScriptEngineFactory factory, ScriptContext globalCtx) {
		super(factory, globalCtx);
		self = this;
		axela.register(this);
		// trying different parser frameworks
		// parser = new ShuntingYardParser();
		// parser = new AntlrParser();
		parser = new AxelaJavaccParser();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see de.lab4inf.axela.core.Axela.Plugin#init(de.lab4inf.axela.core.Axela)
	 */
	@Override
	public void init(Axela engine) {
		final String[] factSignature = new String[0];
		// register the basic solvers of the script engine here
		// Solver to create an AST list of nodes.
		Iris<String, String[], List<Node<?>>> astIris = (p, f) -> astIris(f);
		if (!engine.hasSolverFor(PARSE, factSignature)) {
			engine.registerSolver(PARSE, factSignature, astIris);
		}
		// Solver using a visitor to extract Double from the statements.
		Iris<String, String[], Double> valueIris = (p, f) -> useVisitor(new ValueVisitor(), f);
		if (!engine.hasSolverFor(SCRIPT, factSignature)) {
			engine.registerSolver(SCRIPT, factSignature, valueIris);
		}
		// Solver using a visitor to extract AxelaFunction from the statements.
		Iris<String, String[], AxelaFunction> functionIris = (p, f) -> functionIris(new FunctionVisitor(), f);
		if (!engine.hasSolverFor(FCT, factSignature)) {
			engine.registerSolver(FCT, factSignature, functionIris);
		}
	}

	@SuppressWarnings("unchecked")
	List<Node<?>> astIris(String[] facts) {
		List<Node<?>> nodes = Collections.emptyList();
		try {
			nodes = (List<Node<?>>) eval(asString(facts));
			Objects.requireNonNull(nodes, " parsed ast is null");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return nodes;
	}

	<T> T useVisitor(NodeVisitor<T> visitor, String[] facts) {
		List<Node<?>> nodes = astIris(facts);
		return useVisitor(visitor, nodes);
	}

	<T> T useVisitor(NodeVisitor<T> visitor, List<Node<?>> nodes) {
		T ret = null;
		Objects.requireNonNull(visitor, "no visitor");
		Objects.requireNonNull(nodes, "no nodes");
		for (Node<?> n : nodes) {
			Objects.requireNonNull(n, "nullpointer within nodes list");
			ret = useVisitor(visitor, n);
		}
		Objects.requireNonNull(ret);
		return ret;
	}

	<T> T useVisitor(NodeVisitor<T> visitor, Node<?> node) {
		Objects.requireNonNull(visitor, "no visitor");
		Objects.requireNonNull(node, "no node");
		T ret = node.accept(visitor);
		return ret;
	}

	AxelaFunction functionIris(FunctionVisitor funcVisit, FunctionCall fc) {
		Objects.requireNonNull(funcVisit, "no visitor");
		Objects.requireNonNull(fc, "no node");
		return funcVisit.visit(fc);
	}

	AxelaFunction functionIris(FunctionVisitor funcVisit, SymbolNode facts) {
		Objects.requireNonNull(funcVisit, "no visitor");
		Objects.requireNonNull(facts, "no facts");
		return functionIris(funcVisit, new FunctionCall(facts.getId(), new ArrayList<Node<?>>()));
	}

	AxelaFunction functionIris(FunctionVisitor funcVisit, String[] facts) {
		Objects.requireNonNull(funcVisit, "no visitor");
		Objects.requireNonNull(facts, "no facts");
		return functionIris(funcVisit, new FunctionCall(facts[0], new ArrayList<Node<?>>()));
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.script.ScriptEngine#eval(java.lang.String, javax.script.Bindings)
	 */
	@Override
	public Object eval(String script, Bindings n) throws ScriptException {
		// first prototype of a ScriptEngine returning AST.
		List<Node<?>> nodes = parser.parse(script);
		return nodes;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.script.ScriptEngine#eval(java.lang.String,
	 *      javax.script.ScriptContext)
	 */
	@Override
	public Object eval(String script, ScriptContext ctxt) throws ScriptException {
		return eval(script, ctxt.getBindings(ScriptContext.ENGINE_SCOPE));
	}

	/**
	 * Aggregate a facts array to a fact
	 * 
	 * @param facts to aggregate
	 * @return facts as string
	 */
	String asString(String[] facts) {
		StringBuilder sb = new StringBuilder();
		for (String script : facts) {
			sb.append(script);
			if (!script.endsWith(";"))
				sb.append(";");
		}
		String script = sb.toString();
		return script;
	}

}
