package com.github.nickid2018.smcl.statements;

import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.optimize.*;

public class NumberStatement extends Statement {

	private double num;

	public NumberStatement(double num) {
		super(null, DefinedVariables.EMPTY_VARIABLES);
		this.num = num;
		if (num < 0)
			isNegative = true;
	}

	public double getNumber() {
		return num;
	}

	public void setNumber(double num) {
		this.num = num;
	}

	@Override
	public double calculate(VariableList list) {
		return num;
	}

	@Override
	protected double calculateInternal(VariableList list) {
		return Math.abs(num);
	}

	@Override
	public String toString() {
		return Double.toString(num);
	}

	@Override
	public Statement getNegative() {
		if (num == 0)
			return this;
		return NumberPool.getNumber(-num);
	}

	@Override
	public Statement getNewNegative() {
		return getNegative();
	}

	@Override
	protected Statement derivativeInternal() {
		return NumberPool.NUMBER_CONST_0;
	}
}
