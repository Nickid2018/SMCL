package com.github.nickid2018.smcl.set;

public class SingleSet extends Interval {

	public SingleSet(double number) {
		if (Double.isNaN(number))
			throw new IllegalArgumentException("Not a number!");
		leftRange = number;
		leftClose = true;
		rightRange = number;
		rightClose = true;
	}

	@Override
	public boolean isBelongTo(double value) {
		return leftRange == value;
	}

	@Override
	public boolean isCross(NumberSet other) {
		return other.isBelongTo(leftRange);
	}

	@Override
	public boolean isInclude(NumberSet other) {
		return other.isBelongTo(leftRange);
	}

	@Override
	public NumberSet getIntersection(NumberSet other) {
		return other.isBelongTo(leftRange) ? this : EmptySet.EMPTY_SET;
	}
}
