package com.github.nickid2018.smcl;

public abstract class Statement {

	protected SMCL smcl;
	protected boolean isNegative;

	// Statement Base Functions

	public boolean isNegative() {
		return isNegative;
	}

	public Statement getNegative() {
		isNegative = !isNegative;
		return this;
	}

	@Override
	public abstract String toString();

	protected abstract double calculateInternal(VariableList list);

	public double calculate(VariableList list) {
		return isNegative ? -calculateInternal(list) : calculateInternal(list);
	}
}
