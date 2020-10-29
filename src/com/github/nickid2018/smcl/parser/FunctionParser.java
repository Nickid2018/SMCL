package com.github.nickid2018.smcl.parser;

import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.functions.*;

public abstract class FunctionParser<T extends FunctionStatement> {

	public abstract boolean numParamsVaries();

	public abstract int getNumParams();

	public abstract Statement parseStatement(SMCL smcl, Statement... statements);
}
