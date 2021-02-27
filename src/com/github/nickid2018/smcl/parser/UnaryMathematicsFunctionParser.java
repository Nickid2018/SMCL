package com.github.nickid2018.smcl.parser;

import java.util.function.*;
import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.functions.*;

public class UnaryMathematicsFunctionParser<T extends UnaryFunctionStatement> extends FunctionParser<T> {

	private final BiFunction<SMCL, Statement[], ? extends UnaryFunctionStatement> map;

	public UnaryMathematicsFunctionParser(UnaryFunctionBuilder unary) {
		map = unary::create;
	}

	public UnaryMathematicsFunctionParser(BiFunction<SMCL, Statement[], ? extends UnaryFunctionStatement> map) {
		this.map = map;
	}

	@Override
	public boolean numParamsVaries() {
		return false;
	}

	@Override
	public int getNumParams() {
		return 1;
	}

	@Override
	public UnaryFunctionStatement parseStatement(SMCL smcl, Statement... statements) {
		return map.apply(smcl, statements);
	}

}
