package com.github.nickid2018.smcl.functions;

import com.github.nickid2018.smcl.*;

public class Tan extends FunctionStatement {

	@Override
	public double calculate(VariableList list) {
		double v = ms.calculate(list);
		if (v % Math.PI == Math.PI / 2 && !jmcl.settings.degreeAngle)
			throw new ArithmeticException("tan:argument is invalid-" + v);
		if (v % 180 == 90 && jmcl.settings.degreeAngle)
			throw new ArithmeticException("tan:argument is invalid-" + v);
		return jmcl.settings.degreeAngle ? Math.tan(Math.toRadians(ms.calculate(list))) : Math.tan(ms.calculate(list));
	}

	@Override
	public String toString() {
		if (!ms.getClass().equals(Variable.class))
			return "tan(" + ms + ")";
		else
			return "tan" + ms;
	}

	public static final Tan format(String s, JMCL jmcl) throws MathException {
		Tan tan = jmcl.obtain(Tan.class);
		if (s.startsWith("tan")) {
			tan.ms = JMCLRegister.getStatement(s.substring(3), jmcl);
		}
		return tan;
	}
}
