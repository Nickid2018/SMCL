package com.github.nickid2018.smcl;

import java.util.*;

public class GlobalVariables {

	private final Map<String, Variable> variablemap = new HashMap<>();

	public void registerVariable(String var) {
		variablemap.put(var, new Variable(var));
	}

	public void unregisterVariable(String var) {
		variablemap.remove(var);
	}

	public boolean haveVariable(String var) {
		return variablemap.containsKey(var);
	}

	public Variable getVariable(String var) {
		return variablemap.get(var);
	}
}
