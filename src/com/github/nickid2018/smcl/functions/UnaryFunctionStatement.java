package com.github.nickid2018.smcl.functions;

import com.github.nickid2018.smcl.*;

public abstract class UnaryFunctionStatement extends FunctionStatement {

	private Statement innerStatement;

	public UnaryFunctionStatement(Statement statement) {
		setInnerStatement(statement);
	}

	public Statement getInnerStatement() {
		return innerStatement;
	}

	public UnaryFunctionStatement setInnerStatement(Statement innerStatement) {
		this.innerStatement = innerStatement;
		return this;
	}

}
