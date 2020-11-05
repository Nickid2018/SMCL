package com.github.nickid2018.smcl.functions;

import com.github.nickid2018.smcl.*;

public class Acos extends UnaryFunctionStatement {

	public Acos(Statement statement) {
		super(statement);
	}

	@Override
	public double calculateInternel(VariableList list) {
		return smcl.settings.degreeAngle ? Math.toDegrees(Math.acos(ms.calculate(list)))
				: Math.acos(ms.calculate(list));
	}

	@Override
	public String toString() {
		if (!ms.getClass().equals(Variable.class))
			return "acos(" + ms + ")";
		else
			return "acos" + ms;
	}
}
