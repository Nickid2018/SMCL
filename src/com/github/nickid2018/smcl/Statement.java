package com.github.nickid2018.smcl;

public abstract class Statement implements AutoCloseable {

	protected int shares = 1;
	protected SMCL smcl;
	protected boolean isNegative;

	// Resource Manage

	public void share() {
		shares++;
	}

	public void unshare() {
		shares--;
	}

	@Override
	public final void close() {
		free();
	}

	public final boolean free() {
		shares--;
		if (shares < 1 && canClose()) {
			smcl.free(this);
			doOnFree();
			return true;
		}
		return false;
	}

	public final boolean tryOnlyFree() {
		shares--;
		if (shares < 1 && canClose()) {
			smcl.free(this);
			return true;
		}
		return false;
	}

	public void doOnFree() {
		isNegative = false;
	}

	public boolean canClose() {
		return true;
	}

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

	public abstract boolean isAllNum();

	public abstract double calculateInternel(VariableList list);

	public double calculate(VariableList list) {
		return isNegative ? -calculateInternel(list) : calculateInternel(list);
	}

	// Object Reuse

	public abstract Statement setValues(Statement... statements);
}
