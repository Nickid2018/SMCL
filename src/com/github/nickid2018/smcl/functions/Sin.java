package com.github.nickid2018.smcl.functions;

import com.github.nickid2018.smcl.*;

public class Sin extends FunctionStatement {

	@Override
	public double calculate(VariableList list) {
		return smcl.settings.degreeAngle ? Math.sin(Math.toRadians(ms.calculate(list))) : Math.sin(ms.calculate(list));
	}

	@Override
	public String toString() {
		if (!ms.getClass().equals(Variable.class))
			return "sin(" + ms + ")";
		else
			return "sin" + ms;
	}

	public static final Sin format(String s, SMCL smcl) throws MathException {
		Sin sin = smcl.obtain(Sin.class);
		if (s.startsWith("sin")) {
			sin.ms = smcl.register.getStatement(s.substring(3));
		}
		return sin;
	}
}
