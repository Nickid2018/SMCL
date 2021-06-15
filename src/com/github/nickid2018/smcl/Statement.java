package com.github.nickid2018.smcl;

public abstract class Statement implements Cloneable {

	protected SMCL smcl;
	protected boolean isNegative;
	protected DefinedVariables variables;

	public Statement(SMCL smcl) {
		this.smcl = smcl;
		this.variables = smcl.globalvars.toDefinedVariables();
	}

	public Statement(SMCL smcl, DefinedVariables variables) {
		this.smcl = smcl;
		this.variables = variables;
	}

	// Statement Base Functions

	public boolean isNegative() {
		return isNegative;
	}

	public Statement getNegative() {
		isNegative = !isNegative;
		return this;
	}

	public Statement getNewNegative() {
		try {
			Statement s2 = (Statement) clone();
			return s2.getNegative();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	public DefinedVariables getVariables() {
		return variables;
	}

	public SMCL getSMCL() {
		return smcl;
	}

	@Override
	public abstract String toString();

	protected abstract double calculateInternal(VariableList list);

	public double calculate(VariableList list) {
		return isNegative ? -calculateInternal(list) : calculateInternal(list);
	}

	// Only single variable
	public Statement derivative() {
		if (variables.size() > 1)
			throw new ArithmeticException("Statement " + this + " has more than one independent value");
		return isNegative ? derivativeInternal().getNegative() : derivativeInternal();
	}

	protected abstract Statement derivativeInternal();

	public void setSMCL(SMCL smcl) {
		this.smcl = smcl;
	}
}
