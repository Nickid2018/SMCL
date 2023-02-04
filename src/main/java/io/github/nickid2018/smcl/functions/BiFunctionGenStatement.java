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

package io.github.nickid2018.smcl.functions;

import io.github.nickid2018.smcl.Statement;
import io.github.nickid2018.smcl.VariableValueList;

import java.util.function.BiFunction;

public class BiFunctionGenStatement extends BiFunctionStatement{

    private BinaryFunctionBuilder function;

    /**
     * Construct with arguments.
     *
     * @param statement1 the first statement
     * @param statement2 the second statement
     */
    public BiFunctionGenStatement(Statement statement1, Statement statement2) {
        super(statement1, statement2);
    }

    /**
     * Get the function builder.
     * @return a function builder
     */
    public BinaryFunctionBuilder getFunction() {
        return function;
    }

    /**
     * Set the function builder.
     * @param function a function builder
     * @return this
     */
    public BiFunctionGenStatement setFunction(BinaryFunctionBuilder function) {
        this.function = function;
        return this;
    }

    @Override
    public String toString() {
        return (isNegative ? "-" : "") + function.getName() + "(" + innerStatement1 + ", " + innerStatement2 + ")";
    }

    @Override
    protected double calculateInternal(VariableValueList list) {
        double compute1 = innerStatement1.calculate(list);
        double compute2 = innerStatement2.calculate(list);
        compute1 = function.getResolveVariable1().accept(compute1, context);
        compute2 = function.getResolveVariable2().accept(compute2, context);
        try {
            function.getDomainCheck1().accept(compute1);
            function.getDomainCheck2().accept(compute2);
        } catch (ArithmeticException e) {
            if(context.settings.invalidArgumentWarn)
                System.err.println("Warning: " + e.getLocalizedMessage() + " at " + this);
            else
                throw e;
        }
        return function.getResolveEnd().accept(function.getCalcFunction().accept(compute1, compute2), context);
    }

    @Override
    protected Statement derivativeInternal() {
        BiFunction<Statement, Statement, Statement> derivative = function.getDerivativeResolver();
        if(derivative == null)
            throw new ArithmeticException("Unsupported or illegal function to derivative: " + getFunction().getName());
        return derivative.apply(innerStatement1, innerStatement2);
    }
}
