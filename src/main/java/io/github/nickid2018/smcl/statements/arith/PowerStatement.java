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
import io.github.nickid2018.smcl.functions.FunctionStatement;
import io.github.nickid2018.smcl.functions.Functions;
import io.github.nickid2018.smcl.functions.UnaryFunctionStatement;
import io.github.nickid2018.smcl.number.NumberObject;
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
    public NumberObject calculateInternal(VariableValueList list) {
        NumberObject prevExp = exponents.get(exponents.size() - 1).calculate(list);
        for (int now = exponents.size() - 2; now > -2; now--) {
            NumberObject currentBase = now == -1 ? base.calculate(list) : exponents.get(now).calculate(list);
            prevExp = currentBase.power(prevExp);
        }
        return prevExp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (isNegative)
            sb.append('-');
        if (shouldAddParentheses(base))
            sb.append('(').append(base).append(')');
        else
            sb.append(base);
        for (Statement en : exponents) {
            if (shouldAddParentheses(en))
                sb.append("^(").append(en).append(")");
            else
                sb.append("^").append(en);
        }
        return sb.toString();
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
            return new NumberStatement(context, context.numberProvider.getZero());
        if (baseN) {
            NumberObject constNumber = base.calculate(null).log();
            Statement derivative = exponent.derivative();
            if (derivative instanceof NumberStatement)
                constNumber = constNumber.multiply(derivative.calculate(null));
            if (constNumber.isZero())
                return new NumberStatement(context, context.numberProvider.getZero());
            if (constNumber.isOne() || constNumber.isMinusOne())
                return new MultiplyStatement(context, variables, constNumber.isMinusOne(),
                        derivative instanceof NumberStatement ?
                                Collections.singletonList(deepCopy()) :
                                Arrays.asList(deepCopy(), derivative));
            else
                return new MultiplyStatement(context, variables,
                        derivative instanceof NumberStatement ?
                                Arrays.asList(new NumberStatement(context, constNumber), deepCopy()) :
                                Arrays.asList(new NumberStatement(context, constNumber), deepCopy(), derivative));
        }
        if (exponentN) {
            NumberObject exp = exponent.calculate(null);
            NumberObject constNumber = exp;
            if (exp.isZero())
                return new NumberStatement(context, context.numberProvider.getZero());
            if (exp.isOne())
                return base.derivative();
            Statement derivative = base.derivative();
            if (derivative instanceof NumberStatement)
                constNumber = constNumber.multiply(derivative.calculate(null));
            if (constNumber.isZero())
                return new NumberStatement(context, context.numberProvider.getZero());
            Statement trexp = exp.isReal() && exp.toStdNumber() == 2 ?
                    base.deepCopy() :
                    new PowerStatement(context, variables, base.deepCopy(),
                            new NumberStatement(context, exp.subtract(context.numberProvider.getOne())));
            if (constNumber.isOne() || constNumber.isMinusOne()) {
                return new MultiplyStatement(context, variables, constNumber.isMinusOne(),
                        derivative instanceof NumberStatement ?
                                Collections.singletonList(trexp) :
                                Arrays.asList(trexp, derivative));
            } else {
                return new MultiplyStatement(context, variables,
                        derivative instanceof NumberStatement ?
                                Arrays.asList(new NumberStatement(context, constNumber), trexp) :
                                Arrays.asList(new NumberStatement(context, constNumber), trexp, derivative));
            }
        }
        Statement multi = new MultiplyStatement(context, variables,
                exponent.deepCopy(), Functions.LN.create(base.deepCopy())).derivative();
        if (multi instanceof NumberStatement) {
            NumberObject constNumber = multi.calculate(null);
            if (constNumber.isZero())
                return new NumberStatement(context, context.numberProvider.getZero());
            if (constNumber.isOne())
                return deepCopy();
            if (constNumber.isMinusOne())
                return negate();
            return new MultiplyStatement(context, variables, multi, deepCopy());
        } else
            return new MultiplyStatement(context, variables, deepCopy(), multi);
    }
}
