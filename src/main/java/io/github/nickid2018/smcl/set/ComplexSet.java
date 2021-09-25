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
package io.github.nickid2018.smcl.set;

import java.util.HashSet;
import java.util.Set;

/**
 * A set that contains several subsets in it.
 */
public class ComplexSet extends NumberSet {

    private final Set<NumberSet> subSets = new HashSet<>();

    /**
     * Put a set in it.
     * @param set a set
     * @return this
     */
    public ComplexSet putSet(NumberSet set) {
        if (set instanceof ComplexSet)
            subSets.addAll(((ComplexSet) set).subSets);
        else if (set instanceof EmptySet)
            return this;
        else
            subSets.add(set);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInfinite() {
        for (NumberSet set : subSets)
            if (set.isInfinite())
                return true;
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBelongTo(double value) {
        for (NumberSet set : subSets)
            if (set.isBelongTo(value))
                return true;
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public boolean isInclude(NumberSet other) {
        for (NumberSet set : subSets)
            if (set.isInclude(other))
                return true;
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCross(NumberSet other) {
        for (NumberSet set : subSets)
            if (set.isCross(other))
                return true;
        return false;
    }

    /**
     * Unsupported
     */
    @Override
    @Deprecated
    public NumberSet getIntersection(NumberSet other) {
        // Unsupported
        return EmptySet.EMPTY_SET;
    }

}
