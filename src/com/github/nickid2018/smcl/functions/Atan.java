package com.github.nickid2018.smcl.functions;

import com.github.nickid2018.smcl.*;

public class Atan extends FunctionStatement {

	@Override
	public double calculate(VariableList list) {
		return smcl.settings.degreeAngle ? Math.toDegrees(Math.atan(ms.calculate(list)))
				: Math.atan(ms.calculate(list));
	}

	@Override
	public String toString() {
		if (!ms.getClass().equals(Variable.class))
			return "atan(" + ms + ")";
		else
			return "atan" + ms;
	}

	public static final Atan format(String s, SMCL smcl) throws MathException {
		Atan atan = smcl.obtain(Atan.class);
		if (s.startsWith("atan")) {
			atan.ms = smcl.register.getStatement(s.substring(4));
		}
		return atan;
	}
}
