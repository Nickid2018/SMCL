package com.github.nickid2018.smcl.statements;

import java.util.*;
import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.functions.*;

public class PowerStatement extends Statement {

	private Statement first;
	private Set<Statement> muls = new HashSet<>();

	@Override
	public double calculate(VariableList list) {
		double ret = first.calculate(list);
		for (Statement ms : muls) {
			double mul = ms.calculate(list);
			ret = Math.pow(ret, mul);
		}
		return ret;
	}

	@Override
	public boolean isAllNum() {
		if (!(first instanceof NumberStatement))
			return false;
		for (Statement en : muls) {
			if (!(en instanceof NumberStatement))
				return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (!(first instanceof NumberStatement) && !(first instanceof Variable))
			sb.append("(" + first + ")");
		else
			sb.append(first);
		for (Statement en : muls) {
			if (!(en instanceof NumberStatement) && !(en instanceof Variable)
					&& !(en instanceof UnaryFunctionStatement))
				sb.append("^(" + en + ")");
			else
				sb.append("^" + en);
		}
		return sb + "";
	}

	@Override
	public Statement setValues(Statement... statements) {
		first = statements[0];
		for (int i = 1; i < statements.length; i++) {
			muls.add(statements[i]);
		}
		return this;
	}

	public PowerStatement addStatement(Statement statement) {
		muls.add(statement);
		return this;
	}

	@Override
	public void doOnFree() {
		first.free();
		for (Statement statement : muls) {
			statement.free();
		}
		first = null;
		muls.clear();
	}
}
