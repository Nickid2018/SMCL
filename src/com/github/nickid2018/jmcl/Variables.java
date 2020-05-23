package com.github.nickid2018.jmcl;

import java.util.*;

public class Variables {

	private static final Map<String, Variable> variablemap = new HashMap<>();

	public static void registerVariable(String var) {
		variablemap.put(var, new Variable(var));
	}

	public static void unregisterVariable(String var) {
		variablemap.remove(var);
	}

	public static boolean haveVariable(String var) {
		return variablemap.containsKey(var);
	}

	public static Variable getVariable(String var) {
		return variablemap.get(var);
	}
}
