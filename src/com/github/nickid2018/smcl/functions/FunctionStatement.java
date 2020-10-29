package com.github.nickid2018.smcl.functions;

import com.github.nickid2018.smcl.*;

public abstract class FunctionStatement extends Statement {

	@Override
	public Statement setValues(Statement... statements) {
		return this;
	}
}
