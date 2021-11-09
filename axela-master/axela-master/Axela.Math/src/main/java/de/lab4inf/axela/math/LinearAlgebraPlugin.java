package de.lab4inf.axela.math;

import java.util.Objects;

import de.lab4inf.axela.core.Axela;
import de.lab4inf.axela.facts.FactBase;
import de.lab4inf.axela.math.LinearAlgebra.MatVecCalc;
import de.lab4inf.axela.math.LinearAlgebra.MatrixCalc;
import de.lab4inf.axela.math.LinearAlgebra.VectorCalc;

public class LinearAlgebraPlugin implements Axela.Plugin {
	
	@Override
	public void init(Axela engine) {
		Objects.requireNonNull(engine, "engine is a Nullpointer!");
		double[] vector = new double[0];
		double[][] matrix = new double[0][0];
		FactBase<double[], double[]> vecvec = new FactBase<>(vector, vector);
		FactBase<double[][], double[][]> matmat = new FactBase<>(matrix, matrix);
		FactBase<double[][], double[]> matvec = new FactBase<>(matrix, vector);
		engine.registerSolver("PLUS", vecvec, new VectorCalc());
		engine.registerSolver("MULT", vecvec, new VectorCalc());
		engine.registerSolver("PLUS", matmat, new MatrixCalc());
		engine.registerSolver("MULT", matmat, new MatrixCalc());
		engine.registerSolver("MULT", matvec, new MatVecCalc());
	}
}
