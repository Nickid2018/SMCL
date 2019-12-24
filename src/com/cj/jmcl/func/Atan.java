package com.cj.jmcl.func;

import java.util.*;
import com.cj.jmcl.*;

public class Atan extends Function {

	@Override
	public double calc(Map<String, Double> values) {
		return Math.atan(ms.calc(values));
	}
	
	@Override
	public String toString() {
		if(!ms.getClass().equals(Variable.class))
			return "atan("+ms+")";
		else return "atan"+ms;
	}

	public static final Atan format(String s) throws MathException {
		Atan atan = new Atan();
		if (s.startsWith("atan")) {
			atan.ms = JMCLRegister.getStatement(s.substring(4));
		}
		return atan;
	}
}
