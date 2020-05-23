package com.github.nickid2018.jmcl;

import java.util.*;

public class NumberStatement extends MathStatement {

	private double num;

	public NumberStatement(double num) {
		this.num = num;
	}

	public double getNumber() {
		return num;
	}

	public void setNumber(double num) {
		this.num = num;
	}

	@Override
	public double calc(Map<String, Double> values) {
		return num;
	}

	@Override
	public String toString() {
		return num + "";
	}
}
