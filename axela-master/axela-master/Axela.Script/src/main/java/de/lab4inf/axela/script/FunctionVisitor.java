package de.lab4inf.axela.script;

import java.util.HashMap;

import de.lab4inf.axela.math.ast.*;

public class FunctionVisitor extends BaseVisitor<AxelaFunction> {
	protected static HashMap<String, AxelaFunction> functions = new HashMap<>();

	public FunctionVisitor() {
		super();
		functions.put("sin", x -> Math.sin(x[0]));
		functions.put("asin", x -> Math.asin(x[0]));
		functions.put("cos", x -> Math.cos(x[0]));
		functions.put("acos", x -> Math.acos(x[0]));
		functions.put("ln", x -> Math.log(x[0]));
		functions.put("log", x -> Math.log10(x[0]));
		functions.put("exp", x -> Math.exp(x[0]));
		functions.put("pow", x -> Math.pow(x[0], x[1]));
		functions.put("min", x -> Math.min(x[0], x[1]));
		functions.put("max", x -> Math.max(x[0], x[1]));
	}

	@Override
	public AxelaFunction visit(FunctionNode fn) {
		AxelaFunction func = x -> {
			if (x.length != fn.getArgs().size())
				throw new IllegalArgumentException("arguments don't match");
			ValueVisitor valVisit = new ValueVisitor();
			for (int i = 0; i < fn.getArgs().size(); i++) {
				valVisit.visit(new AssignNode(fn.getArgs().get(i), node(x[i])));
			}
			return fn.getPayload().accept(valVisit);
		};
		functions.put(fn.getId(), func);
		return func;
	}

	@Override
	public AxelaFunction visit(FunctionCall fc) {
		if (functions.containsKey(fc.getId()))
			return functions.get(fc.getId());
		throw new IllegalStateException("function unknown");
	}
}
