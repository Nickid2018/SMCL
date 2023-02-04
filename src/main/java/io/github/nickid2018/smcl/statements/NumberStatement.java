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
package io.github.nickid2018.smcl.statements;

import io.github.nickid2018.smcl.VariableList;
import io.github.nickid2018.smcl.Statement;
import io.github.nickid2018.smcl.VariableValueList;
import io.github.nickid2018.smcl.number.NumberPool;

/**
 * A statement stores the number.
 */
public class NumberStatement extends Statement {

    private final double num;

    /**
     * Construct a number statement with a number.
     * @param num a number
     */
    public NumberStatement(double num) {
        super(null, VariableList.EMPTY_VARIABLES, num < 0);
        this.num = num;
    }

    /**
     * Get the number.
     * @return a number
     */
    public double getNumber() {
        return num;
    }

    @Override
    public double calculate(VariableValueList list) {
        return num;
    }

    @Override
    protected double calculateInternal(VariableValueList list) {
        return Math.abs(num);
    }

    @Override
    public String toString() {
        return Double.toString(num);
    }

    @Override
    public Statement negate() {
        if (num == 0)
            return this;
        return NumberPool.getNumber(-num);
    }

    @Override
    public Statement deepCopy() {
        return this;
    }

    @Override
    protected Statement derivativeInternal() {
        return NumberPool.NUMBER_CONST_0;
    }
}
