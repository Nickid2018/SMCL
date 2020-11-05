package com.github.nickid2018.smcl.statements;

import java.util.*;

import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.functions.*;

public class MultiplyStatement extends Statement {

	private List<Statement> subs = new ArrayList<>();

	@Override
	public double calculateInternel(VariableList list) {
		double all = 1;
		for (Statement ms : subs) {
			all *= ms.calculate(list);
		}
		return all;
	}

	@Override
	public boolean isAllNum() {
		for (Statement en : subs) {
			if (!(en instanceof NumberStatement))
				return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (Statement ms : subs) {
			if (first)
				first = false;
			else
				sb.append("*");
			if (ms.getClass().equals(Variable.class)
					|| ms.getClass().getSuperclass().equals(UnaryFunctionStatement.class)
					|| ms instanceof NumberStatement)
				sb.append(ms);
			else
				sb.append("(" + ms + ")");
		}
		return sb.toString();
	}

	@Override
	public Statement setValues(Statement... statements) {
		for (Statement statement : statements) {
			subs.add(statement);
		}
		return this;
	}

	public MultiplyStatement addStatement(Statement statement) {
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
