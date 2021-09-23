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

public abstract class Statement implements Cloneable {

    protected final DefinedVariables variables;
    protected SMCLContext smcl;
    protected boolean isNegative;

    public Statement(SMCLContext smcl) {
        this.smcl = smcl;
        this.variables = smcl.globalvars.toDefinedVariables();
    }

    public Statement(SMCLContext smcl, DefinedVariables variables) {
        this.smcl = smcl;
        this.variables = variables;
    }

    // Statement Base Functions

    public boolean isNegative() {
        return isNegative;
    }

    public Statement getNegative() {
        isNegative = !isNegative;
        return this;
    }

    public Statement getNewNegative() {
        try {
            Statement s2 = (Statement) clone();
            return s2.getNegative();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public DefinedVariables getVariables() {
        return variables;
    }

    public SMCLContext getSMCL() {
        return smcl;
    }

    public void setSMCL(SMCLContext smcl) {
        this.smcl = smcl;
    }

    @Override
    public abstract String toString();

    protected abstract double calculateInternal(VariableList list);

    public double calculate(VariableList list) {
        return isNegative ? -calculateInternal(list) : calculateInternal(list);
    }

    // Only single variable
    public Statement derivative() {
        if (variables.size() > 1)
            throw new ArithmeticException("Statement " + this + " has more than one independent value");
        return isNegative ? derivativeInternal().getNegative() : derivativeInternal();
    }

    protected abstract Statement derivativeInternal();
}
