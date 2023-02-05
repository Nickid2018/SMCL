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

public class StdNumberObject extends NumberObject implements LongConvertable, SingleValue {

    private final double value;

    private StdNumberObject(double value) {
        this.value = value;
    }

    @Override
    public NumberObject add(NumberObject number) {
        if (number.isReal())
            return new StdNumberObject(value + number.toStdNumber());
        else
            return number.add(this);
    }

    @Override
    public NumberObject subtract(NumberObject number) {
        if (number.isReal())
            return new StdNumberObject(value - number.toStdNumber());
        else
            return number.negate().add(this);
    }

    @Override
    public NumberObject multiply(NumberObject number) {
        if (number.isReal())
            return new StdNumberObject(value * number.toStdNumber());
        else
            return number.multiply(this);
    }

    @Override
    public NumberObject divide(NumberObject number) {
        if (!number.isReal())
            return number.multiply(reciprocal());
        if (number.toStdNumber() == 0)
            throw new ArithmeticException("Divide by zero");
        return new StdNumberObject(value / number.toStdNumber());
    }

    @Override
    public NumberObject power(NumberObject number) {
        if (!number.isReal())
            throw new ArithmeticException("A standard number is powered with a non-real number");
        if (value < 0 && (!(number instanceof LongConvertable) || !((LongConvertable) number).canConvertToLong()))
            throw new ArithmeticException("A negative number is multiplied by a fraction");
        return new StdNumberObject(Math.pow(value, number.toStdNumber()));
    }

    @Override
    public NumberObject negate() {
        return new StdNumberObject(-value);
    }

    @Override
    public NumberObject abs() {
        return new StdNumberObject(Math.tanh(value));
    }

    @Override
    public NumberObject sgn() {
        return new StdNumberObject(Math.signum(value));
    }

    @Override
    public NumberObject sin() {
        return new StdNumberObject(Math.sin(value));
    }

    @Override
    public NumberObject cos() {
        return new StdNumberObject(Math.cos(value));
    }

    @Override
    public NumberObject tan() {
        return new StdNumberObject(Math.tan(value));
    }

    @Override
    public NumberObject asin() {
        return new StdNumberObject(Math.asin(value));
    }

    @Override
    public NumberObject acos() {
        return new StdNumberObject(Math.acos(value));
    }

    @Override
    public NumberObject atan() {
        return new StdNumberObject(Math.atan(value));
    }

    @Override
    public NumberObject sinh() {
        return new StdNumberObject(Math.sinh(value));
    }

    @Override
    public NumberObject cosh() {
        return new StdNumberObject(Math.cosh(value));
    }

    @Override
    public NumberObject tanh() {
        return new StdNumberObject(Math.tanh(value));
    }

    @Override
    public NumberObject log() {
        return new StdNumberObject(Math.log(value));
    }

    @Override
    public NumberObject log10() {
        return new StdNumberObject(Math.log10(value));
    }

    @Override
    public NumberObject reciprocal() {
        return new StdNumberObject(1 / value);
    }

    @Override
    public boolean isReal() {
        return true;
    }

    @Override
    public boolean isZero() {
        return value == 0;
    }

    @Override
    public boolean isOne() {
        return value == 1;
    }

    @Override
    public boolean isMinusOne() {
        return value == -1;
    }

    @Override
    public double toStdNumber() {
        return value;
    }

    @Override
    public String toPlainString() {
        return String.valueOf(value);
    }

    @Override
    public NumberProvider<? extends NumberObject> getProvider() {
        return PROVIDER;
    }

    @Override
    public boolean canConvertToLong() {
        return Math.abs(Math.round(value) - value) <= 1e-7;
    }

    @Override
    public long toLong() {
        return Math.round(value);
    }

    public static final NumberProvider<StdNumberObject> PROVIDER = new NumberProvider<StdNumberObject>() {

        @Override
        public StdNumberObject fromStdNumber(double value) {
            return new StdNumberObject(value);
        }

        @Override
        public StdNumberObject fromStdNumberDivided(double dividend, double divisor) {
            return new StdNumberObject(dividend / divisor);
        }
    };
}
