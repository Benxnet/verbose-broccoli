package de.lab4inf.axela.facts;

import java.util.Objects;

public class FactBase<F1, F2> {

	private final F1 fact1;
	private final F2 fact2;
	private final boolean speedUp;

	public FactBase(F1 fact1, F2 fact2) {
		this.fact1 = Objects.requireNonNull(fact1, "fact is a Nullpointer!");
		this.fact2 = Objects.requireNonNull(fact2, "fact is a Nullpointer!");
		this.speedUp = false;
	}
	
	public FactBase(F1 fact1, F2 fact2, boolean speedUp) {
		this.fact1 = Objects.requireNonNull(fact1, "fact is a Nullpointer!");
		this.fact2 = Objects.requireNonNull(fact2, "fact is a Nullpointer!");
		this.speedUp = Objects.requireNonNull(speedUp, "fact is a Nullpointer!");
	}

	public F1 getFact1() {
		return this.fact1;
	}

	public F2 getFact2() {
		return this.fact2;
	}
	
	public boolean getSpeedUp() {
		return this.speedUp;
	}

	public int getSignature() {
		return Objects.hash(this.fact1.getClass(), this.fact2.getClass());
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.fact1, this.fact2);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof FactBase))
			return false;
		FactBase<?, ?> other = (FactBase<?, ?>) obj;
		return this.fact1.equals(other.fact1) && this.fact2.equals(other.fact2);
	}

}
