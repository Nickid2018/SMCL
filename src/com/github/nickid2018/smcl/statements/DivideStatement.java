package com.github.nickid2018.smcl.statements;

import java.util.*;

import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.functions.*;

public class DivideStatement extends Statement {

	private Statement first;
	private List<Statement> divs = new ArrayList<>();

	@Override
	public double calculate(VariableList list) {
		double ret = first.calculate(list);
		for (Statement ms : divs) {
			double v = ms.calculate(list);
			if (v == 0)
				throw new ArithmeticException("divide by 0");
			ret /= v;
		}
		return ret;
	}

	@Override
	public boolean isAllNum() {
		if (!(first instanceof NumberStatement))
			return false;
		for (Statement en : divs) {
			if (!(en instanceof NumberStatement))
				return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(first.toString());
		for (Statement en : divs) {
			if (!(en instanceof NumberStatement) && !(en instanceof Variable)
					&& !(en instanceof UnaryFunctionStatement))
				sb.append("/(" + en + ")");
			else
				sb.append("/" + en);
		}
		return sb + "";
	}

	@Override
	public Statement setValues(Statement... statements) {
		first = statements[0];
		for (int i = 1; i < statements.length; i++) {
			divs.add(statements[i]);
		}
		return this;
	}

	public DivideStatement addStatement(Statement statement) {
		divs.add(statement);
		return this;
	}

	@Override
	public void doOnFree() {
		first.free();
		for (Statement statement : divs) {
			statement.free();
		}
		first = null;
		divs.clear();
	}
}
