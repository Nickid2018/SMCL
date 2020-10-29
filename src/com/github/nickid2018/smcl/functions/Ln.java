package com.github.nickid2018.smcl.functions;

import com.github.nickid2018.smcl.*;

public class Ln extends UnaryFunctionStatement {

	public Ln(Statement statement) {
		super(statement);
	}

	@Override
	public double calculate(VariableList list) {
		double v = ms.calculate(list);
		if (v <= 0)
			throw new ArithmeticException("ln:argument is invalid-" + v);
		return Math.log(v);
	}

	@Override
	public String toString() {
		if (!ms.getClass().equals(Variable.class))
			return "ln(" + ms + ")";
		else
			return "ln" + ms;
	}
}
