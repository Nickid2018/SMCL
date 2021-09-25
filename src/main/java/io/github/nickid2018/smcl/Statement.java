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

/**
 * A statement can use it to compute and derivative
 */
public abstract class Statement implements Cloneable {

    protected final VariableList variables;
    protected SMCLContext smcl;
    protected boolean isNegative;

    /**
     * Construct a statement with the context.
     * @param smcl a context
     */
    public Statement(SMCLContext smcl) {
        this.smcl = smcl;
        this.variables = smcl.globalvars.toDefinedVariables();
    }

    /**
     * Construct a statement with the context and a variable list.
     * @param smcl a context
     * @param variables a variable list
     */
    public Statement(SMCLContext smcl, VariableList variables) {
        this.smcl = smcl;
        this.variables = variables;
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
    public Statement getNegative() {
        isNegative = !isNegative;
        return this;
    }

    /**
     * Get the statement that is negative and unrelated from this.
     * @return statement that is negative and unrelated from this
     */
    public Statement getNewNegative() {
        try {
            Statement s2 = (Statement) clone();
            return s2.getNegative();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

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
        return smcl;
    }

    /**
     * Set the context of the statement.
     * @param smcl a context
     */
    public void setSMCL(SMCLContext smcl) {
        this.smcl = smcl;
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
    protected abstract double calculateInternal(VariableValueList list);

    /**
     * Calculate the statement with a variable list.
     * @param list a variable list
     * @return the result
     */
    public double calculate(VariableValueList list) {
        return isNegative ? -calculateInternal(list) : calculateInternal(list);
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
        return isNegative ? derivativeInternal().getNegative() : derivativeInternal();
    }
    /**
     * Internal derivatization for subclass to override.
     * @return the result without sign
     */
    protected abstract Statement derivativeInternal();
}
