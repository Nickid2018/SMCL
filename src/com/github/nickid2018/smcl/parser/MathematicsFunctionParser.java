package com.github.nickid2018.smcl.parser;

import java.util.function.*;
import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.functions.*;

public class MathematicsFunctionParser<T extends FunctionStatement> extends FunctionParser<T> {

	private final int numParams;
	private final BiFunction<SMCL, Statement[], T> map;

	public MathematicsFunctionParser(int numParams, BiFunction<SMCL, Statement[], T> map) {
		this.numParams = numParams;
		this.map = map;
	}

	@Override
	public boolean numParamsVaries() {
		return numParams != 1;
	}

	@Override
	public int getNumParams() {
		return numParams;
	}

	@Override
	public T parseStatement(SMCL smcl, Statement... statements) {
		return map.apply(smcl, statements);
	}

}
