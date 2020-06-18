package com.github.nickid2018.jmcl.functions;

import com.github.nickid2018.jmcl.*;

public class Cos extends FunctionStatement {

	@Override
	public double calculate(VariableList list) {
		return jmcl.settings.degreeAngle ? Math.cos(Math.toRadians(ms.calculate(list))) : Math.cos(ms.calculate(list));
	}

	@Override
	public String toString() {
		if (!ms.getClass().equals(Variable.class))
			return "cos(" + ms + ")";
		else
			return "cos" + ms;
	}

	public static final Cos format(String s, JMCL jmcl) throws MathException {
		Cos cos = jmcl.obtain(Cos.class);
		if (s.startsWith("cos")) {
			cos.ms = JMCLRegister.getStatement(s.substring(3), jmcl);
		}
		return cos;
	}
}
