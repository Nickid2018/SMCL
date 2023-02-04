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

import io.github.nickid2018.smcl.VariableList;
import io.github.nickid2018.smcl.SMCLContext;
import io.github.nickid2018.smcl.Statement;
import io.github.nickid2018.smcl.VariableValueList;
import io.github.nickid2018.smcl.functions.UnaryFunctionStatement;
import io.github.nickid2018.smcl.number.NumberPool;
import io.github.nickid2018.smcl.statements.NumberStatement;
import io.github.nickid2018.smcl.statements.Variable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Statement for division.
 */
public class DivideStatement extends Statement {

    private final List<Statement> divisors;
    private final Statement dividend;

    /**
     * Create a statement with a context and a variable list
     * @param smcl a context
     * @param variables a variable list
     * @param dividend a dividend
     * @param divisors a list of divisors
     */
    public DivideStatement(SMCLContext smcl, VariableList variables, Statement dividend, Statement... divisors) {
        this(smcl, variables, false, dividend, Arrays.asList(divisors));
    }

    /**
     * Create a statement with a context and a variable list
     * @param smcl a context
     * @param variables a variable list
     * @param dividend a dividend
     * @param divisors a list of divisors
     */
    public DivideStatement(SMCLContext smcl, VariableList variables, Statement dividend, List<Statement> divisors) {
        this(smcl, variables, false, dividend, divisors);
    }

    /**
     * Create a statement with a context and a variable list
     * @param smcl a context
     * @param variables a variable list
     * @param dividend a dividend
     * @param divisors a list of divisors
     * @param isNegative whether the statement is negative
     */
    public DivideStatement(SMCLContext smcl, VariableList variables, boolean isNegative, Statement dividend, Statement... divisors) {
        this(smcl, variables, isNegative, dividend, Arrays.asList(divisors));
    }

    /**
     * Create a statement with a context and a variable list
     * @param smcl a context
     * @param variables a variable list
     * @param dividend a dividend
     * @param divisors a list of divisors
     * @param isNegative whether the statement is negative
     */
    public DivideStatement(SMCLContext smcl, VariableList variables, boolean isNegative, Statement dividend, List<Statement> divisors) {
        super(smcl, variables, isNegative);
        this.dividend = dividend;
        this.divisors = Collections.unmodifiableList(divisors);
    }

    @Override
    public Statement negate() {
        return new DivideStatement(context, variables, !isNegative,
                dividend.deepCopy(), divisors.stream().map(Statement::deepCopy).collect(Collectors.toList()));
    }

    @Override
    public Statement deepCopy() {
        return new DivideStatement(context, variables, isNegative,
                dividend.deepCopy(), divisors.stream().map(Statement::deepCopy).collect(Collectors.toList()));
    }

    @Override
    public double calculateInternal(VariableValueList list) {
        double ret = dividend.calculate(list);
        for (Statement ms : divisors) {
            double v = ms.calculate(list);
            if (v == 0)
                if (context.settings.invalidArgumentWarn)
                    System.err.println("Warning: divide by 0 at " + this);
                else
                    throw new ArithmeticException("divide by 0");
            ret /= v;
        }
        return ret;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(dividend.toString());
        for (Statement en : divisors) {
            if (!(en instanceof NumberStatement) && !(en instanceof Variable)
                    && !(en instanceof UnaryFunctionStatement))
                sb.append("/(").append(en).append(")");
            else
                sb.append("/").append(en);
        }
        return sb + "";
    }

    public Statement getDividend() {
        return dividend;
    }

    public List<Statement> getDivisors() {
        return divisors;
    }

    @Override
    // (f/g)' = (f'g-fg')/(g^2)
    // g = d1*d2*d3...
    // Optimize:
    // 1) f = C: (C/g)' = -C/(g^2)
    // 2) g = C: (f/C)' = f'/C
    // 3) f = C1, g = C2: (C1/C2)' = 0
    protected Statement derivativeInternal() {
        Statement funcf = dividend;
        Statement funcg;
        if(divisors.size() > 1) {
            funcg = new MultiplyStatement(context, variables, divisors.stream().map(Statement::deepCopy).collect(Collectors.toList()));
        } else
            funcg = divisors.get(0).deepCopy();
        boolean funcfN = funcf instanceof NumberStatement;
        boolean funcgN = funcg instanceof NumberStatement;
        if (funcfN && funcgN)
            return NumberPool.NUMBER_CONST_0;
        if (funcfN) {
            PowerStatement pws = new PowerStatement(context, variables, funcg.deepCopy(), NumberPool.getNumber(2));
            return new DivideStatement(context, variables, funcf.deepCopy(), pws).negate();
        }
        if (funcgN)
            return new DivideStatement(context, variables, funcf.derivative(), funcg.deepCopy());
        Statement derif = funcf.derivative();
        Statement derig = funcg.derivative();
        Statement add1;
        if (derif.equals(NumberPool.NUMBER_CONST_1))
            add1 = funcg.deepCopy();
        else if (derif.equals(NumberPool.NUMBER_CONST_M1))
            add1 = funcg.negate();
        else
            add1 = new MultiplyStatement(context, variables, derif, funcg.deepCopy());
        Statement add2;
        if (derig.equals(NumberPool.NUMBER_CONST_1))
            add2 = funcf.deepCopy();
        else if (derig.equals(NumberPool.NUMBER_CONST_M1))
            add2 = funcf.negate();
        else
            add2 = new MultiplyStatement(context, variables, funcf.deepCopy(), derig);
        MathStatement ms = new MathStatement(context, variables, add1, add2.negate());
        PowerStatement pws = new PowerStatement(context, variables, funcg.deepCopy(), NumberPool.getNumber(2));
        return new DivideStatement(context, variables, ms, pws);
    }

    public DivideStatement getReverse() {
        return new DivideStatement(context, variables,
                new MultiplyStatement(context, variables, divisors.stream().map(Statement::deepCopy).collect(Collectors.toList())),
                dividend
        );
    }

}
