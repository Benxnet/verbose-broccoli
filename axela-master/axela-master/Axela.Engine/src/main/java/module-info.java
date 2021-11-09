module Axela.Engine {
	exports de.lab4inf.axela.engine;
	
	requires transitive Axela.Core;
	
	uses de.lab4inf.axela.core.Axela;
	
	provides de.lab4inf.axela.core.Axela with de.lab4inf.axela.engine.AxelaEngine;
}