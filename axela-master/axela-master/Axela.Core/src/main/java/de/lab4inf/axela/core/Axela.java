/*
 * Project: Axela.Core
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

package de.lab4inf.axela.core;

import static java.lang.String.format;

import java.util.Objects;
import java.util.Optional;
import java.util.ServiceLoader;

/**
 * An eXtendable Expression Language Abstraction (AXELA).<br>
 * 
 * Axela solves problems with help of the registered Iris solver instances.
 * 
 * @see de.lab4inf.axela.core.Iris
 * @author nwulff
 * @since 29.09.2020
 */
public interface Axela {
	String FACTS_ARE_A_NULL_POINTER = "facts are a NullPointer!";
	String PROBLEM_IS_A_NULL_POINTER = "problem is a NullPointer!";
	String IRIS_IS_A_NULL_POINTER = "iris is a NullPointer!";
	String SOLUTION_IS_A_NULL_POINTER = "solution is a NullPointer!";
	String NO_IRIS_FOR_PROBLEM_FOUND = "no Iris for problem: %s found";
	String NO_AXELA_FOUND = "no valid Axela implementation found";

	/**
	 * Utility method using the reflection API to look-up an Axela implementation
	 * via the ServiceLoder mechanism. <br>
	 * Note: This static method uses a JDK1.8 feature: a static implementation
	 * within an interface and uses the Reflection API with a JDK1.6 ServiceLoader
	 * implementation - more in the lectures.
	 * 
	 * @return Axela instance found by reflection
	 */
	static Axela getEngine() {
		ServiceLoader<Axela> loader = ServiceLoader.load(Axela.class);
		Optional<Axela> maybe = loader.findFirst();
		if (maybe.isPresent()) {
			Axela engine = maybe.get();
			return engine;
		}
		throw new IllegalStateException(NO_AXELA_FOUND);
		// all this can be coded in one (more or less unreadable) line - I don't like
		// this style of programming...
		// return ServiceLoader.load(Axela.class).findFirst().orElseThrow(()->new
		// IllegalStateException(NO_AXELA_FOUND));
	}

	/**
	 * Solve the problem with help of the given fact(s), using a registered Iris
	 * solver implementation. <br>
	 * Note: default method is a JDK1.8 feature: an implementation within an
	 * interface - more to come in the lectures. This method can be overwritten in
	 * own classes and if not serves as a simple basic template implementation.
	 * 
	 * @param problem to solve
	 * @param facts   to use
	 * @return solution of the problem
	 * @param <Problem>  generic problem type to solve
	 * @param <Facts>    generic fact type
	 * @param <Solution> generic solution type to return
	 */
	default <Problem, Facts, Solution> Solution solve(final Problem problem, final Facts facts) {
		// I don't like NullPointers, this JDK1.7 Utility method is very helpful against
		// NP ;-)
		Objects.requireNonNull(problem, PROBLEM_IS_A_NULL_POINTER);
		Objects.requireNonNull(facts, FACTS_ARE_A_NULL_POINTER);
		if (hasSolverFor(problem, facts)) {
			Iris<Problem, Facts, Solution> solver = findSolverFor(problem, facts);
			Solution solution = solver.solve(problem, facts);
			return Objects.requireNonNull(solution, SOLUTION_IS_A_NULL_POINTER);
		}
		throw new IllegalArgumentException(format(NO_IRIS_FOR_PROBLEM_FOUND, problem));
	}

	/**
	 * Register a Iris solver within the Axela engine.
	 * 
	 * @param problem to solve
	 * @param facts   to use
	 * @param solver  solver for this problem and fact(s).
	 * @param <P>     generic problem type to solve
	 * @param <F>     generic fact type
	 * @param <S>     generic solution type to return
	 */
	<P, F, S> void registerSolver(final P problem, final F facts, final Iris<P, F, S> solver);

	/**
	 * Check if a pre-registered solver for the given problem and fact(s) is
	 * registered.
	 * 
	 * @param p problem to solve
	 * @param f facts to use
	 * @return true if solver known otherwise false.
	 * @param <P> generic problem type to solve
	 * @param <F> generic fact type
	 * @param <S> generic solution type to return
	 */
	<P, F, S> boolean hasSolverFor(final P p, final F f);

	/**
	 * Find a pre-registered solver for the given problem and fact(s).
	 * 
	 * @param p problem to solve
	 * @param f facts to use
	 * @return solver for this problem and fact(s).
	 * @param <P> generic problem type to solve
	 * @param <F> generic fact type
	 * @param <S> generic solution type to return
	 */
	<P, F, S> Iris<P, F, S> findSolverFor(final P p, final F f);
	
	interface Plugin {
		default void init(Axela engine) {
			
		}
	}
	
	default void register(Plugin plugin) {
		plugin.init(this);
	}
}
