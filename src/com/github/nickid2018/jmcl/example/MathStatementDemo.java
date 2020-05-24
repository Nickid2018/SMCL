package com.github.nickid2018.jmcl.example;

import com.github.nickid2018.jmcl.*;

public class MathStatementDemo {

	public static void main(String[] args) throws Exception {
		JMCL.init();
		Variables.registerVariable("x");
		Statement ms = Statement.format("sinx*x");
		VariableList variable = new VariableList();
		variable.addVariableValue("x", 1024);
		System.out.println(ms.calculate(variable));
		System.out.println(ms);
	}
}
