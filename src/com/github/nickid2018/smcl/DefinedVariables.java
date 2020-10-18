package com.github.nickid2018.smcl;

import java.util.*;

public class DefinedVariables {

	public static final DefinedVariables EMPTY_VARIABLES = new DefinedVariables();

	private static final Map<String, Variable> sharedVariables = new HashMap<>();
	private Set<String> variables;

	public DefinedVariables() {
		variables = new HashSet<>();
	}

	public void register(String var) {
		variables.add(var);
		if (!sharedVariables.containsKey(var))
			sharedVariables.put(var, new Variable(var));
	}

	public void unregister(String var) {
		variables.remove(var);
	}

	public boolean haveVariables(String var) {
		return variables.contains(var);
	}

	public Variable getVariable(String var) {
		return sharedVariables.get(var);
	}
}
