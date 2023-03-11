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

package io.github.nickid2018.smcl.number;

public abstract class NumberProvider<T extends NumberObject> {

    private final T zero;
    private final T one;
    private final T minusOne;

    public NumberProvider() {
        zero = fromStdNumber(0);
        one = fromStdNumber(1);
        minusOne = fromStdNumber(-1);
    }

    public abstract T fromStdNumber(double value);
    public abstract T fromStdNumberDivided(double dividend, double divisor);

    public abstract T fromString(String value);

    public T getZero() {
        return zero;
    }

    public T getOne() {
        return one;
    }

    public T getMinusOne() {
        return minusOne;
    }
}
