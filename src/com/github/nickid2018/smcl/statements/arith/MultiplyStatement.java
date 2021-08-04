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

public class MultiplyStatement extends Statement {

    public MultiplyStatement(SMCL smcl, DefinedVariables variables) {
        super(smcl, variables);
    }

    private final List<Statement> multipliers = new ArrayList<>();

    @Override
    public double calculateInternal(VariableList list) {
        double all = 1;
        for (Statement ms : multipliers) {
            all *= ms.calculate(list);
        }
        return all;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Statement ms : multipliers) {
            if (first)
                first = false;
            else
                sb.append("*");
            if ((ms.getClass().equals(Variable.class)
                    || ms.getClass().getSuperclass().equals(UnaryFunctionStatement.class)
                    || ms instanceof NumberStatement) && !ms.isNegative())
                sb.append(ms);
            else
                sb.append("(").append(ms).append(")");
        }
        return sb.toString();
    }

    public boolean isAllNum() {
        for (Statement statement : multipliers)
            if (!(statement instanceof NumberStatement))
                return false;
        return true;
    }

    public MultiplyStatement addMultiplier(Statement statement) {
        multipliers.add(statement);
        return this;
    }

    public MultiplyStatement addMultipliers(Statement... statements) {
        for (Statement statement : statements)
            addMultiplier(statement);
        return this;
    }

    @Override
    // (f1f2..fn)' = f1'f2f3...fn+f1f2'f3...fn+f1f2f3'...fn+...+f1f2f3...fn'
    // Optimize
    // 0..., =0
    protected Statement derivativeInternal() {
        double constNumber = 1.0;
        List<Statement> normal = new ArrayList<>();
        for (Statement statement : multipliers) {
            if (statement instanceof NumberStatement)
                constNumber *= statement.calculate(null);
            else
                normal.add(statement);
        }
        if (constNumber == 0)
            return NumberPool.NUMBER_CONST_0;
        MathStatement ms = new MathStatement(smcl, variables);
        for (int i = 0; i < normal.size(); i++) {
            MultiplyStatement multi = new MultiplyStatement(smcl, variables);
            for (int j = 0; j < normal.size(); j++) {
                if (i == j) {
                    Statement deri = normal.get(j).derivative();
                    if (deri instanceof MultiplyStatement)
                        multi.addMultipliers(((MultiplyStatement) deri).multipliers.toArray(new Statement[0]));
                    else
                        multi.addMultiplier(deri);
                } else {
                    Statement st = normal.get(j);
                    if (st instanceof MultiplyStatement)
                        multi.addMultipliers(((MultiplyStatement) st).multipliers.toArray(new Statement[0]));
                    else
                        multi.addMultiplier(st);
                }
            }
            if (multi.isAllNum())
                ms.addStatement(NumberPool.getNumber(multi.calculate(null)));
            else
                ms.addStatement(multi);
        }
        if (ms.isAllNum())
            return NumberPool.getNumber(constNumber * ms.calculate(null));
        if (constNumber == 1)
            return ms;
        if (constNumber == -1)
            return ms.getNegative();
        return new MultiplyStatement(smcl, variables).addMultipliers(NumberPool.getNumber(constNumber), ms);
    }
}
