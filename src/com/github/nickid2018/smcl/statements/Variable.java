package com.github.nickid2018.smcl.statements;

import com.github.nickid2018.smcl.Statement;
import com.github.nickid2018.smcl.VariableList;

public class Variable extends Statement {

	private String name;
	private Variable negativeVar;

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
	public double calculateInternal(VariableList list) {
		return list.getVariableValue(name);
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public Statement getNegative() {
		if (negativeVar == null) {
			negativeVar = new Variable(name);
			negativeVar.isNegative = true;
			negativeVar.negativeVar = this;
		}
		return negativeVar;
	}
}
