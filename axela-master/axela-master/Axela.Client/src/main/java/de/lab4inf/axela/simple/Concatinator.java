package de.lab4inf.axela.simple;

import java.util.Objects;

import de.lab4inf.axela.core.Axela;
import de.lab4inf.axela.core.Iris;

public class Concatinator implements Iris<String, String[], String> {

	public Concatinator() {

	}

	static public void init(Axela engine) {
		Objects.requireNonNull(engine, "engine is a Nullpointer!");
		engine.registerSolver("CONCAT", new String[0], new Concatinator());
	}

	// Errorhandling
	public void verify(String problem, String[] facts) {
		Objects.requireNonNull(facts, "facts are a NullPointer!");
		Objects.requireNonNull(problem, "problem is a NullPointer!");

		if (facts.length == 0) { // Facts leer
			throw new IllegalArgumentException("no facts given");
		}
	}

	// Solverfunktion
	@Override
	public String solve(String problem, String[] facts) {
		verify(problem, facts);

		StringBuilder str = new StringBuilder(facts[0]); // String mit erstem Wort erstellen

		for (int i = 1; i < facts.length; i++) { // Alle Wörter durchlaufen
			str.append(" ");
			str.append(facts[i]); // Nächstes Wort hinzufügen
		}
		return str.toString(); // Gebe Lösung zurück
	}
}