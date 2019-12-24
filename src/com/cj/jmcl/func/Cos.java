package com.cj.jmcl.func;

import java.util.*;
import com.cj.jmcl.*;

public class Cos extends Function {

	@Override
	public double calc(Map<String, Double> values) {
		return Math.cos(ms.calc(values));
	}
	
	@Override
	public String toString() {
		if(!ms.getClass().equals(Variable.class))
			return "cos("+ms+")";
		else return "cos"+ms;
	}

	public static final Cos format(String s) throws MathException {
		Cos cos = new Cos();
		if (s.startsWith("cos")) {
			cos.ms = JMCLRegister.getStatement(s.substring(3));
		}
		return cos;
	}
}
