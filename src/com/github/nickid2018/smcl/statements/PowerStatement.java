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
			if (!(en instanceof NumberStatement) && !(en instanceof Variable) && !(en instanceof FunctionStatement))
				sb.append("^(" + en + ")");
			else
				sb.append("^" + en);
		}
		return sb + "";
	}

	@Override
	public void setValues(Statement... statements) {
		first = statements[0];
		for (int i = 1; i < statements.length; i++) {
			muls.add(statements[i]);
		}
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

	public static final PowerStatement format(String s, SMCL smcl) throws MathException {
		PowerStatement ms = smcl.obtain(PowerStatement.class);
		boolean a = true;
		int begin = 0;
		int intimes = 0;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '(')
				intimes++;
			else if (c == ')')
				intimes--;
			else if (c == '^' && intimes == 0) {
				String sub = s.substring(begin, i);
				begin = i + 1;
				if (i == 0)
					continue;
				if (i != 0) {
					Statement tmp = smcl.register.getStatement(sub);
					if (a) {
						ms.first = tmp;
						a = false;
					} else {
						ms.muls.add(tmp);
					}
				}
			}
			if (i == s.length() - 1) {
				if (intimes != 0) {
					throw new MathException("Parentheses are not paired", s, i);
				}
				String sub = s.substring(begin, s.length());
				Statement tmp = smcl.register.getStatement(sub);
				ms.muls.add(tmp);
			}
		}
		return ms;
	}
}
