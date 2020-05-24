package com.github.nickid2018.jmcl;

import java.util.*;

public class VariableList {

	private Map<String, Double> value = new HashMap<>();

	public final void addVariableValue(String var, double v) {
		value.put(var, v);
	}

	public final double getVariableValue(String var) {
		return value.get(var);
	}

	public final boolean containsValue(String var) {
		return value.containsKey(var);
	}

	public final void changeValue(String var, double v) {
		value.put(var, v);
	}
}
