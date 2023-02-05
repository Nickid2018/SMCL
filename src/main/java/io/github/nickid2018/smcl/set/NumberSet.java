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

/**
 * A number set is an object that can store numbers in it.
 */
public abstract class NumberSet {

    /**
     * Returns whether the number set is valid.
     * @return true if the number set is valid
     */
    public abstract boolean isValid();

    /**
     * Returns whether the number set is infinite.
     * @return true if the number set is infinite
     */
    public abstract boolean isInfinite();

    /**
     * Returns whether the number is in the set.
     * @param value a number
     * @return true if the number is in the set
     */
    public abstract boolean isBelongTo(NumberObject value);

    /**
     * Returns whether another number set is included in the set.
     * @param other a number set
     * @return true if another number set is included in the set
     */
    public abstract boolean isInclude(NumberSet other);

    /**
     * Returns whether another number set is crossing with the set.
     * @param other a number set
     * @return true if another number set is crossing with the set
     */
    public abstract boolean isCross(NumberSet other);

    /**
     * Get the intersection of the two sets.
     * @param other a number set
     * @return the intersection; if the two set isn't crossing with, return empty set
     */
    public abstract NumberSet getIntersection(NumberSet other);
}
