package com.github.nickid2018.smcl.optimize;

import java.util.*;

public class ShiftValue {

	public ShiftKey key;

	// The characters maps in expression
	public IntArray mappingChars = new IntArray();

	// The operator sorted priorities
	public IntArray priorityList = new IntArray();

	// The priorities at every character
	public IntArray priorities = new IntArray();

	public void clear() {
		mappingChars.clear();
		priorityList.clear();
		priorities.clear();
	}
	
	public void putNextOperator(int pos, char op) {
		mappingChars.putInt(pos);
	}

	public char getTopPriority(ShiftTable table) {
		return table.expr.charAt(mappingChars.get(priorityList.get(0)));
	}

	public String toString(ShiftTable table) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < mappingChars.length(); i++) {
			sb.append(table.expr.charAt(mappingChars.get(i)));
		}
		return sb.toString();
	}
}
