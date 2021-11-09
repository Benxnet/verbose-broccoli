module Axela.Script {
	exports de.lab4inf.axela.script;
	exports de.lab4inf.axela.math.ast;

	requires transitive Axela.Math;
	requires transitive java.scripting;
	
	uses javax.script.ScriptEngineFactory;
	
	provides javax.script.ScriptEngineFactory with de.lab4inf.axela.script.AxelaScriptEngineFactory;
}