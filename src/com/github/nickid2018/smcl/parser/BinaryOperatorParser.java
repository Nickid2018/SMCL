package com.github.nickid2018.smcl.parser;

import java.util.function.*;
import com.github.nickid2018.smcl.*;

public class BinaryOperatorParser<T extends Statement> extends OperatorParser<T> {

	private final int priority;
	private final boolean leftAssoc;
	private final BiFunction<SMCL, Statement[], T> map;

	public BinaryOperatorParser(int priority, boolean leftAssoc, BiFunction<SMCL, Statement[], T> map) {
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
	public Statement parseStatement(SMCL smcl, Statement... pop) {
		return map.apply(smcl, pop);
	}

}
