package com.github.nickid2018.jmcl.example;

import com.github.nickid2018.jmcl.*;

public class CachedStatementDemo {

	public static void main(String[] args) throws MathException {
		JMCL.init();
		Variables.registerVariable("x");
		long now = System.nanoTime();
		Statement ms = Statement.format("(x+1)^2");
		CachedStatement statement = new CachedStatement(ms);
		System.out.println(-(now - (now = System.nanoTime()))/1000_000D);
		statement.setVariable("x", 1000);
		System.out.println(-(now - (now = System.nanoTime()))/1000_000D);
		statement.calculate();
		System.out.println(-(now - (now = System.nanoTime()))/1000_000D);
		statement.calculate();
		System.out.println(-(now - (now = System.nanoTime()))/1000_000D);
		statement.close();
	}

}
