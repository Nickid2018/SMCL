package com.github.nickid2018.smcl.functions;

import com.github.nickid2018.smcl.*;

public class Tan extends UnaryFunctionStatement {

	public Tan(Statement statement) {
		super(statement);
	}

	@Override
	public double calculateInternel(VariableList list) {
		double v = ms.calculate(list);
		if (v % Math.PI == Math.PI / 2 && !smcl.settings.degreeAngle)
			throw new ArithmeticException("tan:argument is invalid-" + v);
		if (v % 180 == 90 && smcl.settings.degreeAngle)
			throw new ArithmeticException("tan:argument is invalid-" + v);
		return smcl.settings.degreeAngle ? Math.tan(Math.toRadians(v)) : Math.tan(v);
	}

	@Override
	public String toString() {
		if (!ms.getClass().equals(Variable.class))
			return "tan(" + ms + ")";
		else
			return "tan" + ms;
	}
}
