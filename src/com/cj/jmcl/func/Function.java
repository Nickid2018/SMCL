package com.cj.jmcl.func;

import com.cj.jmcl.*;
import com.cj.jmcl.Number;

public class Function extends MathStatement {
	
	protected MathStatement ms;
	
	@Override
	public boolean isAllNum() {
		return ms instanceof Number;
	}
}
