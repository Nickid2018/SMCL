package com.github.nickid2018.jmcl.functions;

import com.github.nickid2018.jmcl.*;

public class Tan extends FunctionStatement {

	@Override
	public double calculate(VariableList list) {
		double v = ms.calculate(list);
		if (v % Math.PI == Math.PI / 2 && JMCL.radium)
			throw new ArithmeticException("tan:argument is invalid-" + v);
		if (v % 180 == 90 && !JMCL.radium)
			throw new ArithmeticException("tan:argument is invalid-" + v);
		return JMCL.radium ? Math.tan(ms.calculate(list)) : Math.tan(Math.toRadians(ms.calculate(list)));
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
