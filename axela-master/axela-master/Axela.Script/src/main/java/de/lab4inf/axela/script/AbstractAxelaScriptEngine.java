/*
 * Project: Axela.Script
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

package de.lab4inf.axela.script;

import java.io.LineNumberReader;
import java.io.Reader;
import java.util.Objects;

import javax.script.*;

import de.lab4inf.axela.core.Axela;

/**
 * Abstract Axela implementation of the ScriptEngine interface, with some
 * defaults, only the parsing is missing.
 * 
 * 
 * @author nwulff
 * @since 17.11.2020
 */
public abstract class AbstractAxelaScriptEngine extends AbstractScriptEngine implements ScriptEngine {
	final static Axela axela = Axela.getEngine();
	final AxelaScriptEngineFactory factory;
	final ScriptContext globalContext;
	ScriptContext localContext = new SimpleScriptContext();

	/**
	 * Constructor with a script factory.
	 * 
	 * @param factory   of this engine
	 * @param globalCtx global context set by the factory
	 * 
	 */
	public AbstractAxelaScriptEngine(AxelaScriptEngineFactory factory, ScriptContext globalCtx) {
		this.factory = Objects.requireNonNull(factory, "factory is null");
		this.globalContext = Objects.requireNonNull(globalCtx, "global context is null");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.script.ScriptEngine#getFactory()
	 */
	@Override
	public ScriptEngineFactory getFactory() {
		return factory;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.script.ScriptEngine#createBindings()
	 */
	@Override
	public Bindings createBindings() {
		return new SimpleBindings();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.script.ScriptEngine#eval(java.io.Reader,
	 *      javax.script.ScriptContext)
	 */
	@Override
	public Object eval(Reader reader, ScriptContext ctx) throws ScriptException {
		return eval(reader, ctx.getBindings(ScriptContext.GLOBAL_SCOPE));
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.script.ScriptEngine#eval(java.io.Reader, javax.script.Bindings)
	 */
	@Override
	public Object eval(Reader reader, Bindings n) throws ScriptException {
		// This hook method delegates everything to the string version
		// not optimal but a start ...
		StringBuilder sb = new StringBuilder();
		LineNumberReader lnb = new LineNumberReader(reader);
		try {
			while (lnb.ready()) {
				String line = lnb.readLine();
				sb.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ScriptException(e);
		}
		return eval(sb.toString(), n);
	}

}
