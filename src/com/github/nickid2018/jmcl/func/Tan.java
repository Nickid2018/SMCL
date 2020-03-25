package com.github.nickid2018.jmcl.func;

import java.util.*;

import com.github.nickid2018.jmcl.*;

public class Tan extends Function {

	@Override
	public double calc(Map<String, Double> values) {
		double v = ms.calc(values);
		if (v % Math.PI == Math.PI / 2)
			throw new ArithmeticException("tan:argument is invalid-" + v);
		return Math.tan(v);
	}

	@Override
	public String toString() {
		if (!ms.getClass().equals(Variable.class))
			return "tan(" + ms + ")";
		else
			return "tan" + ms;
	}

	public static final Tan format(String s) throws MathException {
		Tan tan = new Tan();
		if (s.startsWith("tan")) {
			tan.ms = JMCLRegister.getStatement(s.substring(3));
		}
		return tan;
	}
}
