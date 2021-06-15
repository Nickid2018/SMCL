package com.github.nickid2018.smcl.parser;

import com.github.nickid2018.smcl.*;

public class UnaryOperatorParser<T extends Statement> extends OperatorParser<T> {

	private final int priority;
	private final boolean leftAssoc;
	private final TriFunction<SMCL, Statement, DefinedVariables, T> map;

	public UnaryOperatorParser(int priority, boolean leftAssoc, TriFunction<SMCL, Statement, DefinedVariables, T> map) {
		this.priority = priority;
		this.leftAssoc = leftAssoc;
		this.map = map;
	}

	@Override
	public int getPriority() {
		return priority;
	}

	@Override
	public boolean isLeftAssoc() {
		return leftAssoc;
	}

	@Override
	public Statement parseStatement(SMCL smcl, DefinedVariables variables, Statement... pop) {
		return map.accept(smcl, pop[0], variables);
	}

}
