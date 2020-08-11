package com.github.nickid2018.smcl.set;

public class Interval extends NumberSet {

	public double leftRange;
	public double rightRange;

	public boolean leftInfinite = false;
	public boolean rightInfinite = false;

	public boolean leftClose;
	public boolean rightClose;

	public void setLeftInfinite() {
		leftInfinite = true;
		leftRange = Double.MIN_VALUE;
	}

	public void setRightInfinite() {
		rightInfinite = true;
		rightRange = Double.MAX_VALUE;
	}

	public void setLeftRange(double value, boolean close) {
		leftInfinite = false;
		leftRange = value;
		leftClose = close;
	}

	public void setRightRange(double value, boolean close) {
		rightInfinite = false;
		rightRange = value;
		rightClose = close;
	}

	@Override
	public boolean isValid() {
		return (leftRange < rightRange) || (!(leftInfinite || rightInfinite) && (leftClose && rightClose) && leftRange == rightRange);
	}

    @Override
    public boolean isInfinite() {
        return isValid() && leftRange != rightRange;
    }

	@Override
	public boolean isBelongTo(double value) {
		return (leftClose && leftRange == value && !leftInfinite) || (rightClose && rightRange == value && !rightInfinite) || (leftRange < value && rightRange > value);
	}

	@Override
	public boolean isInclude(NumberSet other) {
		if (other instanceof EmptySet)
			return true;
		if (other instanceof Interval) {
			Interval i = (Interval)other;
			return (leftInfinite || (leftRange == i.leftRange && leftClose == i.leftClose) || leftRange < i.leftRange) && (rightInfinite || (rightRange == i.rightRange && rightClose == i.rightClose) || rightRange > i.rightRange);
		}
		return false;
	}

	@Override
	public boolean isCross(NumberSet other) {
		if (other instanceof EmptySet)
			return false;
		if (other instanceof Interval) {
			Interval i = (Interval) other;;
			return (leftRange - i.rightRange) * (rightRange - i.leftRange) < 0 || (leftRange == i.leftRange && leftClose && i.leftClose) || (rightRange == i.rightRange && rightClose && i.rightClose);
		}
		return false;
	}

	@Override
	public NumberSet getIntersection(NumberSet other) {
		if (other instanceof EmptySet)
			return EmptySet.EMPTY_SET;
		if (other instanceof Interval) {
			if (!isCross(other))
				return EmptySet.EMPTY_SET;

		}
		return null;
	}
}
