package com.github.nickid2018.smcl.statements.arith;

import java.util.*;
import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.functions.*;
import com.github.nickid2018.smcl.statements.*;

public class PowerStatement extends Statement {

	private Statement base;
	private List<Statement> exponents = new ArrayList<>();

	@Override
	public double calculateInternal(VariableList list) {
		double ret = base.calculate(list);
		for (Statement ms : exponents) {
			double mul = ms.calculate(list);
			ret = Math.pow(ret, mul);
		}
		return ret;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (!(base instanceof NumberStatement) && !(base instanceof Variable))
			sb.append("(" + base + ")");
		else
			sb.append(base);
		for (Statement en : exponents) {
			if (!(en instanceof NumberStatement) && !(en instanceof Variable)
					&& !(en instanceof UnaryFunctionStatement))
				sb.append("^(" + en + ")");
			else
				sb.append("^" + en);
		}
		return sb + "";
	}

	public PowerStatement addExponent(Statement statement) {
		exponents.add(statement);
		return this;
	}

	public PowerStatement putBaseAndExponents(Statement... statements) {
		base = statements[0];
		for (int i = 1; i < statements.length; i++) {
			addExponent(statements[i]);
		}
		return this;
	}
}
