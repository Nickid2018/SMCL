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
package io.github.nickid2018.smcl.statements.arith;

import io.github.nickid2018.smcl.VariableList;
import io.github.nickid2018.smcl.SMCLContext;
import io.github.nickid2018.smcl.Statement;
import io.github.nickid2018.smcl.VariableValueList;
import io.github.nickid2018.smcl.functions.Functions;
import io.github.nickid2018.smcl.functions.UnaryFunctionStatement;
import io.github.nickid2018.smcl.optimize.NumberPool;
import io.github.nickid2018.smcl.statements.NumberStatement;
import io.github.nickid2018.smcl.statements.Variable;

import java.util.ArrayList;
import java.util.List;

/**
 * A statement stands for powering.
 */
public class PowerStatement extends Statement {

    private final List<Statement> exponents = new ArrayList<>();
    private Statement base;

    /**
     * Construct a power statement with a context and a variable list.
     * @param smcl a context
     * @param variables a variable list
     */
    public PowerStatement(SMCLContext smcl, VariableList variables) {
        super(smcl, variables);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double calculateInternal(VariableValueList list) {
        double prevExp = exponents.get(exponents.size() - 1).calculate(list);
        for (int now = exponents.size() - 2; now > -2; now--) {
            double currentBase = now == -1 ? base.calculate(list) : exponents.get(now).calculate(list);
            if (currentBase == 0 && prevExp <= 0)
                if (context.settings.invalidArgumentWarn)
                    System.err.println("Warning: 0 is multiplied by an exponent not greater than 0 at " + this);
                else
                    throw new ArithmeticException("0 is multiplied by an exponent not greater than 0");
            if (currentBase < 0) {
                int intPrev = (int) prevExp;
                if (Math.abs(intPrev - prevExp) > 1E-5)
                    if (context.settings.invalidArgumentWarn)
                        System.err.println("Warning: A negative number is multiplied by a fraction at " + this);
                    else
                        throw new ArithmeticException("A negative number is multiplied by a fraction");
            }
            prevExp = Math.pow(currentBase, prevExp);
        }
        return prevExp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (!(base instanceof NumberStatement) && !(base instanceof Variable))
            sb.append("(").append(base).append(")");
        else
            sb.append(base);
        for (Statement en : exponents) {
            if ((!(en instanceof NumberStatement) && !(en instanceof Variable)
                    && !(en instanceof UnaryFunctionStatement)) || en.isNegative())
                sb.append("^(").append(en).append(")");
            else
                sb.append("^").append(en);
        }
        return sb + "";
    }

    /**
     * Add an exponent at end.
     * @param statement a divisor
     * @return this
     */
    public PowerStatement addExponent(Statement statement) {
        exponents.add(statement);
        return this;
    }

    /**
     * Set base and exponents.
     * @param statements base and exponents
     * @return this
     */
    public PowerStatement putBaseAndExponents(Statement... statements) {
        base = statements[0];
        for (int i = 1; i < statements.length; i++) {
            addExponent(statements[i]);
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    // (f^g)' = (f^g)(glnf)'
    // Optimize:
    // 1) f = C: (C^g)' = (C^g)g'lnC
    // 2) g = C: (f^C)' = Cf'f^(C-1)
    protected Statement derivativeInternal() {
        if (exponents.size() > 1) {
            Statement now = base.getClone();
            for (Statement statement : exponents)
                now = new PowerStatement(context, variables).putBaseAndExponents(now, statement.getClone());
            return now.derivative();
        }
        Statement exponent = exponents.get(0);
        boolean baseN = base instanceof NumberStatement;
        boolean exponentN = exponent instanceof NumberStatement;
        if (baseN && exponentN)
            return NumberPool.NUMBER_CONST_0;
        if (baseN) {
            double constNumber = Math.log(base.calculate(null));
            Statement derivative = exponent.derivative();
            if (derivative instanceof NumberStatement)
                constNumber *= derivative.calculate(null);
            if (constNumber == 0)
                return NumberPool.NUMBER_CONST_0;
            if (constNumber == 1 || constNumber == -1) {
                MultiplyStatement end = new MultiplyStatement(context, variables).addMultiplier(this);
                return constNumber == 1 ? (derivative instanceof NumberStatement) ? end : end.addMultiplier(derivative)
                        : (derivative instanceof NumberStatement) ? end : end.addMultiplier(derivative).getNegative();
            } else {
                MultiplyStatement end = new MultiplyStatement(context, variables)
                        .addMultipliers(NumberPool.getNumber(constNumber), this);
                return (derivative instanceof NumberStatement) ? end : end.addMultiplier(derivative);
            }
        }
        if (exponentN) {
            double exp = exponent.calculate(null);
            double constNumber = exp;
            if (exp == 0)
                return NumberPool.NUMBER_CONST_0;
            if (exp == 1)
                return base.derivative();
            Statement derivative = base.derivative();
            if (derivative instanceof NumberStatement)
                constNumber *= derivative.calculate(null);
            if (constNumber == 0)
                return NumberPool.NUMBER_CONST_0;
            Statement trexp = exp == 2 ? base.getClone()
                    : new PowerStatement(context, variables).putBaseAndExponents(base.getClone(), NumberPool.getNumber(exp - 1));
            if (constNumber == 1 || constNumber == -1) {
                MultiplyStatement end = new MultiplyStatement(context, variables).addMultiplier(trexp);
                return constNumber == 1 ? (derivative instanceof NumberStatement) ? end : end.addMultiplier(derivative)
                        : (derivative instanceof NumberStatement) ? end : end.addMultiplier(derivative).getNegative();
            } else {
                MultiplyStatement end = new MultiplyStatement(context, variables)
                        .addMultipliers(NumberPool.getNumber(constNumber), trexp);
                return (derivative instanceof NumberStatement) ? end : end.addMultiplier(derivative);
            }
        }
        Statement multi = new MultiplyStatement(context, variables)
                .addMultipliers(exponent.getClone(), Functions.LN.create(context, base.getClone())).derivative();
        if (multi instanceof NumberStatement) {
            double constNumber = multi.calculate(null);
            if (constNumber == 0)
                return NumberPool.NUMBER_CONST_0;
            if (constNumber == 1)
                return this;
            if (constNumber == -1)
                return getNewNegative();
            return new MultiplyStatement(context, variables).addMultipliers(multi, getClone());
        } else
            return new MultiplyStatement(context, variables).addMultipliers(getClone(), multi);
    }
}
