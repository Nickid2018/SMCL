package com.github.nickid2018.smcl.set;

import java.util.*;

public class SimpleSet extends NumberSet {

	private final Set<Double> set;

	public SimpleSet() {
		set = new HashSet<>();
	}

	public void add(double value) {
		set.add(value);
	}

	public void remove(double value) {
		set.remove(value);
	}

	public int size() {
		return set.size();
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
		return set.contains(value);
	}

	@Override
	public boolean isCross(NumberSet other) {
		// TODO: Implement this method
		return false;
	}

	@Override
	public boolean isInclude(NumberSet other) {
		return false;
	}

	@Override
	public NumberSet getIntersection(NumberSet other) {
		// TODO: Implement this method
		return null;
	}

}
