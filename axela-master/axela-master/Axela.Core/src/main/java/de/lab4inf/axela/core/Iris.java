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

/**
 * Intelligent Rule-based Inference Solver (IRIS).<br>
 * Generic solver interface to be registered and used within the Axela engine as
 * the basic functional building blocks for independent solvers.
 * 
 * @see de.lab4inf.axela.core.Axela
 * @author nwulff
 * @since 27.09.2020
 * @param <Problem>  generic problem type to solve
 * @param <Facts>    generic fact type to parameterize the problem
 * @param <Solution> generic solution type to return
 */
@FunctionalInterface
public interface Iris<Problem, Facts, Solution> {
	/**
	 * Solve a problem using the given facts and return the solution. <br>
	 * Note: This interfaces uses generic types as of JDK1.5 and further it is a
	 * FunctionalInterface as of JDK1.8 suitable for lambda expressions - all this
	 * will come in the lectures.
	 * 
	 * @param problem to solve
	 * @param facts   to use
	 * @return solution
	 */
	Solution solve(Problem problem, Facts facts);
}
