package com.github.nickid2018.jmcl.func;

import com.github.nickid2018.jmcl.*;

public class Cos extends FunctionStatement {

	@Override
	public double calc(VariableList list) {
		return Math.cos(ms.calc(list));
	}

	@Override
	public String toString() {
		if (!ms.getClass().equals(Variable.class))
			return "cos(" + ms + ")";
		else
			return "cos" + ms;
	}

	public static final Cos format(String s) throws MathException {
		Cos cos = JMCL.obtain(Cos.class);
		if (s.startsWith("cos")) {
			cos.ms = JMCLRegister.getStatement(s.substring(3));
		}
		return cos;
	}
}
