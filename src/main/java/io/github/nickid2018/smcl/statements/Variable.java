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
 * A statement stands for a variable.
 */
public class Variable extends Statement {

    private final String name;
    private Variable negativeVar;

    /**
     * Construct a variable with a name.
     * @param s a name
     */
    public Variable(SMCLContext smcl, String s) {
        this(smcl, s, false);
    }

    /**
     * Construct a variable with a name and a negative flag.
     * @param s a name
     * @param isNegative a negative flag
     */
    public Variable(SMCLContext smcl, String s, boolean isNegative) {
        super(smcl, VariableList.EMPTY_VARIABLES, isNegative);
        if (!s.matches("[a-zA-Z]+"))
            throw new IllegalArgumentException(String.format(
                    smcl.settings.resourceBundle.getString("smcl.parse.error.invalid_variable"), s));
        name = s;
    }

    /**
     * Get the name of the variable.
     * @return the name
     */
    public String getName() {
        return name;
    }

    @Override
    public NumberObject calculateInternal(VariableValueList list) {
        return list.getVariableValue(name);
    }

    @Override
    public String toString() {
        return (isNegative ? "-" : "") + name;
    }

    @Override
    public Statement negate() {
        if (negativeVar == null)
            negativeVar = new Variable(getSMCL(), name, !isNegative);
        return negativeVar;
    }

    @Override
    public Statement deepCopy() {
        return this;
    }

    @Override
    protected Statement derivativeInternal() {
        return new NumberStatement(context, context.numberProvider.getOne());
    }
}
