package com.github.nickid2018.smcl.example;

import com.github.nickid2018.smcl.*;

public class DerivativeDemo {

	static long timer;

	public static void main(String[] args) throws Exception {
		SMCL smcl = SMCL.getInstance();
		smcl.init();
		smcl.globalvars.registerVariables("x");
		smcl.settings.degreeAngle = true;
		Statement s = smcl.format("sin(x)*cos(2*x)*cos(x)");
		s = smcl.format("sin(x)*cos(2*x)*cos(x)");
		s = smcl.format("sin(x)*cos(2*x)*cos(x)");
		s = smcl.format("sin(x)*cos(2*x)*cos(x)");
		s = smcl.format("sin(x)*cos(2*x)*cos(x)");
		s = smcl.format("sin(x)*cos(2*x)*cos(x)");
		s = smcl.format("sin(x)*cos(2*x)*cos(x)");
		timer = System.nanoTime();
		s = smcl.format("sin(x)*cos(2*x)*cos(x)");
		timeOutput();
		s.toString();
		s.toString();
		s.toString();
		s.toString();
		s.toString();
		s.toString();
		timer = System.nanoTime();
		System.out.println(s);
		timeOutput();
		Statement d = s.derivative();
		d = s.derivative();
		d = s.derivative();
		d = s.derivative();
		d = s.derivative();
		d = s.derivative();
		d = s.derivative();
		d = s.derivative();
		d = s.derivative();
		timer = System.nanoTime();
		d = s.derivative();
		timeOutput();
		d.toString();
		d.toString();
		d.toString();
		d.toString();
		d.toString();
		timer = System.nanoTime();
		System.out.println(d);
		timeOutput();
	}

	static void timeOutput() {
		long time = System.nanoTime();
		System.out.println((time - timer) / 1000_000D);
		timer = System.nanoTime();
	}
}
