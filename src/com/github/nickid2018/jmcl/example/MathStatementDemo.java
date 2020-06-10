package com.github.nickid2018.jmcl.example;

import com.github.nickid2018.jmcl.*;
import com.github.nickid2018.jmcl.optimize.*;

public class MathStatementDemo {

	public static void main(String[] args) throws Exception {
		JMCL.init();
		JMCL.optimize = false;
		GlobalVariables.registerVariable("x");
		Statement ms = Statement.format("ln(x)/ln2");
		VariableList variable = new VariableList();
		variable.addVariableValue("x", 1024);
		System.out.println(ms.calculate(variable));
		System.out.println(ms);
		ShiftTable table = new ShiftTable();
		String a;
		table.fill(a = "x＋(x-1+(x)*(y-1))");
		System.out.println(a);
		for(int i:table.shiftLevels) {
			System.out.println(i);
		}
		for(ShiftKey key:table.table.keySet()){
			System.out.println(key);
		}
	}
}
