package com.github.nickid2018.smcl.statements;

import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.optimize.*;

public class Variable extends Statement {

	private String name;
	private Variable negativeVar;

	public Variable(String s) {
		super(null, DefinedVariables.EMPTY_VARIABLES);
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
		return (isNegative ? "-" : "") + name;
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

	@Override
	public Statement getNewNegative() {
		return getNegative();
	}

	@Override
	protected Statement derivativeInternal() {
		return NumberPool.NUMBER_CONST_1;
	}
}
