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

public abstract class NumberObject {

    public abstract NumberObject add(NumberObject number);
    public abstract NumberObject subtract(NumberObject number);
    public abstract NumberObject multiply(NumberObject number);
    public abstract NumberObject divide(NumberObject number);
    public abstract NumberObject power(NumberObject number);
    public abstract NumberObject negate();
    public abstract NumberObject abs();
    public abstract NumberObject sgn();

    public abstract NumberObject sin();
    public abstract NumberObject cos();
    public abstract NumberObject tan();
    public abstract NumberObject asin();
    public abstract NumberObject acos();
    public abstract NumberObject atan();
    public abstract NumberObject sinh();
    public abstract NumberObject cosh();
    public abstract NumberObject tanh();
    public abstract NumberObject log();
    public abstract NumberObject log10();
    public abstract NumberObject reciprocal();
    public abstract boolean isReal();
    public abstract boolean isZero();
    public abstract boolean isOne();
    public abstract boolean isMinusOne();
    public abstract double toStdNumber();
    public abstract String toPlainString();
    public abstract NumberProvider<? extends  NumberObject> getProvider();

    @Override
    public String toString() {
        return toPlainString();
    }
}
