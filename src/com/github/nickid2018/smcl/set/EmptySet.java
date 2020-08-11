package com.github.nickid2018.smcl.set;

public class EmptySet extends NumberSet {

	public static final EmptySet EMPTY_SET = new EmptySet();
	
	private EmptySet() {
	}

	@Override
	public boolean isValid() {
		return true;
	}

    @Override
    public boolean isInfinite() {
        return false;
    }

	@Override
	public boolean isBelongTo(double value) {
		return false;
	}

	@Override
	public boolean isInclude(NumberSet other) {
		return other instanceof EmptySet;
	}

	@Override
	public boolean isCross(NumberSet other) {
		return other instanceof EmptySet;
	}

	@Override
	public NumberSet getIntersection(NumberSet other) {
		return other;
	}
}
