package com.github.nickid2018.smcl.statements;

import java.util.*;
import com.github.nickid2018.smcl.*;

public class MathStatement extends Statement {

	private List<Statement> subs = new ArrayList<>();

	public double calculate(VariableList list) {
		double t = 0;
		for (Statement en : subs) {
			t += en.calculate(list);
		}
		return t;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (Statement en : subs) {
			if (first) {
				first = false;
				if (en.isNegative())
					sb.append("-");
				sb.append(en.toString());
				continue;
			}
			sb.append((en.isNegative() ? "-" : "+") + en.toString());
		}
		return sb.toString();
	}

	public boolean isAllNum() {
		for (Statement en : subs) {
			if (!(en instanceof NumberStatement))
				return false;
		}
		return true;
	}

	@Override
	public Statement setValues(Statement... statements) {
		for (Statement statement : statements) {
			subs.add(statement);
		}
		return this;
	}

	public MathStatement addStatement(Statement statement) {
		subs.add(statement);
		return this;
	}

	@Override
	public void doOnFree() {
		for (Statement statement : subs) {
			statement.free();
		}
		subs.clear();
	}
}
