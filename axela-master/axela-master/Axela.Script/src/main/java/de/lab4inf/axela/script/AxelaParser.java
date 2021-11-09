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

import static java.lang.String.format;

import java.util.*;
import java.util.regex.Pattern;

import de.lab4inf.axela.math.ast.*;

/**
 * Parser transforming statements into an abstract syntax tree.
 * 
 * EBNF grammar notation of this parser:
 * 
 * <pre>
 *    
 *     statements  :=  statement (';' statement)*
 *     statement   :=  definition | assignment | expr     
 *   
 *     definition  :=  ID '(' arglist ')' '=' expr   # function definition
 *     assignment  :=  ID '=' expr | vector | matrix # variable assignment
 *     
 *     vector      := '{' exprlist '}'               # vector definition
 *     matrix      := '{' vector (',' vector)* '}'   # matrix definition
 *     
 *     expr        :=  ID '(' exprlist ')'           # function call
 *                 | '(' expr ')'                    # expression within bracket
 *                 |  expr '^' expr                  # power operation
 *                 |  expr '*'|'/' expr              # multiplicative operation
 *                 |  expr '+'|'-' expr              # additive operation
 *                 |  ID                             # symbolic variable
 *                 |  NUMBER                         # numeric constant
 *
 *     arglist     := ID (','ID)*                    # formal argument list of function
 *     exprlist    := expr (','expr)*                # actual argument list of function
 * 
 * </pre>
 * 
 * @author nwulff
 * @since 30.10.2020
 */
public abstract class AxelaParser {
	protected static final Pattern symbols = Pattern.compile("^[a-zA-Z]+$");
	protected static final Pattern numbers = Pattern.compile("^-?[0-9]+$");
	protected static final Pattern doubles = Pattern.compile("^-?[0-9.]+$");
	protected static final LongNode ZERO = NumericNode.ZERO;
	protected static final LongNode ONE = NumericNode.ONE;
	protected static final LongNode MINUS_ONE = new LongNode(-1);

	static char[] operators;
	static String sOperators;
	static {
		operators = new char[] { '(', '=', '-', '+', '/', '*', '^', ')' };
		sOperators = new String(operators);
		// warn("operators: %s", sOperators);
	}

	/**
	 * Factory method to create a node.
	 * 
	 * @param x value as payload of the node
	 * @return Node for x
	 */
	protected static final LongNode node(long x) {
		if (0 == x)
			return ZERO;
		else if (1 == x)
			return ONE;
		else if (-1 == x)
			return MINUS_ONE;
		return new LongNode(x);
	}

	/**
	 * Factory method to create a node.
	 * 
	 * @param x value as payload of the node
	 * @return Node for x
	 */
	protected static final DoubleNode node(double x) {
		if (0.0 == x)
			return NumericNode.DZERO;
		else if (1 == x)
			return NumericNode.DONE;
		return new DoubleNode(x);
	}

	/**
	 * Factory method to create a node.
	 * 
	 * @param x value as payload of the node
	 * @return Node for x
	 */
	protected static final SymbolNode node(String x) {
		return new SymbolNode(x);
	}

	/**
	 * Short-form of the lazy for the factory method to create a node.
	 * 
	 * @param x value as payload of the node
	 * @return Node for x
	 */
	protected static final LongNode n(long x) {
		return node(x);
	}

	/**
	 * Short-form of the lazy for the factory method to create a node.
	 * 
	 * @param x value as payload of the node
	 * @return Node for x
	 */
	protected static final DoubleNode n(double x) {
		return node(x);
	}

	/**
	 * Short-form of the lazy for the factory method to create a node.
	 * 
	 * @param x value as payload of the node
	 * @return Node for x
	 */
	protected static final SymbolNode n(String x) {
		return node(x);
	}

	/**
	 * Extract the payload from the node. You must know the right type!
	 * 
	 * @param n the node
	 * @return value payload of the node
	 */
	protected static final long asLong(Node<?> n) {
		return LongNode.class.cast(n).getPayload();
	}

	/**
	 * Extract the payload from the node. You must know the right type!
	 * 
	 * @param n the node
	 * @return value payload of the node
	 */
	protected static final double asDouble(Node<?> n) {
		return DoubleNode.class.cast(n).getPayload();
	}

