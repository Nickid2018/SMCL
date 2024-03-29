/*
 * Copyright 2021-2023 Nickid2018
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
package io.github.nickid2018.smcl.set;

import io.github.nickid2018.smcl.number.NumberObject;
import io.github.nickid2018.smcl.number.StdNumberObject;

/**
 * A set that only has one number.
 */
public class SingleSet extends Interval {

    /**
     * Construct with a number.
     * @param number a number
     */
    public SingleSet(double number) {
        if (Double.isNaN(number))
            throw new IllegalArgumentException("Not a number!");
        leftRange = number;
        leftClose = true;
        rightRange = number;
        rightClose = true;
    }

    @Override
    public boolean isBelongTo(NumberObject value) {
        return value.isReal() && leftRange == value.toStdNumber();
    }

    @Override
    public boolean isCross(NumberSet other) {
        return other.isBelongTo(StdNumberObject.PROVIDER.fromStdNumber(leftRange));
    }

    @Override
    public boolean isInclude(NumberSet other) {
        return other.isBelongTo(StdNumberObject.PROVIDER.fromStdNumber(leftRange));
    }

    @Override
    public NumberSet getIntersection(NumberSet other) {
        return other.isBelongTo(StdNumberObject.PROVIDER.fromStdNumber(leftRange)) ? this : EmptySet.EMPTY_SET;
    }
}
