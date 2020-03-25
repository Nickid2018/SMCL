package com.github.nickid2018.jmcl;

import java.util.*;

public class Variable extends MathStatement {
	
	private String name;

	public Variable(String s) {
		name=s;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public double calc(Map<String, Double> values) {
		double v=0;
		try {
			v = values.get(name);
		} catch (Exception e) {
			return 0;
		}
		return v;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
