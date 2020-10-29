package com.github.nickid2018.smcl.functions;

import com.github.nickid2018.smcl.*;

public class Sin extends UnaryFunctionStatement {

	public Sin(Statement statement) {
		super(statement);
	}

	@Override
	public double calculate(VariableList list) {
		return smcl.settings.degreeAngle ? Math.sin(Math.toRadians(ms.calculate(list))) : Math.sin(ms.calculate(list));
	}

	@Override
	public String toString() {
		if (!ms.getClass().equals(Variable.class))
			return "sin(" + ms + ")";
		else
			return "sin" + ms;
	}
}
