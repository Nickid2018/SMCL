package com.github.nickid2018.smcl.functions;

import com.github.nickid2018.smcl.*;

public class Sin extends FunctionStatement {

	@Override
	public double calculate(VariableList list) {
		return jmcl.settings.degreeAngle ? Math.sin(Math.toRadians(ms.calculate(list))) : Math.sin(ms.calculate(list));
	}

	@Override
	public String toString() {
		if (!ms.getClass().equals(Variable.class))
			return "sin(" + ms + ")";
		else
			return "sin" + ms;
	}

	public static final Sin format(String s, JMCL jmcl) throws MathException {
		Sin sin = jmcl.obtain(Sin.class);
		if (s.startsWith("sin")) {
			sin.ms = JMCLRegister.getStatement(s.substring(3), jmcl);
		}
		return sin;
	}
}
