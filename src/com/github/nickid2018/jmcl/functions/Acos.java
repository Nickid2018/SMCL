package com.github.nickid2018.jmcl.functions;

import com.github.nickid2018.jmcl.*;

public class Acos extends FunctionStatement {

	@Override
	public double calculate(VariableList list) {
		return jmcl.settings.degreeAngle ? Math.toDegrees(Math.acos(ms.calculate(list)))
				: Math.acos(ms.calculate(list));
	}

	@Override
	public String toString() {
		if (!ms.getClass().equals(Variable.class))
			return "acos(" + ms + ")";
		else
			return "acos" + ms;
	}

	public static final Acos format(String s, JMCL jmcl) throws MathException {
		Acos acos = jmcl.obtain(Acos.class);
		if (s.startsWith("acos")) {
			acos.ms = JMCLRegister.getStatement(s.substring(4), jmcl);
		}
		return acos;
	}
}
