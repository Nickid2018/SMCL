package com.github.nickid2018.smcl.functions;

import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.statements.*;

public abstract class UnaryFunctionStatement extends FunctionStatement {

	protected Statement ms;

	public UnaryFunctionStatement(Statement statement) {
		ms = statement;
	}

	@Override
	public boolean isAllNum() {
		return ms instanceof NumberStatement;
	}

	@Override
	public Statement setValues(Statement... statements) {
		ms = statements[0];
		return this;
	}

	@Override
	public void doOnFree() {
		ms.free();
		ms = null;
	}
}
