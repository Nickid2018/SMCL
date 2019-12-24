package com.cj.jmcl.func;

import java.util.*;
import com.cj.jmcl.*;

public class Asin extends Function {
	
	@Override
	public double calc(Map<String, Double> values) {
		return Math.asin(ms.calc(values));
	}
	
	@Override
	public String toString() {
		if(!ms.getClass().equals(Variable.class))
			return "asin("+ms+")";
		else return "asin"+ms;
	}

	public static final Asin format(String s) throws MathException {
		Asin asin=new Asin();
		if(s.startsWith("asin")) {
			asin.ms=JMCLRegister.getStatement(s.substring(4));
		}
		return asin;
	}
}
