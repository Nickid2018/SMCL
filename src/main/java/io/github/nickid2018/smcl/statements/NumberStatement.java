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
package io.github.nickid2018.smcl.statements;

import io.github.nickid2018.smcl.DefinedVariables;
import io.github.nickid2018.smcl.Statement;
import io.github.nickid2018.smcl.VariableList;
import io.github.nickid2018.smcl.optimize.NumberPool;

public class NumberStatement extends Statement {

    private double num;

    public NumberStatement(double num) {
        super(null, DefinedVariables.EMPTY_VARIABLES);
        this.num = num;
        if (num < 0)
            isNegative = true;
    }

    public double getNumber() {
        return num;
    }

    public void setNumber(double num) {
        this.num = num;
    }

    @Override
    public double calculate(VariableList list) {
        return num;
    }

    @Override
    protected double calculateInternal(VariableList list) {
        return Math.abs(num);
    }

    @Override
    public String toString() {
        return Double.toString(num);
    }

    @Override
    public Statement getNegative() {
        if (num == 0)
            return this;
        return NumberPool.getNumber(-num);
    }

    @Override
    public Statement getNewNegative() {
        return getNegative();
    }

    @Override
    protected Statement derivativeInternal() {
        return NumberPool.NUMBER_CONST_0;
    }
}
