package com.github.nickid2018.smcl.functions;

import com.github.nickid2018.smcl.*;

public class Asin extends FunctionStatement {

	@Override
	public double calculate(VariableList list) {
		return smcl.settings.degreeAngle ? Math.toDegrees(Math.asin(ms.calculate(list)))
				: Math.asin(ms.calculate(list));
	}

	@Override
	public String toString() {
		if (!ms.getClass().equals(Variable.class))
			return "asin(" + ms + ")";
		else
			return "asin" + ms;
	}

	public static final Asin format(String s, SMCL smcl) throws MathException {
		Asin asin = smcl.obtain(Asin.class);
		if (s.startsWith("asin")) {
			asin.ms = smcl.register.getStatement(s.substring(4));
		}
		return asin;
	}
}
