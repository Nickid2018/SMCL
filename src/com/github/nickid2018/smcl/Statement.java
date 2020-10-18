package com.github.nickid2018.smcl;

import com.github.nickid2018.smcl.statements.*;

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

	public void setNegative(boolean negative) {
		isNegative = negative;
	}

	@Override
	public abstract String toString();

	public abstract boolean isAllNum();

	public abstract double calculate(VariableList list);

	// Object Reuse

	public abstract void setValues(Statement... statements);

	public static Statement format(String expr) throws MathException {
		return MathStatement.format(expr);
	}
}
