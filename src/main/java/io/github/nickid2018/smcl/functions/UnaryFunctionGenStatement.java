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

import io.github.nickid2018.smcl.SMCLContext;
import io.github.nickid2018.smcl.Statement;
import io.github.nickid2018.smcl.VariableValueList;
import io.github.nickid2018.smcl.number.NumberPool;
import io.github.nickid2018.smcl.statements.NumberStatement;
import io.github.nickid2018.smcl.statements.arith.MultiplyStatement;

import java.util.function.Function;

/**
 * Statement for unary functions.
 */
public class UnaryFunctionGenStatement extends UnaryFunctionStatement {

    private final UnaryFunctionBuilder function;

    /**
     * Construct a statement with a context, an argument and a function builder.
     * @param statement a statement
     * @param function a function builder
     */
    public UnaryFunctionGenStatement(Statement statement, UnaryFunctionBuilder function) {
        this(statement, function, false);
    }

    /**
     * Construct a statement with a context, an argument and a function builder.
     * @param statement a statement
     * @param function a function builder
     */
    public UnaryFunctionGenStatement(Statement statement, UnaryFunctionBuilder function, boolean isNegative) {
        super(statement, isNegative);
        this.function = function;
    }

    @Override
    public Statement negate() {
        return new UnaryFunctionGenStatement(innerStatement.deepCopy(), function, !isNegative);
    }

    @Override
    public Statement deepCopy() {
        return new UnaryFunctionGenStatement(innerStatement.deepCopy(), function, isNegative);
    }

    @Override
    public final String toString() {
        return (isNegative ? "-" : "") + getFunction().getName() + "(" + innerStatement + ")";
    }

    @Override
    protected final double calculateInternal(VariableValueList list) {
        double innerResult = innerStatement.calculate(list);
        innerResult = function.getResolveVariable().accept(innerResult, context);
        try {
            function.getDomainCheck().accept(innerResult);
        } catch (ArithmeticException e) {
            if(context.settings.invalidArgumentWarn)
                System.err.println("Warning: " + e.getLocalizedMessage() + " at " + this);
            else
                throw e;
        }
        return function.getResolveEnd().accept(getFunction().getCalcFunction().accept(innerResult), context);
    }

    public UnaryFunctionBuilder getFunction() {
        return function;
    }

    @Override
    protected Statement derivativeInternal() {
        Function<Statement, Statement> resolver = function.getDerivativeResolver();
        if (resolver == null)
            throw new ArithmeticException("Unsupported or illegal function to derivative: " + getFunction().getName());
        Statement partDesi = resolver.apply(innerStatement);
        Statement end = innerStatement.derivative();
        if (end instanceof NumberStatement) {
            double get = end.calculate(null);
            if (get == 0)
                return NumberPool.NUMBER_CONST_0;
            if (get == 1)
                return partDesi;
            if (get == -1)
                return partDesi.negate();
        }
        return new MultiplyStatement(context, variables, end, partDesi);
    }
}
