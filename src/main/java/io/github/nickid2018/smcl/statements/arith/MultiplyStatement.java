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

import io.github.nickid2018.smcl.DefinedVariables;
import io.github.nickid2018.smcl.SMCLContext;
import io.github.nickid2018.smcl.Statement;
import io.github.nickid2018.smcl.VariableList;
import io.github.nickid2018.smcl.functions.UnaryFunctionStatement;
import io.github.nickid2018.smcl.optimize.NumberPool;
import io.github.nickid2018.smcl.statements.NumberStatement;
import io.github.nickid2018.smcl.statements.Variable;

import java.util.ArrayList;
import java.util.List;

public class MultiplyStatement extends Statement {

    private final List<Statement> multipliers = new ArrayList<>();

    public MultiplyStatement(SMCLContext smcl, DefinedVariables variables) {
        super(smcl, variables);
    }

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
        if (statement.equals(NumberPool.NUMBER_CONST_1))
            return this;
        if (statement.equals(NumberPool.NUMBER_CONST_0)) {
            multipliers.clear();
            multipliers.add(statement);
            return this;
        }
        if (statement.equals(NumberPool.NUMBER_CONST_N1))
            return (MultiplyStatement) getNegative();
        multipliers.add(statement);
        if (isAllNum()) {
            Statement number = NumberPool.get(smcl, calculate(null));
            multipliers.clear();
            multipliers.add(number);
        }
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
                    Statement derivative = normal.get(j).derivative();
                    if (derivative instanceof MultiplyStatement)
                        multi.addMultipliers(((MultiplyStatement) derivative).multipliers.toArray(new Statement[0]));
                    else
                        multi.addMultiplier(derivative);
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
