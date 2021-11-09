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

import java.util.Arrays;
import java.util.List;

import javax.script.*;

/**
 * Axela implementation of the ScriptEngineFactory.
 * 
 * 
 * @author nwulff
 * @since 17.11.2020
 */
public class AxelaScriptEngineFactory implements ScriptEngineFactory {
	private static ScriptContext globalContext = new SimpleScriptContext();
	private static Bindings globalBindings = new SimpleBindings();
	private static AxelaScriptEngineFactory factory;
	/** name of the script language. */
	public static final String AXELA_SCRIPT = "AxelaScript";
	/** version string. */
	public static final String AXELA_SRIPT_VERSION = "1.0.0";

	/**
	 * POJO constructor
	 */
	public AxelaScriptEngineFactory() {
	}

	/**
	 * static provider method for reflection.
	 * 
	 * @return a ScriptEngineFactory
	 */
	public static ScriptEngineFactory provides() {
		if (null == factory)
			synchronized (AxelaScriptEngineFactory.class) {
				if (null == factory) {
					factory = new AxelaScriptEngineFactory();
				}
			}
		return factory;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.script.ScriptEngineFactory#getEngineName()
	 */
	@Override
	public String getEngineName() {
		return "Axela-ScriptEngine";
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.script.ScriptEngineFactory#getEngineVersion()
	 */
	@Override
	public String getEngineVersion() {
		return AXELA_SRIPT_VERSION;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.script.ScriptEngineFactory#getExtensions()
	 */
	@Override
	public List<String> getExtensions() {
		return Arrays.asList("axela");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.script.ScriptEngineFactory#getMimeTypes()
	 */
	@Override
	public List<String> getMimeTypes() {
		return Arrays.asList("text/plain");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.script.ScriptEngineFactory#getNames()
	 */
	@Override
	public List<String> getNames() {
		return Arrays.asList("AxelaEngine", "ae");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.script.ScriptEngineFactory#getLanguageName()
	 */
	@Override
	public String getLanguageName() {
		return AXELA_SCRIPT;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.script.ScriptEngineFactory#getLanguageVersion()
	 */
	@Override
	public String getLanguageVersion() {
		return AXELA_SRIPT_VERSION;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.script.ScriptEngineFactory#getParameter(java.lang.String)
	 */
	@Override
	public Object getParameter(String key) {
		return globalBindings.get(key);
	}

	void setParameter(String key, Object value) {
		globalBindings.put(key, value);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.script.ScriptEngineFactory#getScriptEngine()
	 */
	@Override
	public ScriptEngine getScriptEngine() {
		return new AxelaScriptEngine(this, globalContext);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.script.ScriptEngineFactory#getMethodCallSyntax(java.lang.String,
	 *      java.lang.String, java.lang.String[])
	 */
	@Override
	public String getMethodCallSyntax(String obj, String m, String... args) {
		throw new IllegalStateException("not implemented yet ...");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.script.ScriptEngineFactory#getOutputStatement(java.lang.String)
	 */
	@Override
	public String getOutputStatement(String toDisplay) {
		throw new IllegalStateException("not implemented yet ...");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.script.ScriptEngineFactory#getProgram(java.lang.String[])
	 */
	@Override
	public String getProgram(String... statements) {
		throw new IllegalStateException("not implemented yet ...");
	}

}
