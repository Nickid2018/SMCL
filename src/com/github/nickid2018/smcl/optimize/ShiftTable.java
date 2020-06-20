package com.github.nickid2018.smcl.optimize;

import java.util.*;

import com.github.nickid2018.smcl.*;

public class ShiftTable {

	public Map<ShiftKey, ShiftValue> table = new HashMap<>();
	public String expr;
	public int length;

	public void fill(String expr) throws MathException {
		table.clear();
		this.expr = expr;
		int len = length = expr.length();
		int shift = internalFill(expr, 0, 0);
		if (shift < len)
			throw new MathException("Parentheses are not paired: Excess right brackets", expr, shift);
	}

	private int internalFill(String expr, int shift, int level) throws MathException {
		ShiftKey key = new ShiftKey();
		ShiftValue value = new ShiftValue();
		key.start = shift;
		key.level = level;
		for (; shift < expr.length(); shift++) {
			char now = expr.charAt(shift);
			if (now == '(') {
				shift = internalFill(expr, shift + 1, level + 1);
			} else {
				if (now == ')')
					break;
				value.putNextOperator(shift, now);
			}
		}
		if (shift == expr.length() && level != 0)
			throw new MathException("Parentheses are not paired: Missing right brackets", expr, shift);
		key.end = shift;
		value.key = key;
		table.put(key, value);
		return shift;
	}

	public int length() {
		return length;
	}
}
