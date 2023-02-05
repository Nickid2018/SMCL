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

import io.github.nickid2018.smcl.SMCLContext;
import io.github.nickid2018.smcl.VariableList;
import io.github.nickid2018.smcl.Statement;
import io.github.nickid2018.smcl.VariableValueList;
import io.github.nickid2018.smcl.number.NumberObject;

/**
 * A statement stores the number.
 */
public class NumberStatement extends Statement {

    private final NumberObject num;

    /**
     * Construct a number statement with a number.
     * @param num a number
     */
    public NumberStatement(SMCLContext smcl, NumberObject num) {
        super(smcl, VariableList.EMPTY_VARIABLES, num.isReal() && num.toStdNumber() < 0);
        this.num = num;
    }

    /**
     * Get the number.
     * @return a number
     */
    public NumberObject getNumber() {
        return num;
    }

    @Override
    public NumberObject calculate(VariableValueList list) {
        return num;
    }

    @Override
    protected NumberObject calculateInternal(VariableValueList list) {
        return num.abs();
    }

    @Override
    public String toString() {
        return num.toPlainString();
    }

    @Override
    public Statement negate() {
        return new NumberStatement(context, num.negate());
    }

    @Override
    public Statement deepCopy() {
        return this;
    }

    @Override
    protected Statement derivativeInternal() {
        return new NumberStatement(context, context.numberProvider.fromStdNumber(0));
    }
}
