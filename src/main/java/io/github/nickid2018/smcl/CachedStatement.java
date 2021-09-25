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
package io.github.nickid2018.smcl;

public class CachedStatement extends Statement {

    private final Statement statement;
    private final VariableValueList list = new VariableValueList();
    private double result;
    private boolean dirty = true;

    public CachedStatement(Statement statement) {
        super(statement.smcl, statement.variables);
        this.statement = statement;
    }

    @Override
    public String toString() {
        return statement.toString();
    }

    public void setVariable(String var, double v) {
        dirty = true;
        list.addVariableValue(var, v);
    }

    public double getVariable(String var) {
        return list.getVariableValue(var);
    }

    public double calculate() {
        if (dirty) {
            result = calculateInternal(list);
            dirty = false;
        }
        return result;
    }

    @Override
    public double calculateInternal(VariableValueList list) {
        return statement.calculateInternal(list);
    }

    @Override
    protected Statement derivativeInternal() {
        return statement.derivativeInternal();
    }
}
