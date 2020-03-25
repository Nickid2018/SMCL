package com.github.nickid2018.jmcl;

import java.util.*;

import com.github.nickid2018.jmcl.Number;

public class MathStatement {

	private Map<MathStatement, Boolean> subs = new HashMap<>();

	public double calc(Map<String, Double> values) {
		double t = 0;
		for (Map.Entry<MathStatement, Boolean> en : subs.entrySet()) {
			t += en.getValue() ? en.getKey().calc(values) : -en.getKey().calc(values);
		}
		return t;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (Map.Entry<MathStatement, Boolean> en : subs.entrySet()) {
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
		for (Map.Entry<MathStatement, Boolean> en : subs.entrySet()) {
			if (!(en.getKey() instanceof Number))
				return false;
		}
		return true;
	}

	public static MathStatement format(String s) throws MathException {
		if (s.isEmpty())
			throw new MathException("Empty Statement", s, 0);
		MathStatement ms = new MathStatement();
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
					MathStatement tmp = JMCLRegister.getStatement(sub);
					ms.subs.put(tmp, plus);
				}
				plus = true;
			} else if (c == '-' && intimes == 0) {
				String sub = s.substring(begin, i);
				begin = i + 1;
				if (i != 0) {
					MathStatement tmp = JMCLRegister.getStatement(sub);
					ms.subs.put(tmp, plus);
				}
				plus = false;
			}
			if (i == s.length() - 1) {
				if (intimes != 0) {
					throw new MathException("Parentheses are not paired", s, i);
				}
				String sub = s.substring(begin, s.length());
				MathStatement tmp = JMCLRegister.getStatement(sub);
				ms.subs.put(tmp, plus);
			}
		}
		if (ms.subs.size() == 1 && ms.subs.containsValue(true))
			return (MathStatement) ms.subs.keySet().toArray()[0];
		if (ms.isAllNum())
			ms = new Number(ms.calc(new HashMap<>()));
		Set<MathStatement> mss = new HashSet<>();
		for (Map.Entry<MathStatement, Boolean> en : ms.subs.entrySet()) {
			if (en.getKey().getClass().equals(MathStatement.class)) {
				mss.add((MathStatement) en.getKey());
			}
		}
		if (mss.size() > 0) {
			for (MathStatement n : mss) {
				ms.subs.remove(n);
				ms.subs.putAll(n.subs);
			}
		}
		double all = 0;
		Set<Number> set = new HashSet<>();
		for (Map.Entry<MathStatement, Boolean> en : ms.subs.entrySet()) {
			if (en.getKey() instanceof Number) {
				set.add((Number) en.getKey());
				if (en.getValue())
					all += en.getKey().calc(null);
				else
					all -= en.getKey().calc(null);
			}
		}
		if (set.size() > 1) {
			for (Number n : set) {
				ms.subs.remove(n);
			}
			if (all != 0) {
				Number num = new Number(Math.abs(all));
				ms.subs.put(num, all > 0);
			}
		}
		return ms;
	}
}
