package com.github.nickid2018.smcl.functions;

import com.github.nickid2018.smcl.*;

public class Cos extends UnaryFunctionStatement {

	public Cos(Statement statement) {
		super(statement);
	}

	@Override
	public double calculate(VariableList list) {
		return smcl.settings.degreeAngle ? Math.cos(Math.toRadians(ms.calculate(list))) : Math.cos(ms.calculate(list));
	}

	@Override
	public String toString() {
		if (!ms.getClass().equals(Variable.class))
			return "cos(" + ms + ")";
		else
			return "cos" + ms;
	}
}
