package com.github.nickid2018.jmcl.functions;

import com.github.nickid2018.jmcl.*;

public class Atan extends FunctionStatement {

	@Override
	public double calculate(VariableList list) {
		return JMCL.radium ? Math.atan(ms.calculate(list)) : Math.toDegrees(Math.atan(ms.calculate(list)));
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
