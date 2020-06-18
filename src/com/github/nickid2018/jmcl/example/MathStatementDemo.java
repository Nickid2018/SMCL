package com.github.nickid2018.jmcl.example;

import com.github.nickid2018.jmcl.optimize.*;

public class MathStatementDemo {

	public static void main(String[] args) throws Exception {
		ShiftTable table = new ShiftTable();
		String a;
		table.fill(a = "x*(x-1+(x)*(y-1))-0");
		System.out.println(a);
		for (ShiftKey key : table.table.keySet()) {
			System.out.println(key + " " + table.table.get(key));
		}
	}
}
