package com.github.nickid2018.smcl.statements;

import java.util.*;

import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.functions.*;

public class MultiplyStatement extends Statement {

	private List<Statement> subs = new ArrayList<>();

	@Override
	public double calculate(VariableList list) {
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
			if (ms.getClass().equals(Variable.class) || ms.getClass().getSuperclass().equals(FunctionStatement.class)
					|| ms instanceof NumberStatement)
				sb.append(ms);
			else
				sb.append("(" + ms + ")");
		}
		return sb.toString();
	}

	@Override
	public void setValues(Statement... statements) {
		for (Statement statement : statements) {
			subs.add(statement);
		}
	}

	@Override
	public void doOnFree() {
		for (Statement statement : subs) {
			statement.free();
		}
		subs.clear();
	}

	public static final MultiplyStatement format(String s, SMCL jmcl) throws MathException {
		MultiplyStatement ms = jmcl.obtain(MultiplyStatement.class);
		int begin = 0;
		int intimes = 0;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '(')
				intimes++;
			else if (c == ')')
				intimes--;
			else if (c == '*' && intimes == 0) {
				String sub = s.substring(begin, i);
				begin = i + 1;
				if (i == 0)
					continue;
				if (i != 0) {
					Statement tmp = SMCLRegister.getStatement(sub, jmcl);
					ms.subs.add(tmp);
				}
			}
			if (i == s.length() - 1) {
				if (intimes != 0) {
					throw new MathException("Parentheses are not paired", s, i);
				}
				String sub = s.substring(begin, s.length());
				Statement tmp = SMCLRegister.getStatement(sub, jmcl);
				ms.subs.add(tmp);
			}
		}
		Set<MultiplyStatement> mss = new HashSet<>();
		for (Statement en : ms.subs) {
			if (en.getClass().equals(MultiplyStatement.class)) {
				mss.add((MultiplyStatement) en);
			}
		}
		if (mss.size() > 0) {
			for (MultiplyStatement n : mss) {
				ms.subs.remove(n);
				ms.subs.addAll(n.subs);
			}
		}
		return ms;
	}
}