	/**
	 * Extract the payload from the node. You must know the right type!
	 * 
	 * @param n the node
	 * @return value payload of the node
	 */
	protected static final double asNumber(Node<?> n) {
		Number x = NumericNode.class.cast(n).getNumber();
		return x.doubleValue();
	}

	/**
	 * Return the negative of a node.
	 * 
	 * @param n the node
	 * @return -n
	 */
	protected static final Node<?> negative(Node<?> n) {
		Node<?> r = new TimesNode(MINUS_ONE, n);
		if (isInteger(n)) {
			LongNode i = LongNode.class.cast(n);
			r = node(-i.getPayload());
		} else if (isFloat(n)) {
			DoubleNode d = DoubleNode.class.cast(n);
			r = node(-d.getPayload());
		} else if (n instanceof TimesNode) {
			Node<?> lhs = n.getLeft();
			Node<?> rhs = n.getRight();
			if (MINUS_ONE.equals(lhs)) {
				r = rhs;
			} else if (ZERO.equals(lhs) || ZERO.equals(rhs)) {
				r = ZERO;
			}
		}
		return r;
	}

	/**
	 * Is the given string an operator?
	 * 
	 * @param s string to analyze
	 * @return true if an operator
	 */
	protected static boolean isOperator(String s) {
		return sOperators.contains(s);
	}

	/**
	 * Is the given string an integer number?
	 * 
	 * @param s string to analyze
	 * @return true if an integer number
	 */
	protected static boolean isInteger(String s) {
		String d = s;
		if (s.startsWith("+"))
			d = s.substring(1);

		return numbers.matcher(d).matches();
	}

	/**
	 * Is the given string a floating point number?
	 * 
	 * @param s string to analyze
	 * @return true if a float number
	 */
	protected static boolean isFloat(String s) {
		String d = s;
		if (s.startsWith("+"))
			d = s.substring(1);

		return doubles.matcher(d).matches();
	}

	/**
	 * Is the given string a symbol?
	 * 
	 * @param s string to analyze
	 * @return true if a symbol string
	 */
	protected static boolean isSymbol(String s) {
		return symbols.matcher(s).matches();
	}

	/**
	 * Is the given node an operator?
	 * 
	 * @param n node to analyze
	 * @return true if an operator
	 */
	protected static boolean isOperator(Node<?> n) {
		return isOperator(n.getId());
	}

	/**
	 * Presents the given node a numeric value?
	 * 
	 * @param n node to analyze
	 * @return true if a number
	 */
	protected static boolean isDigit(Node<?> n) {
		return n instanceof NumericNode;
	}

	/**
	 * Presents the given node an integer value?
	 * 
	 * @param n node to analyze
	 * @return true if a number
	 */
	protected static boolean isInteger(Node<?> n) {
		return n instanceof LongNode;
	}

	/**
	 * Presents the given node a floating point value?
	 * 
	 * @param n node to analyze
	 * @return true if a number
	 */
	protected static boolean isFloat(Node<?> n) {
		return n instanceof DoubleNode;
	}

	/**
	 * Presents the given node a symbolic variable?
	 * 
	 * @param n node to analyze
	 * @return true if a symbol
	 */
	protected static boolean isSymbol(Node<?> n) {
		return n instanceof SymbolNode;
	}

	/**
	 * Presents the given node a function variable?
	 * 
	 * @param n node to analyze
	 * @return true if a function
	 */
	protected static boolean isFunction(Node<?> n) {
		return n instanceof FunctionNode;
	}

	protected static void info(String msg) {
		System.out.println(msg);
	}

	protected static void info(String fmt, Object... args) {
		System.out.println(format(fmt, args));
	}

	protected static void warn(String msg) {
		System.err.println(msg);
	}

	protected static void warn(String fmt, Object... args) {
		warn(format(fmt, args));
	}

	protected static void error(String msg) {
		throw new IllegalStateException(msg);
	}

	protected static void error(String fmt, Object... args) {
		error(format(fmt, args));
	}

	protected final static String eatWhiteSpace(String s) {
		Objects.requireNonNull(s, "string is null");
		return s.replaceAll(" ", "");
	}

