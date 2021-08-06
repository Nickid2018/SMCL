/*
 * Copyright 2021 Nickid2018
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.nickid2018.smcl.set;

public class Interval extends NumberSet {

    public double leftRange;
    public double rightRange;

    public boolean leftInfinite = false;
    public boolean rightInfinite = false;

    public boolean leftClose;
    public boolean rightClose;

    public static Interval fromNonInfString(String str) {
        Interval val = new Interval();
        // Check pattern
        if (!str.matches("[\\[(][-+\\d.]+,[-+\\d.]+[)\\]]"))
            throw new IllegalArgumentException("Illegal pattern for interval: " + str);
        // Left edge
        val.leftClose = str.charAt(0) == '[';
        // Right edge
        val.rightClose = str.charAt(str.length() - 1) == ']';
        String[] numbers = str.substring(1, str.length() - 1).split(",");
        val.leftRange = Double.parseDouble(numbers[0]);
        val.rightRange = Double.parseDouble(numbers[1]);
        if (!val.isValid())
            throw new IllegalArgumentException("Invalid interval: " + str);
        return val;
    }

    public static Interval lessThanInclude(double right) {
        return new Interval().setLeftInfinite().setRightRange(right, true);
    }

    public static Interval lessThanExclude(double right) {
        return new Interval().setLeftInfinite().setRightRange(right, false);
    }

    public static Interval moreThanInclude(double left) {
        return new Interval().setRightInfinite().setLeftRange(left, true);
    }

    public static Interval moreThanExclude(double left) {
        return new Interval().setRightInfinite().setLeftRange(left, false);
    }

    public static Interval universeSet() {
        return new Interval().setLeftInfinite().setRightInfinite();
    }

    public Interval setLeftInfinite() {
        leftInfinite = true;
        leftClose = false;
        leftRange = Double.NEGATIVE_INFINITY;
        return this;
    }

    public Interval setRightInfinite() {
        rightInfinite = true;
        rightClose = false;
        rightRange = Double.POSITIVE_INFINITY;
        return this;
    }

    public Interval setLeftRange(double value, boolean close) {
        if (Double.isNaN(value))
            throw new IllegalArgumentException("Not a number!");
        leftInfinite = false;
        leftRange = value;
        leftClose = close;
        return this;
    }

    public Interval setRightRange(double value, boolean close) {
        if (Double.isNaN(value))
            throw new IllegalArgumentException("Not a number!");
        rightInfinite = false;
        rightRange = value;
        rightClose = close;
        return this;
    }

    @Override
    public boolean isValid() {
        return (leftRange < rightRange)
                || (!(leftInfinite || rightInfinite) && (leftClose && rightClose) && leftRange == rightRange);
    }

    @Override
    public boolean isInfinite() {
        return isValid() && leftRange != rightRange;
    }

    @Override
    public boolean isBelongTo(double value) {
        return (leftClose && leftRange == value && !leftInfinite)
                || (rightClose && rightRange == value && !rightInfinite) || (leftRange < value && rightRange > value);
    }

    @Override
    public boolean isInclude(NumberSet other) {
        if (other instanceof EmptySet)
            return true;
        if (other instanceof Interval) {
            Interval i = (Interval) other;
            return (leftInfinite || (leftRange == i.leftRange && leftClose == i.leftClose) || leftRange < i.leftRange)
                    && (rightInfinite || (rightRange == i.rightRange && rightClose == i.rightClose)
                    || rightRange > i.rightRange);
        }
        return false;
    }

    @Override
    public boolean isCross(NumberSet other) {
        if (other instanceof EmptySet)
            return false;
        if (other instanceof Interval) {
            Interval i = (Interval) other;
            return (leftRange - i.rightRange) * (rightRange - i.leftRange) < 0
                    || (leftRange == i.leftRange && leftClose && i.leftClose)
                    || (rightRange == i.rightRange && rightClose && i.rightClose);
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
            if (other instanceof SingleSet)
                return other;
            Interval oth = (Interval) other;
            Interval ret = new Interval();
            if (leftRange == oth.leftRange) {
                ret.leftRange = leftRange;
                ret.leftClose = leftClose && oth.leftClose;
            } else {
                boolean left = leftRange > oth.leftRange;
                ret.leftRange = left ? leftRange : oth.leftRange;
                ret.leftClose = left ? leftClose : oth.leftClose;
            }
            ret.leftInfinite = leftInfinite && oth.leftInfinite;
            if (rightRange == oth.rightRange) {
                ret.rightRange = rightRange;
                ret.rightClose = rightClose && oth.rightClose;
            } else {
                boolean left = rightRange < oth.rightRange;
                ret.rightRange = left ? rightRange : oth.rightRange;
                ret.rightClose = left ? rightClose : oth.rightClose;
            }
            ret.rightInfinite = rightInfinite && oth.rightInfinite;
            return ret;
        }
        // Unsupported ComplexSet
        return EmptySet.EMPTY_SET;
    }
}
