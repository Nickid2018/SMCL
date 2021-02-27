package com.github.nickid2018.smcl.functions;

import com.github.nickid2018.smcl.*;

public abstract class UnaryFunctionStatement extends FunctionStatement {

	protected Statement innerStatement;

	public UnaryFunctionStatement(Statement statement) {
		innerStatement = statement;
	}
}
