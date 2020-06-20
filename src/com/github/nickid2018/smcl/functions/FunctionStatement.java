package com.github.nickid2018.smcl.functions;

import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.statements.*;

public abstract class FunctionStatement extends Statement {

	protected Statement ms;

	@Override
	public boolean isAllNum() {
		return ms instanceof NumberStatement;
	}

	@Override
	public void setValues(Statement... statements) {
		ms = statements[0];
	}

	@Override
	public void doOnFree() {
		ms.free();
		ms = null;
	}
}
