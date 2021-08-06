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
package com.github.nickid2018.smcl.functions;

import com.github.nickid2018.smcl.SMCLContext;
import com.github.nickid2018.smcl.Statement;
import com.github.nickid2018.smcl.VariableList;
import com.github.nickid2018.smcl.optimize.NumberPool;
import com.github.nickid2018.smcl.statements.NumberStatement;
import com.github.nickid2018.smcl.statements.arith.MultiplyStatement;

import java.util.function.Function;

public class UnaryFunctionGenStatement extends UnaryFunctionStatement {

    private UnaryFunctionBuilder function;

    public UnaryFunctionGenStatement(Statement ms) {
        super(ms);
    }

    public UnaryFunctionGenStatement(SMCLContext smcl, Statement statement, UnaryFunctionBuilder function) {
        super(statement);
        this.smcl = smcl;
        this.function = function;
    }

    @Override
    public final String toString() {
        return (isNegative ? "-" : "") + getFunction().getName() + "(" + innerStatement + ")";
    }

    @Override
    protected final double calculateInternal(VariableList list) {
        double innerResult = innerStatement.calculate(list);
        innerResult = function.getResolveVariable().accept(innerResult, smcl);
        function.getDomainCheck().accept(innerResult);
        return function.getResolveEnd().accept(getFunction().getCalcFunction().accept(innerResult), smcl);
    }

    public UnaryFunctionBuilder getFunction() {
        return function;
    }

    public UnaryFunctionGenStatement setFunction(UnaryFunctionBuilder function) {
        this.function = function;
        return this;
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
                return partDesi.getNewNegative();
        }
        return new MultiplyStatement(smcl, variables).addMultipliers(end, partDesi);
    }

}
