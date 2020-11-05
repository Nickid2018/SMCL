package com.github.nickid2018.smcl;

import java.util.*;

public class VariableList {

	private Map<String, Double> value = new HashMap<>();

	public final VariableList addVariableValue(String var, double v) {
		value.put(var, v);
		return this;
	}

	public final double getVariableValue(String var) {
		if (!value.containsKey(var))
			throw new ArithmeticException("Variable \"" + var + "\" is not declared in list");
		return value.get(var);
	}

	public final boolean containsValue(String var) {
		return value.containsKey(var);
	}

	public final VariableList changeValue(String var, double v) {
		value.put(var, v);
		return this;
	}
}
