package com.github.nickid2018.smcl.statements;

import java.util.*;

import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.optimize.*;

public class MathStatement extends Statement {

	private List<Pair<Statement, Boolean>> subs = new ArrayList<>();

	public double calculate(VariableList list) {
		double t = 0;
		for (Pair<Statement, Boolean> en : getSubs()) {
			t += en.getValue() ? en.getKey().calculate(list) : -en.getKey().calculate(list);
		}
		return t;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (Pair<Statement, Boolean> en : getSubs()) {
			char a = en.getValue() ? '+' : '-';
			if (first) {
				first = false;
				if (!en.getValue())
					sb.append(a);
			} else {
				sb.append(a);
			}
			sb.append(en.getKey());
		}
		return sb.toString();
	}

	public boolean isAllNum() {
		for (Pair<Statement, Boolean> en : subs) {
			if (!(en.getKey() instanceof NumberStatement))
				return false;
		}
		return true;
	}

	@Override
	public void setValues(Statement... statements) {
		for (Statement statement : statements) {
			subs.add(new Pair<>(statement, Boolean.TRUE));
		}
	}

	@Override
	public void doOnFree() {
		for (Pair<Statement, Boolean> statement : subs) {
			statement.key.free();
		}
		subs.clear();
	}

	public List<Pair<Statement, Boolean>> getSubs() {
		return subs;
	}

	public static Statement format(String s, SMCL smcl) throws MathException {
		if (s.isEmpty())
			throw new MathException("Empty Statement", s, 0);

		MathStatement ms = smcl.obtain(MathStatement.class);
		ms.smcl = smcl;

		// Split
		int begin = 0;
		int intimes = 0;
		boolean plus = true;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '(')
				intimes++;
			else if (c == ')')
				intimes--;
			else if (c == '+' && intimes == 0) {
				String sub = s.substring(begin, i);
				begin = i + 1;
				if (i == 0)
					continue;
				if (i != 0) {
					Statement tmp = smcl.register.getStatement(sub);
					ms.subs.add(new Pair<>(tmp, plus));
				}
				plus = true;
			} else if (c == '-' && intimes == 0) {
				String sub = s.substring(begin, i);
				begin = i + 1;
				if (i != 0) {
					Statement tmp = smcl.register.getStatement(sub);
					ms.subs.add(new Pair<>(tmp, plus));
				}
				plus = false;
			}
			if (i == s.length() - 1) {
				if (intimes != 0) {
					throw new MathException("Parentheses are not paired", s, i);
				}
				String sub = s.substring(begin, s.length());
				Statement tmp = smcl.register.getStatement(sub);
				ms.subs.add(new Pair<>(tmp, plus));
			}
		}

		return smcl.settings.disableInitialOptimize ? ms : StatementOptimize.optimizeMathStatement(ms);
	}
}
