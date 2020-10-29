package com.github.nickid2018.smcl.parser;

import com.github.nickid2018.smcl.*;

public abstract class OperatorParser<T extends Statement> {

	public abstract int getPriority();

	public abstract boolean isLeftAssoc();

	public abstract Statement parseStatement(SMCL smcl, Statement... statements);
}
