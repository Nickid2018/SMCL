package com.github.nickid2018.smcl.statements.arith;

import java.util.*;
import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.functions.*;
import com.github.nickid2018.smcl.statements.*;

public class MultiplyStatement extends Statement {

	private List<Statement> multipliers = new ArrayList<>();

	@Override
	public double calculateInternal(VariableList list) {
		double all = 1;
		for (Statement ms : multipliers) {
			all *= ms.calculate(list);
		}
		return all;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (Statement ms : multipliers) {
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

	public MultiplyStatement addMultiplier(Statement statement) {
		multipliers.add(statement);
		return this;
	}

	public MultiplyStatement addMultipliers(Statement... statements) {
		for (Statement statement : statements)
			addMultiplier(statement);
		return this;
	}
}
