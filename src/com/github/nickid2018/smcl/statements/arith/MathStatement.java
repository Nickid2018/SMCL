package com.github.nickid2018.smcl.statements.arith;

import java.util.*;
import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.optimize.*;
import com.github.nickid2018.smcl.statements.*;

public class MathStatement extends Statement {

	public MathStatement(SMCL smcl, DefinedVariables variables) {
		super(smcl, variables);
	}

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
				if (en.isNegative() && !(en instanceof NumberStatement || en instanceof Variable))
					sb.append("-");
				sb.append(en.toString());
				continue;
			}
			sb.append((((en instanceof NumberStatement || en instanceof Variable) && en.isNegative()) ? ""
					: (en.isNegative() ? "-" : "+")) + en.toString());
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

	@Override
	protected Statement derivativeInternal() {
		MathStatement end = new MathStatement(smcl, variables);
		for (Statement s : subs) {
			Statement deri = s.derivative();
			if (deri.equals(NumberPool.NUMBER_CONST_0))
				continue;
			if (deri instanceof MathStatement)
				end.addStatements(((MathStatement) deri).subs.toArray(new Statement[0]));
			else
				end.addStatement(deri);
		}
		return end.subs.size() > 0 ? (end.isAllNum() ? NumberPool.getNumber(end.calculate(null)) : end)
				: NumberPool.NUMBER_CONST_0;
	}
}
