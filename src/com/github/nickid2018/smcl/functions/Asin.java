package com.github.nickid2018.smcl.functions;

import com.github.nickid2018.smcl.*;

public class Asin extends UnaryFunctionStatement {

	public Asin(Statement statement) {
		super(statement);
	}

	@Override
	public double calculateInternel(VariableList list) {
		return smcl.settings.degreeAngle ? Math.toDegrees(Math.asin(ms.calculate(list)))
				: Math.asin(ms.calculate(list));
	}

	@Override
	public String toString() {
		if (!ms.getClass().equals(Variable.class))
			return "asin(" + ms + ")";
		else
			return "asin" + ms;
	}
}
