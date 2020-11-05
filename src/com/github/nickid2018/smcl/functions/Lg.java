package com.github.nickid2018.smcl.functions;

import com.github.nickid2018.smcl.*;

public class Lg extends UnaryFunctionStatement {

	public Lg(Statement statement) {
		super(statement);
	}

	@Override
	public double calculateInternel(VariableList list) {
		double v = ms.calculate(list);
		if (v <= 0)
			throw new ArithmeticException("lg:argument is invalid-" + v);
		return Math.log10(v);
	}

	@Override
	public String toString() {
		if (!ms.getClass().equals(Variable.class))
			return "lg(" + ms + ")";
		else
			return "lg" + ms;
	}
}
