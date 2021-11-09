package de.lab4inf.axela.engine;

import de.lab4inf.axela.core.Axela;
import de.lab4inf.axela.core.Iris;
import de.lab4inf.axela.facts.FactBase;

import static java.lang.String.format;

import java.util.HashMap;
import java.util.Objects;

public class AxelaEngine implements Axela {

	public class Key {
		private final Object problem;
		private final Object factclass;

		public <P, F> Key(P problem, F facts) {
			Objects.requireNonNull(problem, PROBLEM_IS_A_NULL_POINTER);
			Objects.requireNonNull(facts, FACTS_ARE_A_NULL_POINTER);
			this.problem = problem;
			if (facts instanceof FactBase<?, ?>)
				this.factclass = ((FactBase<?, ?>) facts).getSignature();
			else
				this.factclass = facts.getClass();
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.problem, this.factclass);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (this.getClass() != obj.getClass())
				return false;
			Key other = (Key) obj;
			return this.problem.equals(other.problem) && this.factclass.equals(other.factclass);
		}
	}

	private static HashMap<Key, Iris<?, ?, ?>> registeredIrises = new HashMap<>();

	// Method to register given solver to HashMap
	@Override
	public <P, F, S> void registerSolver(P problem, F facts, Iris<P, F, S> solver) {
		Objects.requireNonNull(problem, PROBLEM_IS_A_NULL_POINTER);
		Objects.requireNonNull(facts, FACTS_ARE_A_NULL_POINTER);
		Objects.requireNonNull(solver, IRIS_IS_A_NULL_POINTER);

		Key hashKey = new Key(problem, facts);
		if (!hasSolverFor(problem, facts))
			registeredIrises.put(hashKey, solver);
		else
			throw new IllegalStateException("iris already registered");
	}

	// Check if solver exists for given problems&facts
	@Override
	public <P, F, S> boolean hasSolverFor(P problem, F facts) {
		Objects.requireNonNull(problem, PROBLEM_IS_A_NULL_POINTER);
		Objects.requireNonNull(facts, FACTS_ARE_A_NULL_POINTER);

		Key hashKey = new Key(problem, facts);
		boolean re = registeredIrises.containsKey(hashKey);
		return re;
	}

	// returns iris solver for given problem&facts
	@SuppressWarnings("unchecked")
	@Override
	public <P, F, S> Iris<P, F, S> findSolverFor(P problem, F facts) {
		Objects.requireNonNull(problem, PROBLEM_IS_A_NULL_POINTER);
		Objects.requireNonNull(facts, FACTS_ARE_A_NULL_POINTER);

		Key hashKey = new Key(problem, facts);

		if (hasSolverFor(problem, facts))
			return (Iris<P, F, S>) registeredIrises.get(hashKey);
		else
			throw new IllegalStateException(format(NO_IRIS_FOR_PROBLEM_FOUND, problem));
	}
}
