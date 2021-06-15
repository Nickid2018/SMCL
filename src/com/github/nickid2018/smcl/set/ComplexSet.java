package com.github.nickid2018.smcl.set;

import java.util.*;

public class ComplexSet extends NumberSet {

	private Set<NumberSet> subSets = new HashSet<>();

	public ComplexSet putSet(NumberSet set) {
		if (set instanceof ComplexSet)
			subSets.addAll(((ComplexSet) set).subSets);
		else if (set instanceof EmptySet)
			return this;
		else
			subSets.add(set);
		return this;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public boolean isInfinite() {
		for (NumberSet set : subSets)
			if (set.isInfinite())
				return true;
		return false;
	}

	@Override
	public boolean isBelongTo(double value) {
		for (NumberSet set : subSets)
			if (set.isBelongTo(value))
				return true;
		return false;
	}

	@Override
	@Deprecated
	public boolean isInclude(NumberSet other) {
		for (NumberSet set : subSets)
			if (set.isInclude(other))
				return true;
		return false;
	}

	@Override
	public boolean isCross(NumberSet other) {
		for (NumberSet set : subSets)
			if (set.isCross(other))
				return true;
		return false;
	}

	@Override
	@Deprecated
	public NumberSet getIntersection(NumberSet other) {
		// Unsupport
		return EmptySet.EMPTY_SET;
	}

}
