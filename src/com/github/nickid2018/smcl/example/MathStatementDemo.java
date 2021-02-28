package com.github.nickid2018.smcl.example;

import com.github.nickid2018.smcl.*;

public class MathStatementDemo {

	static long timer;

	public static void main(String[] args) throws Exception {
		SMCL smcl = SMCL.getInstance();
		smcl.init();
		smcl.globalvars.registerVariables("x");
		smcl.settings.degreeAngle = true;
		timer = System.nanoTime();
		Statement s = smcl.format("tan(x)");
		timeOutput();
		System.out.println(s);
		timeOutput();
		System.out.println(s.calculate(new VariableList().addVariableValue("x", 89.99999999999999)));
		timeOutput();
	}

	static void timeOutput() {
		long time = System.nanoTime();
		System.out.println((time - timer) / 1000_000D);
		timer = System.nanoTime();
	}
}
