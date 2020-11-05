package com.github.nickid2018.smcl.statements;

import com.github.nickid2018.smcl.*;

public class VoidStatement extends Statement {

	public static final VoidStatement PARAMS_START_STATEMENT = new VoidStatement();

	private VoidStatement() {
	}

	@Override
	public boolean canClose() {
		return false;
	}

	@Override
	public String toString() {
		return "void";
	}

	@Override
	public boolean isAllNum() {
		return false;
	}

	@Override
	public double calculateInternel(VariableList list) {
		return 0;
	}

	@Override
	public Statement setValues(Statement... statements) {
		return this;
	}

}
