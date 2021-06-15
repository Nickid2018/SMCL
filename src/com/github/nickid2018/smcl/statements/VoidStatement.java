package com.github.nickid2018.smcl.statements;

import com.github.nickid2018.smcl.*;

public class VoidStatement extends Statement {

	public static final VoidStatement PARAMS_START_STATEMENT = new VoidStatement();

	private VoidStatement() {
		super(null, DefinedVariables.EMPTY_VARIABLES);
	}

	@Override
	public String toString() {
		return "void";
	}

	@Override
	public double calculateInternal(VariableList list) {
		return 0;
	}

	@Override
	protected Statement derivativeInternal() {
		return null;
	}
}
