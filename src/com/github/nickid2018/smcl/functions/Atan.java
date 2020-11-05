package com.github.nickid2018.smcl.functions;

import com.github.nickid2018.smcl.*;

public class Atan extends UnaryFunctionStatement {

	public Atan(Statement statement) {
		super(statement);
	}

	@Override
	public double calculateInternel(VariableList list) {
		return smcl.settings.degreeAngle ? Math.toDegrees(Math.atan(ms.calculate(list)))
				: Math.atan(ms.calculate(list));
	}

	@Override
	public String toString() {
		if (!ms.getClass().equals(Variable.class))
			return "atan(" + ms + ")";
		else
			return "atan" + ms;
	}
}
