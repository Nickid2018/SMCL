package com.github.nickid2018.jmcl;

import com.github.nickid2018.jmcl.statements.*;

public abstract class Statement implements AutoCloseable {

	protected int shares = 1;

	public void share() {
		shares++;
	}

	public void unshare() {
		shares--;
	}

	@Override
	public void close() throws Exception {
		free();
	}

	public boolean free() {
		shares--;
		if (shares < 1 && canClose()) {
			JMCL.free(this);
			return true;
		}
		return false;
	}

	public boolean canClose() {
		return true;
	}

	@Override
	public abstract String toString();

	public abstract boolean isAllNum();

	public abstract double calc(VariableList list);

	public abstract void setValues(Statement... statements);

	public static Statement format(String expr) throws MathException {
		return MathStatement.format(expr);
	}
}
