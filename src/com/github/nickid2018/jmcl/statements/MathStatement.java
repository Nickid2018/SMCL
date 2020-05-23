package com.github.nickid2018.jmcl.statements;

import java.util.*;
import com.github.nickid2018.jmcl.*;
import com.github.nickid2018.jmcl.optimize.*;

public class MathStatement extends Statement {

	private List<Pair<Statement, Boolean>> subs = new ArrayList<>();

	public double calc(VariableList list) {
		double t = 0;
		for (Pair<Statement, Boolean> en : subs) {
			t += en.getValue() ? en.getKey().calc(list) : -en.getKey().calc(list);
		}
		return t;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (Pair<Statement, Boolean> en : subs) {
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

	public static Statement format(String s) throws MathException {
		if (s.isEmpty())
			throw new MathException("Empty Statement", s, 0);

		MathStatement ms = JMCL.obtain(MathStatement.class);

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
					Statement tmp = JMCLRegister.getStatement(sub);
					ms.subs.add(new Pair<>(tmp, plus));
				}
				plus = true;
			} else if (c == '-' && intimes == 0) {
				String sub = s.substring(begin, i);
				begin = i + 1;
				if (i != 0) {
					Statement tmp = JMCLRegister.getStatement(sub);
					ms.subs.add(new Pair<>(tmp, plus));
				}
				plus = false;
			}
			if (i == s.length() - 1) {
				if (intimes != 0) {
					throw new MathException("Parentheses are not paired", s, i);
				}
				String sub = s.substring(begin, s.length());
				Statement tmp = JMCLRegister.getStatement(sub);
				ms.subs.add(new Pair<>(tmp, plus));
			}
		}

		// Shorten Distance
		if (ms.subs.size() == 1 && ms.subs.get(0).value) {
			Statement statement = ms.subs.get(0).key;
			ms.free();
			return statement;
		}

		// Number-Onlys
		if (ms.isAllNum())
			return NumberPool.getNumber(ms.calc(JMCL.EMPTY_ARGS));

		// Merge Math Statements
		Set<Pair<Statement, Boolean>> mss = new HashSet<>();
		for (Pair<Statement, Boolean> en : ms.subs) {
			if (en.getKey().getClass().equals(MathStatement.class)) {
				mss.add(en);
			}
		}
		if (mss.size() > 0) {
			for (Pair<Statement, Boolean> n : mss) {
				ms.subs.remove(n);
				ms.subs.addAll(((MathStatement) (n.key)).subs);
			}
		}

		// Merge Numbers
		double all = 0;
		Set<Pair<Statement, Boolean>> set = new HashSet<>();
		for (Pair<Statement, Boolean> en : ms.subs) {
			if (en.getKey() instanceof NumberStatement) {
				set.add(en);
				if (en.getValue())
					all += en.getKey().calc(null);
				else
					all -= en.getKey().calc(null);
			}
		}
		if (set.size() > 1) {
			for (Pair<Statement, Boolean> n : set) {
				ms.subs.remove(n);
			}
			if (all != 0) {
				NumberStatement num = NumberPool.getNumber(Math.abs(all));
				ms.subs.add(new Pair<>(num, all > 0));
			}
		}

		return ms;
	}
}
