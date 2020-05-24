package com.github.nickid2018.jmcl;

public class Variable extends Statement {

	private String name;

	public Variable(String s) {
		name = s;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public double calculate(VariableList list) {
		return list.getVariableValue(name);
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean canClose() {
		// Avoid being free
		return false;
	}

	@Override
	public boolean isAllNum() {
		return false;
	}

	@Override
	public void setValues(Statement... statements) {
	}
}
