package com.github.nickid2018.smcl.functions;

import com.github.nickid2018.smcl.*;

public abstract class UnaryFunctionStatement extends FunctionStatement {

	protected Statement innerStatement;

	public UnaryFunctionStatement(Statement statement) {
		super(statement.getSMCL(), statement.getVariables());
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
