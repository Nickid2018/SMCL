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

/**
 * A set that has no element.
 */
public class EmptySet extends NumberSet {

    /**
     * Default instance.
     */
    public static final EmptySet EMPTY_SET = new EmptySet();

    private EmptySet() {
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
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBelongTo(double value) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInclude(NumberSet other) {
        return other instanceof EmptySet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCross(NumberSet other) {
        return other instanceof EmptySet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NumberSet getIntersection(NumberSet other) {
        return other;
    }
}
