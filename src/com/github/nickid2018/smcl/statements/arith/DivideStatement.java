/*
 * Copyright 2021 Nickid2018
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.nickid2018.smcl.statements.arith;

import java.util.*;

import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.optimize.*;
import com.github.nickid2018.smcl.functions.*;
import com.github.nickid2018.smcl.statements.*;

public class DivideStatement extends Statement {

    public DivideStatement(SMCL smcl, DefinedVariables variables) {
        super(smcl, variables);
    }

    private Statement dividend;
    private final List<Statement> divisors = new ArrayList<>();

    @Override
    public double calculateInternal(VariableList list) {
        double ret = dividend.calculate(list);
        for (Statement ms : divisors) {
            double v = ms.calculate(list);
            if (v == 0)
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

    public DivideStatement addDivisor(Statement statement) {
        divisors.add(statement);
        return this;
    }

    public DivideStatement putDividendAndDivisors(Statement... statements) {
        dividend = statements[0];
        for (int i = 1; i < statements.length; i++) {
            addDivisor(statements[i]);
        }
        return this;
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
        Statement funcg = divisors.size() > 1
                ? new MultiplyStatement(smcl, variables).addMultipliers(divisors.toArray(new Statement[0]))
                : divisors.get(0);
        boolean funcfN = funcf instanceof NumberStatement;
        boolean funcgN = funcg instanceof NumberStatement;
        if (funcfN && funcgN)
            return NumberPool.NUMBER_CONST_0;
        if (funcfN) {
            PowerStatement pws = new PowerStatement(smcl, variables).putBaseAndExponents(funcg,
                    NumberPool.getNumber(2));
            return new DivideStatement(smcl, variables).putDividendAndDivisors(funcf, pws).getNegative();
        }
        if (funcgN) {
            return new DivideStatement(smcl, variables).putDividendAndDivisors(funcf.derivative(), funcg);
        }
        Statement derif = funcf.derivative();
        Statement derig = funcg.derivative();
        Statement add1;
        if (derif.equals(NumberPool.NUMBER_CONST_1))
            add1 = funcg;
        else if (derif.equals(NumberPool.NUMBER_CONST_N1))
            add1 = funcg.getNewNegative();
        else
            add1 = new MultiplyStatement(smcl, variables).addMultipliers(derif, funcg);
        Statement add2;
        if (derig.equals(NumberPool.NUMBER_CONST_1))
            add2 = funcf;
        else if (derig.equals(NumberPool.NUMBER_CONST_N1))
            add2 = funcf.getNewNegative();
        else
            add2 = new MultiplyStatement(smcl, variables).addMultipliers(funcf, derig);
        MathStatement ms = new MathStatement(smcl, variables).addStatements(add1, add2.getNegative());
        PowerStatement pws = new PowerStatement(smcl, variables).putBaseAndExponents(funcg, NumberPool.getNumber(2));
        return new DivideStatement(smcl, variables).putDividendAndDivisors(ms, pws);
    }
}
