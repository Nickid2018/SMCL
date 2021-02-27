package com.github.nickid2018.smcl.statements.arith;

import java.util.*;
import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.functions.*;
import com.github.nickid2018.smcl.statements.*;

public class DivideStatement extends Statement {

	private Statement dividend;
	private List<Statement> divisors = new ArrayList<>();

	@Override
	public double calculateInternal(VariableList list) {
		double ret = dividend.calculate(list);
		for (Statement ms : divisors) {
			double v = ms.calculate(list);
			if (v == 0)
				throw new ArithmeticException("divide by 0");
			ret /= v;
		}
		return ret;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(dividend.toString());
		for (Statement en : divisors) {
			if (!(en instanceof NumberStatement) && !(en instanceof Variable)
					&& !(en instanceof UnaryFunctionStatement))
				sb.append("/(" + en + ")");
			else
				sb.append("/" + en);
		}
		return sb + "";
	}

	public DivideStatement addDivisor(Statement statement) {
		divisors.add(statement);
		return this;
	}

	public DivideStatement putDividendAndDivisors(Statement... statements) {
		dividend = statements[0];
		for (int i = 1; i < statements.length; i++) {
			addDivisor(statements[i]);
		}
		return this;
	}
}
