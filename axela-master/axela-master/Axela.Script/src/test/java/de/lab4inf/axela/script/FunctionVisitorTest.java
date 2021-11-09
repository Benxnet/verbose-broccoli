package de.lab4inf.axela.script;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.*;

import de.lab4inf.axela.math.ast.*;

public class FunctionVisitorTest {
	protected static FunctionVisitor visitor;
	protected double tolerance = 5.E-12;
	protected Random rd = new Random();

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	protected static void setUpBeforeClass() throws Exception {
		visitor = new FunctionVisitor();
	}

	@Test
	protected void visitFunctionNode() throws Exception { // test PlusNode integration
		double x = rd.nextDouble();
		SymbolNode symX = new SymbolNode("x");
		List<Node<?>> args = new ArrayList<Node<?>>();
		args.add(symX);
		FunctionNode node = new FunctionNode("f", args, new PlusNode(symX, symX));
		AxelaFunction returned = visitor.visit(node);
		double expected = x + x;
		assertNotNull(returned);
		assertEquals(expected, returned.apply(x), tolerance);
	}

	@Test
	protected void visitFunctionCall() throws Exception {
		SymbolNode symX = new SymbolNode("x");
		List<Node<?>> args = new ArrayList<Node<?>>();
		args.add(symX);
		FunctionNode node = new FunctionNode("h", args, new PlusNode(symX, symX));
		AxelaFunction expected = visitor.visit(node);

		double x = rd.nextDouble();
		AxelaFunction returned = visitor.visit(new FunctionCall("h", new ArrayList<Node<?>>()));
		assertEquals(expected.apply(x), returned.apply(x), tolerance);
		assertEquals(x + x, returned.apply(x), tolerance);
	}

	@Test
	protected void visitFunctionNodeOverwrite() throws Exception {
		SymbolNode symX = new SymbolNode("x");
		List<Node<?>> args = new ArrayList<Node<?>>();
		args.add(symX);
		FunctionNode node = new FunctionNode("z", args, new PlusNode(symX, symX));
		AxelaFunction expected = visitor.visit(node);

		double x = rd.nextDouble();
		FunctionCall call = new FunctionCall("z", new ArrayList<Node<?>>());
		AxelaFunction returned = visitor.visit(call);
		assertEquals(expected.apply(x), returned.apply(x), tolerance);
		assertEquals(x + x, returned.apply(x), tolerance);

		// overwrite
		FunctionNode node2 = new FunctionNode("z", args, new MinusNode(symX, symX));
		AxelaFunction expected2 = visitor.visit(node2);
		AxelaFunction returned2 = visitor.visit(call);
		assertEquals(expected2.apply(x), returned2.apply(x), tolerance);
		assertEquals(x - x, returned2.apply(x), tolerance);
	}

	@Test
	protected void visitSymbolNodeMultipleArgs() throws Exception {
		SymbolNode symX = new SymbolNode("x"), symY = new SymbolNode("y");
		List<Node<?>> args = new ArrayList<Node<?>>();
		args.add(symX);
		args.add(symY);
		FunctionNode node = new FunctionNode("j", args, new PlusNode(symX, symY));
		AxelaFunction expected = visitor.visit(node);

		double x = rd.nextDouble(), y = rd.nextDouble();
		AxelaFunction returned = visitor.visit(new FunctionCall("j", new ArrayList<Node<?>>()));
		assertEquals(expected.apply(x, y), returned.apply(x, y), tolerance);
		assertEquals(x + y, returned.apply(x, y), tolerance);
	}

	@Test
	protected void visitSymbolNodeUnequalArgs() throws Exception {
		SymbolNode symX = new SymbolNode("x"), symY = new SymbolNode("y");
		List<Node<?>> args = new ArrayList<Node<?>>();
		args.add(symX);
		args.add(symY);
		FunctionNode node = new FunctionNode("j", args, new PlusNode(symX, symY));
		AxelaFunction func = visitor.visit(node);
		try {
			func.apply(5.);
			fail("no exception thrown");
		} catch (IllegalArgumentException error) {
			String msg = error.getMessage();
			assertNotNull(msg, "empty exception is meaningless");
			assertTrue(error.getMessage().contains("arguments"));
		}
	}

	@Test
	protected void visitNullFunction() throws Exception {
		try {
			visitor.visit(new FunctionCall("abc", new ArrayList<Node<?>>()));
		} catch (IllegalStateException error) {
			String msg = error.getMessage();
			assertNotNull(msg, "empty exception is meaningless");
			assertTrue(error.getMessage().contains("unknown"));
		}
	}

	@Test
	protected void visitMathfunctions() throws Exception {
		double x = rd.nextDouble(), y = rd.nextDouble();
		List<Node<?>> vals = new ArrayList<Node<?>>();

		assertEquals(Math.sin(x), visitor.visit(new FunctionCall("sin", vals)).apply(x), tolerance);
		assertEquals(Math.asin(x), visitor.visit(new FunctionCall("asin", vals)).apply(x), tolerance);
		assertEquals(Math.cos(x), visitor.visit(new FunctionCall("cos", vals)).apply(x), tolerance);
		assertEquals(Math.acos(x), visitor.visit(new FunctionCall("acos", vals)).apply(x), tolerance);
		assertEquals(Math.log(x), visitor.visit(new FunctionCall("ln", vals)).apply(x), tolerance);
		assertEquals(Math.log10(x), visitor.visit(new FunctionCall("log", vals)).apply(x), tolerance);
		assertEquals(Math.exp(x), visitor.visit(new FunctionCall("exp", vals)).apply(x), tolerance);
		assertEquals(Math.pow(x, y), visitor.visit(new FunctionCall("pow", vals)).apply(x, y), tolerance);
		assertEquals(Math.min(x, y), visitor.visit(new FunctionCall("min", vals)).apply(x, y), tolerance);
		assertEquals(Math.max(x, y), visitor.visit(new FunctionCall("max", vals)).apply(x, y), tolerance);
	}

}
