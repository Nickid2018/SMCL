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
			if (!(en instanceof NumberStatement) && !(en instanceof Variable) && !(en instanceof FunctionStatement))
				sb.append("/(" + en + ")");
			else
				sb.append("/" + en);
		}
		return sb + "";
	}

	@Override
	public void setValues(Statement... statements) {
		first = statements[0];
		for (int i = 1; i < statements.length; i++) {
			divs.add(statements[i]);
		}
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

	public static final DivideStatement format(String s, SMCL smcl) throws MathException {
		DivideStatement ms = smcl.obtain(DivideStatement.class);
		boolean a = true;
		int begin = 0;
		int intimes = 0;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '(')
				intimes++;
			else if (c == ')')
				intimes--;
			else if (c == '/' && intimes == 0) {
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
						ms.divs.add(tmp);
					}
				}
			}
			if (i == s.length() - 1) {
				if (intimes != 0) {
					throw new MathException("Parentheses are not paired", s, i);
				}
				String sub = s.substring(begin, s.length());
				Statement tmp = smcl.register.getStatement(sub);
				ms.divs.add(tmp);
			}
		}
		return ms;
	}
}
