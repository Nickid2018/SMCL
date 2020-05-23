package com.github.nickid2018.jmcl.example;

import com.github.nickid2018.jmcl.*;

public class MathStatementDemo {

	public static void main(String[] args) throws Exception {
		JMCL.init();
		Variables.registerVariable("x");
		Statement ms = Statement.format("(x+1)^2+2*(x+1)+1");
		VariableList variable = new VariableList();
		variable.addVariableValue("x", 3);
		System.out.println(ms.calc(variable));
		System.out.println(ms);
	}
}
