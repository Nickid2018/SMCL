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
