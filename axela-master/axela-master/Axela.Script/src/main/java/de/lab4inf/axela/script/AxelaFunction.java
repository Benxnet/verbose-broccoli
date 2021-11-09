package de.lab4inf.axela.script;

import java.util.function.ToDoubleFunction;

public interface AxelaFunction extends ToDoubleFunction<double[]> {
	
	/**
	* Function evaluation mapping tuple x=(x1,...,xn) to y = f(x1 ,..., xn)
	∗ using varargs.
	*
	* @param	x the double array tuple x1,...,xn
	* @return f(x1 ,..., xn)
	*/
	
	default	double apply(double... x){
		return applyAsDouble(x);
	}
}
