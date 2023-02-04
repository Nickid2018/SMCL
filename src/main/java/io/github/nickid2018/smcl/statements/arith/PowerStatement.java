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
package io.github.nickid2018.smcl.statements.arith;

import io.github.nickid2018.smcl.SMCLContext;
import io.github.nickid2018.smcl.Statement;
import io.github.nickid2018.smcl.VariableList;
import io.github.nickid2018.smcl.VariableValueList;
import io.github.nickid2018.smcl.functions.Functions;
import io.github.nickid2018.smcl.functions.UnaryFunctionStatement;
import io.github.nickid2018.smcl.number.NumberPool;
import io.github.nickid2018.smcl.statements.NumberStatement;
import io.github.nickid2018.smcl.statements.Variable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A statement stands for powering.
 */
public class PowerStatement extends Statement {

    private final List<Statement> exponents;
    private final Statement base;

    /**
     * Construct a power statement with a context and a variable list.
     *
     * @param smcl      a context
     * @param variables a variable list
     * @param exponents exponents
     */
    public PowerStatement(SMCLContext smcl, VariableList variables, Statement base, Statement... exponents) {
        this(smcl, variables, false, base, Arrays.asList(exponents));
    }

    public PowerStatement(SMCLContext smcl, VariableList variables, boolean isNegative, Statement base, Statement... exponents) {
        this(smcl, variables, isNegative, base, Arrays.asList(exponents));
    }

    /**
     * Construct a power statement with a context and a variable list.
     *
     * @param smcl      a context
     * @param variables a variable list
     * @param exponents exponents
     */
    public PowerStatement(SMCLContext smcl, VariableList variables, Statement base, List<Statement> exponents) {
        this(smcl, variables, false, base, exponents);
    }

    /**
     * Construct a power statement with a context and a variable list.
     *
     * @param smcl       a context
     * @param variables  a variable list
     * @param isNegative whether the statement is negative
     */
    public PowerStatement(SMCLContext smcl, VariableList variables, boolean isNegative, Statement base, List<Statement> exponents) {
        super(smcl, variables, isNegative);
        this.base = base;
        this.exponents = Collections.unmodifiableList(exponents);
    }

    @Override
    public Statement negate() {
        return new PowerStatement(context, variables, !isNegative, base,
                exponents.stream().map(Statement::deepCopy).collect(Collectors.toList()));
    }

    @Override
    public Statement deepCopy() {
        return new PowerStatement(context, variables, isNegative, base,
                exponents.stream().map(Statement::deepCopy).collect(Collectors.toList()));
    }

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

    public Statement getBase() {
        return base;
    }

    public List<Statement> getExponents() {
        return exponents;
    }

    @Override
    // (f^g)' = (f^g)(glnf)'
    // Optimize:
    // 1) f = C: (C^g)' = (C^g)g'lnC
    // 2) g = C: (f^C)' = Cf'f^(C-1)
    protected Statement derivativeInternal() {
        if (exponents.size() > 1) {
            Statement now = base.deepCopy();
            for (Statement statement : exponents)
                now = new PowerStatement(context, variables, now, statement.deepCopy());
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
            if (constNumber == 1 || constNumber == -1)
                return new MultiplyStatement(context, variables, constNumber == -1,
                        derivative instanceof NumberStatement ?
                                Collections.singletonList(deepCopy()) :
                                Arrays.asList(deepCopy(), derivative));
            else
                return new MultiplyStatement(context, variables,
                        derivative instanceof NumberStatement ?
                                Arrays.asList(NumberPool.getNumber(constNumber), deepCopy()) :
                                Arrays.asList(NumberPool.getNumber(constNumber), deepCopy(), derivative));
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
            Statement trexp = exp == 2 ? base.deepCopy()
                    : new PowerStatement(context, variables, base.deepCopy(), NumberPool.getNumber(exp - 1));
            if (constNumber == 1 || constNumber == -1) {
                return new MultiplyStatement(context, variables, constNumber == -1,
                        derivative instanceof NumberStatement ?
                                Collections.singletonList(trexp) :
                                Arrays.asList(trexp, derivative));
            } else {
                return new MultiplyStatement(context, variables,
                        derivative instanceof NumberStatement ?
                                Arrays.asList(NumberPool.getNumber(constNumber), trexp) :
                                Arrays.asList(NumberPool.getNumber(constNumber), trexp, derivative));
            }
        }
        Statement multi = new MultiplyStatement(context, variables,
                exponent.deepCopy(), Functions.LN.create(base.deepCopy())).derivative();
        if (multi instanceof NumberStatement) {
            double constNumber = multi.calculate(null);
            if (constNumber == 0)
                return NumberPool.NUMBER_CONST_0;
            if (constNumber == 1)
                return deepCopy();
            if (constNumber == -1)
                return negate();
            return new MultiplyStatement(context, variables, multi, deepCopy());
        } else
            return new MultiplyStatement(context, variables, deepCopy(), multi);
    }
}
