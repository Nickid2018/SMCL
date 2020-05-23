package com.github.nickid2018.jmcl.func;

import com.github.nickid2018.jmcl.*;

public class Acos extends FunctionStatement {

	@Override
	public double calc(VariableList list) {
		return Math.acos(ms.calc(list));
	}

	@Override
	public String toString() {
		if (!ms.getClass().equals(Variable.class))
			return "acos(" + ms + ")";
		else
			return "acos" + ms;
	}

	public static final Acos format(String s) throws MathException {
		Acos acos = JMCL.obtain(Acos.class);
		if (s.startsWith("acos")) {
			acos.ms = JMCLRegister.getStatement(s.substring(4));
		}
		return acos;
	}
}
