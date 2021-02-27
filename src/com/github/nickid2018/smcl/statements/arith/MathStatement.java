package com.github.nickid2018.smcl.statements.arith;

import java.util.*;
import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.statements.*;

public class MathStatement extends Statement {

	private List<Statement> subs = new ArrayList<>();

	public double calculateInternal(VariableList list) {
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
				if (en.isNegative() && !(en instanceof NumberStatement))
					sb.append("-");
				sb.append(en.toString());
				continue;
			}
			sb.append(((en instanceof NumberStatement && en.isNegative()) ? "" : (en.isNegative() ? "-" : "+"))
					+ en.toString());
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

	public MathStatement addStatement(Statement statement) {
		subs.add(statement);
		return this;
	}

	public MathStatement addStatements(Statement... statements) {
		for (Statement statement : statements)
			addStatement(statement);
		return this;
	}
}
