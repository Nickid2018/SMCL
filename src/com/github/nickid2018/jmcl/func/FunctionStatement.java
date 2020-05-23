package com.github.nickid2018.jmcl.func;

import com.github.nickid2018.jmcl.*;

public class FunctionStatement extends MathStatement {

	protected MathStatement ms;

	@Override
	public boolean isAllNum() {
		return ms instanceof NumberStatement;
	}
}
