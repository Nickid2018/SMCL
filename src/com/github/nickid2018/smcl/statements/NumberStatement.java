package com.github.nickid2018.smcl.statements;

import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.optimize.NumberPool;

public class NumberStatement extends Statement {

	private double num;

	public NumberStatement(double num) {
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
	public double calculateInternel(VariableList list) {
		return num;
	}

	@Override
	public String toString() {
		return num + "";
	}

	@Override
	public boolean isAllNum() {
		// Avoid Roll Action
		return false;
	}

	@Override
	public boolean canClose() {
		// Avoid being free
		return false;
	}

	@Override
	public Statement getNegative() {
		return NumberPool.getNumber(-num);
	}

	@Override
	public Statement setValues(Statement... statements) {
		return this;
	}
}