	/**
	 * parse the given definition and create an AST.
	 * 
	 * @param definition to parse
	 * @return the AST
	 */
	public List<Node<?>> parse(String definition) {
		return statements(eatWhiteSpace(definition));
	}

	/**
	 * Parse the colon separated statements.
	 * 
	 * <code>
	 *   statements = statement (; statements)*
	 * </code>
	 * 
	 * @param stm statements to parse
	 * @return node
	 */
	protected List<Node<?>> statements(String s) {
		String definition = eatWhiteSpace(s);
		ArrayList<Node<?>> statements = new ArrayList<>();
		StringTokenizer st = new StringTokenizer(definition, ";", false);
		while (st.hasMoreTokens()) {
			String statement = st.nextToken();
			Node<?> result = statement(statement);
			statements.add(result);
		}
		return statements;
	}

	/**
	 * Parse the statement as one of assignment, function definition or expression.
	 * 
	 * <code>
	 *   statement = assign | function definition | expression
	 * </code>
	 * 
	 * @param stm statement to parse
	 * @return node
	 */
	protected Node<?> statement(String statment) {
		String lhs;
		int q, p = statment.indexOf('=');
		if (p < 0) {
			return expression(statment);
		}
		lhs = statment.substring(0, p);
		p = lhs.indexOf('(');
		q = lhs.indexOf(')');
		if (0 < p && p <= q)
			return definition(statment);
		return assignment(statment);
	}

	/**
	 * Parse the statement as assignment definition.
	 * 
	 * <code>
	 *   id = expression
	 * </code>
	 * 
	 * @param stm statement to parse
	 * @return assignment node
	 */
	protected Node<?> assignment(String stm) {
		String lhs, rhs;
		int p = stm.indexOf('=');
		lhs = stm.substring(0, p);
		rhs = stm.substring(p + 1);
		AssignNode a = new AssignNode();
		SymbolNode sn = new SymbolNode(lhs);
		Node<?> rs = expression(rhs);
		a.setLeft(sn);
		a.setRight(rs);
		return a;
	}

	/**
	 * Parse the statement as function definition.
	 * 
	 * <code>
	 *   id(arglist) = expression
	 * </code>
	 * 
	 * @param stm statement to parse
	 * @return function node
	 */
	protected Node<?> definition(String stm) {
		String lhs, rhs;
		int q, p = stm.indexOf('=');
		lhs = stm.substring(0, p);
		rhs = stm.substring(p + 1);
		p = lhs.indexOf('(');
		q = lhs.indexOf(')');
		String id = lhs.substring(0, p);
		List<Node<?>> args = arglist(lhs.substring(p + 1, q));
		Node<?> expr = expression(rhs);
		info("def fct %s(%s)=%s", id, args, expr);
		FunctionNode fct = new FunctionNode(id, args, expr);

		return fct;
	}

	/**
	 * Parse the string of comma separated arguments into a list of nodes.
	 * 
	 * <code>
	 *   arglist = ID (,ID)*
	 * </code>
	 * 
	 * @param s string to parse
	 * @return List<Node> arguments
	 */
	protected List<Node<?>> arglist(String s) {
		ArrayList<Node<?>> args = new ArrayList<>();
		StringTokenizer st = new StringTokenizer(s, ",", false);
		while (st.hasMoreTokens()) {
			String arg = st.nextToken();
			Node<?> n = expression(arg);
			if (isSymbol(n)) {
				args.add(n);
			} else {
				error("not a symbol %s", n);
			}
		}
		return args;
	}

	/**
	 * Parse the string of comma separated arguments into a list of nodes.
	 * 
	 * <code>
	 *   exprlist = expr (,expr)*
	 * </code>
	 * 
	 * @param s string to parse
	 * @return List<Node> arguments
	 */
	protected List<Node<?>> exprlist(String s) {
		ArrayList<Node<?>> expressions = new ArrayList<>();
		StringTokenizer st = new StringTokenizer(s, ",", false);
		while (st.hasMoreTokens()) {
			String arg = st.nextToken();
			expressions.add(expression(arg));
		}
		return expressions;
	}

	/**
	 * The remaining part of the expression parsing has to be solved by some derived
	 * classes.
	 * 
	 * @param expr string with the expression to parse
	 * @return Node for the given expression string
	 */
	protected abstract Node<?> expression(String expr);

}
