package com.github.nickid2018.jmcl.func;

import com.github.nickid2018.jmcl.*;

public class Tan extends FunctionStatement {

	@Override
	public double calc(VariableList list) {
		double v = ms.calc(list);
		if (v % Math.PI == Math.PI / 2)
			throw new ArithmeticException("tan:argument is invalid-" + v);
		return Math.tan(v);
	}

	@Override
	public String toString() {
		if (!ms.getClass().equals(Variable.class))
			return "tan(" + ms + ")";
		else
			return "tan" + ms;
	}

	public static final Tan format(String s) throws MathException {
		Tan tan = JMCL.obtain(Tan.class);
		if (s.startsWith("tan")) {
			tan.ms = JMCLRegister.getStatement(s.substring(3));
		}
		return tan;
	}
}
