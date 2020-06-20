package com.github.nickid2018.smcl.functions;

import com.github.nickid2018.smcl.*;

public class Lg extends FunctionStatement {

	@Override
	public double calculate(VariableList list) {
		double v = ms.calculate(list);
		if (v <= 0)
			throw new ArithmeticException("lg:argument is invalid-" + v);
		return Math.log10(v);
	}

	@Override
	public String toString() {
		if (!ms.getClass().equals(Variable.class))
			return "lg(" + ms + ")";
		else
			return "lg" + ms;
	}

	public static final Lg format(String s, JMCL jmcl) throws MathException {
		Lg lg = jmcl.obtain(Lg.class);
		if (s.startsWith("lg")) {
			lg.ms = JMCLRegister.getStatement(s.substring(2), jmcl);
		}
		return lg;
	}
}
