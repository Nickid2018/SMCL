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
import io.github.nickid2018.smcl.functions.UnaryFunctionStatement;
import io.github.nickid2018.smcl.optimize.NumberPool;
import io.github.nickid2018.smcl.statements.NumberStatement;
import io.github.nickid2018.smcl.statements.Variable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Statement for division.
 */
public class DivideStatement extends Statement {

    private final List<Statement> divisors = new ArrayList<>();
    private Statement dividend;

    /**
     * Create a statement with a context and a variable list
     * @param smcl a context
     * @param variables a variable list
     */
    public DivideStatement(SMCLContext smcl, VariableList variables) {
        super(smcl, variables);
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * Add a divisor at end.
     * @param statement a divisor
     * @return this
     */
    public DivideStatement addDivisor(Statement statement) {
        if (statement.equals(NumberPool.NUMBER_CONST_0))
            throw new ArithmeticException("divide by 0");
        if (statement.equals(NumberPool.NUMBER_CONST_1))
            return this;
        if (statement instanceof DivideStatement) {
            merge(((DivideStatement) statement).getReverse());
            return this;
        }
        if (statement.equals(NumberPool.NUMBER_CONST_M1))
            return (DivideStatement) getNegative();
        if (dividend instanceof NumberStatement && statement instanceof NumberStatement) {
            dividend = NumberPool.getNumber(dividend.calculate(null) / statement.calculate(null));
            return this;
        }
        divisors.add(statement);
        return this;
    }

    /**
     * Set dividend and divisors.
     * @param statements dividend and divisors
     * @return this
     */
    public DivideStatement putDividendAndDivisors(Statement... statements) {
        dividend = statements[0];
        for (int i = 1; i < statements.length; i++)
            addDivisor(statements[i]);
        return this;
    }

    /**
     * {@inheritDoc}
     */
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
            funcg = new MultiplyStatement(context, variables);
            for(Statement statement : divisors)
                ((MultiplyStatement) funcg).addMultiplier(statement.getClone());
        } else
            funcg = divisors.get(0).getClone();
        boolean funcfN = funcf instanceof NumberStatement;
        boolean funcgN = funcg instanceof NumberStatement;
        if (funcfN && funcgN)
            return NumberPool.NUMBER_CONST_0;
        if (funcfN) {
            PowerStatement pws = new PowerStatement(context, variables).putBaseAndExponents(funcg,
                    NumberPool.getNumber(2));
            return new DivideStatement(context, variables).putDividendAndDivisors(funcf.getClone(), pws).getNegative();
        }
        if (funcgN)
            return new DivideStatement(context, variables).putDividendAndDivisors(funcf.derivative(), funcg);
        Statement derif = funcf.derivative();
        Statement derig = funcg.derivative();
        Statement add1;
        if (derif.equals(NumberPool.NUMBER_CONST_1))
            add1 = funcg.getClone();
        else if (derif.equals(NumberPool.NUMBER_CONST_M1))
            add1 = funcg.getNewNegative();
        else
            add1 = new MultiplyStatement(context, variables).addMultipliers(derif, funcg);
        Statement add2;
        if (derig.equals(NumberPool.NUMBER_CONST_1))
            add2 = funcf.getClone();
        else if (derig.equals(NumberPool.NUMBER_CONST_M1))
            add2 = funcf.getNewNegative();
        else
            add2 = new MultiplyStatement(context, variables).addMultipliers(funcf.getClone(), derig);
        MathStatement ms = new MathStatement(context, variables).addStatements(add1, add2.getNegative());
        PowerStatement pws = new PowerStatement(context, variables).putBaseAndExponents(funcg, NumberPool.getNumber(2));
        return new DivideStatement(context, variables).putDividendAndDivisors(ms, pws);
    }

    public DivideStatement getReverse() {
        return new DivideStatement(context, variables).putDividendAndDivisors(
                new MultiplyStatement(context, variables).addMultipliers(divisors.toArray(new Statement[0])),
                dividend
        );
    }

    @Override
    public boolean merge(Statement statement) {
        if (!(statement instanceof DivideStatement))
            return false;
        MultiplyStatement newDividend = new MultiplyStatement(context, variables);
        newDividend.addMultipliers(dividend, ((DivideStatement) statement).dividend);
        dividend = newDividend;
        divisors.addAll(((DivideStatement) statement).divisors.stream().map(Statement::getClone).collect(Collectors.toList()));
        return true;
    }
}
