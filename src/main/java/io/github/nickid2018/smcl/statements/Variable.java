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

public class Variable extends Statement {

    private String name;
    private Variable negativeVar;

    public Variable(String s) {
        super(null, DefinedVariables.EMPTY_VARIABLES);
        if (!s.matches("[a-zA-Z]+"))
            throw new IllegalArgumentException("Illegal variable name:" + s);
        name = s;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        if (negativeVar != null)
            negativeVar.name = name;
    }

    @Override
    public double calculateInternal(VariableList list) {
        return list.getVariableValue(name);
    }

    @Override
    public String toString() {
        return (isNegative ? "-" : "") + name;
    }

    @Override
    public Statement getNegative() {
        if (negativeVar == null) {
            negativeVar = new Variable(name);
            negativeVar.isNegative = true;
            negativeVar.negativeVar = this;
        }
        return negativeVar;
    }

    @Override
    public Statement getNewNegative() {
        return getNegative();
    }

    @Override
    protected Statement derivativeInternal() {
        return NumberPool.NUMBER_CONST_1;
    }
}
