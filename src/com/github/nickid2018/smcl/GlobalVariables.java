package com.github.nickid2018.smcl;

import java.util.*;
import com.github.nickid2018.smcl.statements.*;

public class GlobalVariables {

	private final Map<String, Variable> variablemap = new HashMap<>();

	public GlobalVariables registerVariable(String var) {
		variablemap.put(var, new Variable(var));
		return this;
	}

	public GlobalVariables registerVariables(String... vars) {
		for (String var : vars)
			variablemap.put(var, new Variable(var));
		return this;
	}

	public GlobalVariables unregisterVariable(String var) {
		variablemap.remove(var);
		return this;
	}

	public boolean haveVariable(String var) {
		return variablemap.containsKey(var);
	}

	public Variable getVariable(String var) {
		return variablemap.get(var);
	}

	public Set<String> getAllRegistered() {
		return variablemap.keySet();
	}

	public DefinedVariables toDefinedVariables() {
		return new DefinedVariables().registerAll(getAllRegistered());
	}
}
