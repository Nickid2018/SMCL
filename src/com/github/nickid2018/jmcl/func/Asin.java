package com.github.nickid2018.jmcl.func;

import com.github.nickid2018.jmcl.*;

public class Asin extends FunctionStatement {

	@Override
	public double calc(VariableList list) {
		return Math.asin(ms.calc(list));
	}

	@Override
	public String toString() {
		if (!ms.getClass().equals(Variable.class))
			return "asin(" + ms + ")";
		else
			return "asin" + ms;
	}

	public static final Asin format(String s) throws MathException {
		Asin asin = JMCL.obtain(Asin.class);
		if (s.startsWith("asin")) {
			asin.ms = JMCLRegister.getStatement(s.substring(4));
		}
		return asin;
	}
}
