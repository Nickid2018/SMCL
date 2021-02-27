package com.github.nickid2018.smcl;

public class CachedStatement extends Statement {

	private Statement statement;
	private VariableList list = new VariableList();
	private double result;
	private boolean dirty = true;

	public CachedStatement(Statement statement) {
		this.statement = statement;
	}

	@Override
	public String toString() {
		return statement.toString();
	}

	public void setVariable(String var, double v) {
		dirty = true;
		list.addVariableValue(var, v);
	}

	public double getVariable(String var) {
		return list.getVariableValue(var);
	}

	public double calculate() {
		if (dirty) {
			result = calculateInternal(list);
			dirty = false;
		}
		return result;
	}

	@Override
	public double calculateInternal(VariableList list) {
		return statement.calculateInternal(list);
	}
}
