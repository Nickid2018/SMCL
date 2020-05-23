package com.github.nickid2018.jmcl.func;

import com.github.nickid2018.jmcl.*;

public class Atan extends FunctionStatement {

	@Override
	public double calc(VariableList list) {
		return Math.atan(ms.calc(list));
	}

	@Override
	public String toString() {
		if (!ms.getClass().equals(Variable.class))
			return "atan(" + ms + ")";
		else
			return "atan" + ms;
	}

	public static final Atan format(String s) throws MathException {
		Atan atan = JMCL.obtain(Atan.class);
		if (s.startsWith("atan")) {
			atan.ms = JMCLRegister.getStatement(s.substring(4));
		}
		return atan;
	}
}
