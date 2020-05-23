package com.github.nickid2018.jmcl.func;

import com.github.nickid2018.jmcl.*;

public class Lg extends FunctionStatement {

	@Override
	public double calc(VariableList list) {
		double v = ms.calc(list);
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

	public static final Lg format(String s) throws MathException {
		Lg lg = JMCL.obtain(Lg.class);
		if (s.startsWith("lg")) {
			lg.ms = JMCLRegister.getStatement(s.substring(2));
		}
		return lg;
	}
}
