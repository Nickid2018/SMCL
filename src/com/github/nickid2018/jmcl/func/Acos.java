package com.github.nickid2018.jmcl.func;

import java.util.*;

import com.github.nickid2018.jmcl.*;

public class Acos extends Function {

	@Override
	public double calc(Map<String, Double> values) {
		return Math.acos(ms.calc(values));
	}

	@Override
	public String toString() {
		if (!ms.getClass().equals(Variable.class))
			return "acos(" + ms + ")";
		else
			return "acos" + ms;
	}

	public static final Acos format(String s) throws MathException {
		Acos acos = new Acos();
		if (s.startsWith("acos")) {
			acos.ms = JMCLRegister.getStatement(s.substring(4));
		}
		return acos;
	}
}
