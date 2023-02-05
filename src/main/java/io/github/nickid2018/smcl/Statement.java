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
package io.github.nickid2018.smcl;

import io.github.nickid2018.smcl.number.NumberObject;

/**
 * A statement can use it to compute and derivative
 */
public abstract class Statement implements Cloneable {

    protected final VariableList variables;
    protected final boolean isNegative;
    protected SMCLContext context;

    /**
     * Construct a statement with the context.
     * @param smcl a context
     */
    public Statement(SMCLContext smcl) {
        this.context = smcl;
        this.variables = smcl.globalvars.toDefinedVariables();
        this.isNegative = false;
    }

    /**
     * Construct a statement with the context and a variable list.
     * @param smcl a context
     * @param variables a variable list
     */
    public Statement(SMCLContext smcl, VariableList variables) {
        this.context = smcl;
        this.variables = variables;
        this.isNegative = false;
    }

    /**
     * Construct a statement with the context and a variable list.
     * @param smcl a context
     * @param variables a variable list
     * @param isNegative whether the statement is negative
     */
    public Statement(SMCLContext smcl, VariableList variables, boolean isNegative) {
        this.context = smcl;
        this.variables = variables;
        this.isNegative = isNegative;
    }

    // Statement Base Functions

    /**
     * Returns true if the statement is negative.
     * @return true if the statement is negative
     */
    public boolean isNegative() {
        return isNegative;
    }

    /**
     * Get the statement that is negative from this.
     * @return statement that is negative from this
     */
    public abstract Statement negate();

    public abstract Statement deepCopy();

    /**
     * Get the variable list for the statement.
     * @return a variable list
     */
    public VariableList getVariables() {
        return variables;
    }

    /**
     * Get the context of the statement.
     * @return a context
     */
    public SMCLContext getSMCL() {
        return context;
    }

    /**
     * Set the context of the statement.
     * @param smcl a context
     */
    public void setSMCL(SMCLContext smcl) {
        this.context = smcl;
    }

    /**
     * Get the statement in string.
     * @return a string contains the statement
     */
    @Override
    public abstract String toString();

    /**
     * Internal calculation for subclass to override.
     * @param list a variable list
     * @return the result without sign
     */
    protected abstract NumberObject calculateInternal(VariableValueList list);

    /**
     * Calculate the statement with a variable list.
     * @param list a variable list
     * @return the result
     */
    public NumberObject calculate(VariableValueList list) {
        list = list == null ? new VariableValueList(context) : list;
        return isNegative ? calculateInternal(list).negate() : calculateInternal(list);
    }

    // Only single variable
    /**
     * Derivative the statement.
     * @return the result
     * @throws ArithmeticException throws of the statement can't be derivatived
     */
    public Statement derivative() {
        if (variables.size() > 1)
            throw new ArithmeticException("Statement " + this + " has more than one independent value");
        return isNegative ? derivativeInternal().negate() : derivativeInternal();
    }
    /**
     * Internal derivatization for subclass to override.
     * @return the result without sign
     */
    protected abstract Statement derivativeInternal();

}
