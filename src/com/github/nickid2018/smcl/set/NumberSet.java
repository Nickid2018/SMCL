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

public abstract class NumberSet {

    public abstract boolean isValid();

    public abstract boolean isInfinite();

    public abstract boolean isBelongTo(double value);

    public abstract boolean isInclude(NumberSet other);

    public abstract boolean isCross(NumberSet other);

    public abstract NumberSet getIntersection(NumberSet other);
}
