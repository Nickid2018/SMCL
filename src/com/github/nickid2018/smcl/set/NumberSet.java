package com.github.nickid2018.smcl.set;

public abstract class NumberSet {
	
	public abstract boolean isValid();
    public abstract boolean isInfinite();
	public abstract boolean isBelongTo(double value);
	public abstract boolean isInclude(NumberSet other);
	public abstract boolean isCross(NumberSet other);
	public abstract NumberSet getIntersection(NumberSet other);
}
