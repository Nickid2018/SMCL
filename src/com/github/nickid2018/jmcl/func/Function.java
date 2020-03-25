package com.github.nickid2018.jmcl.func;

import com.github.nickid2018.jmcl.Number;
import com.github.nickid2018.jmcl.MathStatement;

public class Function extends MathStatement {

	protected MathStatement ms;

	@Override
	public boolean isAllNum() {
		return ms instanceof Number;
	}
}
