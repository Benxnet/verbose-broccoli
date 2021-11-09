package de.lab4inf.axela.script;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.*;

import de.lab4inf.axela.math.ast.*;

public class ValueVisitorTest {
	protected static ValueVisitor visitor;
	protected double tolerance = 5.E-12;
	protected Random rd = new Random();

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	protected static void setUpBeforeClass() throws Exception {
		visitor = new ValueVisitor();
	}

	@Test
	protected void visitAdd() throws Exception {
		double x = 1.3, y = 0.5;
		PlusNode node = new PlusNode(visitor.node(x), visitor.node(y));
		double expected = x + y;
		double returned = visitor.visit(node);
		assertEquals(1.8, returned, tolerance);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	protected void visitMinus() throws Exception {
		double x = 1.3, y = 0.5;
		MinusNode node = new MinusNode(visitor.node(x), visitor.node(y));
		double expected = x - y;
		double returned = visitor.visit(node);
		assertEquals(0.8, returned, tolerance);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	protected void visitTimes() throws Exception {
		double x = 2.3, y = 1.75;
		TimesNode node = new TimesNode(visitor.node(x), visitor.node(y));
		double expected = x * y;
		double returned = visitor.visit(node);
		assertEquals(4.025, returned, tolerance);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	protected void visitDivide() throws Exception {
		double x = 15.3, y = 3.;
		DivideNode node = new DivideNode(visitor.node(x), visitor.node(y));
		double expected = x / y;
		double returned = visitor.visit(node);
		assertEquals(5.1, returned, tolerance);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	protected void visitPower() throws Exception {
		double x = 1.3, y = 5.;
		PowerNode node = new PowerNode(visitor.node(x), visitor.node(y));
		double expected = Math.pow(x, y);
		double returned = visitor.visit(node);
		assertEquals(3.71293, returned, tolerance);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	protected void visitRndAdd() throws Exception {
		double x = rd.nextDouble(), y = rd.nextDouble();
		PlusNode node = new PlusNode(visitor.node(x), visitor.node(y));
		double expected = x + y;
		double returned = visitor.visit(node);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	protected void visitRndMinus() throws Exception {
		double x = rd.nextDouble(), y = rd.nextDouble();
		MinusNode node = new MinusNode(visitor.node(x), visitor.node(y));
		double expected = x - y;
		double returned = visitor.visit(node);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	protected void visitRndTimes() throws Exception {
		double x = rd.nextDouble(), y = rd.nextDouble();
		TimesNode node = new TimesNode(visitor.node(x), visitor.node(y));
		double expected = x * y;
		double returned = visitor.visit(node);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	protected void visitRndDivide() throws Exception {
		double x = rd.nextDouble(), y = rd.nextDouble();
		DivideNode node = new DivideNode(visitor.node(x), visitor.node(y));
		double expected = x / y;
		double returned = visitor.visit(node);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	protected void visitRndPower() throws Exception {
		double x = rd.nextDouble(), y = rd.nextDouble();
		PowerNode node = new PowerNode(visitor.node(x), visitor.node(y));
		double expected = Math.pow(x, y);
		double returned = visitor.visit(node);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	protected void visitMany() throws Exception {
		double x = 1.5, y = 8.32, z = 2.5, a = 1.5, b = 4., c = 8.;
		PowerNode node = new PowerNode(new PlusNode(visitor.node(x), new MinusNode(visitor.node(y), visitor.node(z))),
				new TimesNode(new DivideNode(visitor.node(a), visitor.node(b)), visitor.node(c)));
		double expected = Math.pow((x + (y - z)), ((a / b) * c));
		double returned = visitor.visit(node);
		assertEquals(392.223168, returned, tolerance);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	protected void visitRndMany() throws Exception {
		double x = rd.nextDouble(), y = rd.nextDouble(), z = rd.nextDouble(), a = rd.nextDouble(), b = rd.nextDouble(),
				c = rd.nextDouble();
		PowerNode node = new PowerNode(new PlusNode(visitor.node(x), new MinusNode(visitor.node(y), visitor.node(z))),
				new TimesNode(new DivideNode(visitor.node(a), visitor.node(b)), visitor.node(c)));
		double expected = Math.pow((x + (y - z)), ((a / b) * c));
		double returned = visitor.visit(node);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	protected void visitNullUnknown() throws Exception {
		double x = 1.3, y = 5;
		NullNode node = new NullNode(visitor.node(x), visitor.node(y));
		try {
			visitor.visit(node);
			fail("no exception thrown");
		} catch (IllegalArgumentException error) {
			String msg = error.getMessage();
			assertNotNull(msg, "empty exception is meaningless");
			assertTrue(error.getMessage().contains("unknown"));
		}
	}

	@Test
	protected void visitDivideByZero() throws Exception {
		double x = 1.3, y = 0;
		DivideNode node = new DivideNode(visitor.node(x), visitor.node(y));
		try {
			visitor.visit(node);
			fail("no exception thrown");
		} catch (IllegalArgumentException error) {
			String msg = error.getMessage();
			assertNotNull(msg, "empty exception is meaningless");
			assertTrue(error.getMessage().contains("divide"));
		}
	}

	@Test
	protected void visitNum() throws Exception {
		double x = 1.3;
		DoubleNode node = visitor.node(x);
		double expected = x;
		double returned = visitor.visit(node);
		assertEquals(1.3, returned, tolerance);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	protected void visitRndNum() throws Exception {
		double x = rd.nextDouble();
		DoubleNode node = visitor.node(x);
		double expected = x;
		double returned = visitor.visit(node);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	protected void visitAssign() throws Exception {
		double x = 0.8, y = 1.3;
		AssignNode node = new AssignNode(new SymbolNode("z"),
				new PlusNode(new AssignNode(new SymbolNode("x"), visitor.node(x)),
						new AssignNode(new SymbolNode("y"), visitor.node(y))));
		double expected = x + y;
		double returned = visitor.visit(node);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	protected void visitRndAssign() throws Exception {
		double x = rd.nextDouble(), y = rd.nextDouble();
		AssignNode node = new AssignNode(new SymbolNode("z"),
				new PlusNode(new AssignNode(new SymbolNode("x"), visitor.node(x)),
						new AssignNode(new SymbolNode("y"), visitor.node(y))));
		double expected = x + y;
		double returned = visitor.visit(node);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	protected void visitFunctionNode() throws Exception {
		try {
			List<Node<?>> args = new ArrayList<Node<?>>();
			args.add(new SymbolNode("x"));
			FunctionNode node = new FunctionNode("f", args, new PlusNode(new SymbolNode("x"), new SymbolNode("x")));
			visitor.visit(node);
		} catch (Exception error) {
			fail("Exception thrown");
		}
	}

	@Test
	protected void visitFunctionCall() throws Exception {
		// Save function
		SymbolNode symX = new SymbolNode("x");
		List<Node<?>> args = new ArrayList<Node<?>>();
		args.add(symX);
		FunctionNode node = new FunctionNode("f", args, new PlusNode(symX, symX));
		visitor.visit(node);

		// execute function
		double x = 5;
		List<Node<?>> vals = new ArrayList<Node<?>>();
		vals.add(new AssignNode(symX, new DoubleNode(x)));
		FunctionCall call = new FunctionCall("f", vals);
		double expected = x + x;
		double returned = visitor.visit(call);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	protected void visitRndFunctionCall() throws Exception {
		// Save function
		SymbolNode symX = new SymbolNode("x");
		List<Node<?>> args = new ArrayList<Node<?>>();
		args.add(symX);
		FunctionNode node = new FunctionNode("f", args, new PlusNode(symX, symX));
		visitor.visit(node);

		// execute function
		double x = rd.nextDouble();
		List<Node<?>> vals = new ArrayList<Node<?>>();
		vals.add(new AssignNode(symX, new DoubleNode(x)));
		FunctionCall call = new FunctionCall("f", vals);
		double expected = x + x;
		double returned = visitor.visit(call);
		assertEquals(expected, returned, tolerance);
	}

	@Test
	protected void visitFunctionCallUnknownFunction() throws Exception {
		SymbolNode symX = new SymbolNode("x");
		double x = 5;
		List<Node<?>> vals = new ArrayList<Node<?>>();
		vals.add(new AssignNode(symX, new DoubleNode(x)));
		FunctionCall call = new FunctionCall("unknown", vals);
		try {
			visitor.visit(call);
		} catch (IllegalStateException error) {
			String msg = error.getMessage();
			assertNotNull(msg, "empty exception is meaningless");
			assertTrue(error.getMessage().contains("unknown"));
		}
	}

	protected class NullNode extends MathOperatorNode {
		/**
		 * Pojo constructor.
		 */
		public NullNode() {
			super(AstOperator.NULL);
		}

		/**
		 * Constructor with left and right child.
		 * 
		 * @param left  node of this
		 * @param right node of this
		 *
		 */
		public NullNode(Node<?> left, Node<?> right) {
			this();
			Node<?> l = left;
			Node<?> r = right;
			if (r instanceof NumericNode<?>) {
				setLeft(r);
				setRight(l);
			} else {
				setLeft(l);
				setRight(r);
			}
		}
	}

}
