package com.github.nickid2018.smcl.functions;

import com.github.nickid2018.smcl.*;

public class Acos extends FunctionStatement {

	@Override
	public double calculate(VariableList list) {
		return smcl.settings.degreeAngle ? Math.toDegrees(Math.acos(ms.calculate(list)))
				: Math.acos(ms.calculate(list));
	}

	@Override
	public String toString() {
		if (!ms.getClass().equals(Variable.class))
			return "acos(" + ms + ")";
		else
			return "acos" + ms;
	}

	public static final Acos format(String s, SMCL smcl) throws MathException {
		Acos acos = smcl.obtain(Acos.class);
		if (s.startsWith("acos")) {
			acos.ms = smcl.register.getStatement(s.substring(4));
		}
		return acos;
	}
}
