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
 * A statement stands for multiplication.
 */
public class MultiplyStatement extends Statement {

    private final List<Statement> multipliers = new ArrayList<>();

    /**
     * Construct a multiply statement with a context and a variable list.
     * @param smcl a context
     * @param variables a variable list
     */
    public MultiplyStatement(SMCLContext smcl, VariableList variables) {
        super(smcl, variables);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double calculateInternal(VariableValueList list) {
        double all = 1;
        for (Statement ms : multipliers) {
            all *= ms.calculate(list);
        }
        return all;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * Returns whether the statement only has numbers.
     * @return true if the statement only has numbers
     */
    public boolean isAllNum() {
        for (Statement statement : multipliers)
            if (!(statement instanceof NumberStatement))
                return false;
        return true;
    }

    /**
     * Add a statement into the multiply statement.
     * @param statement another statement
     * @return this
     */
    public MultiplyStatement addMultiplier(Statement statement) {
        if(statement.equals(this))
            throw new ArithmeticException("Multiply itself");
        if (multipliers.size() > 0 && multipliers.get(0).equals(NumberPool.NUMBER_CONST_0))
            return this;
        if (merge(statement))
            return this;
        if (statement.equals(NumberPool.NUMBER_CONST_1))
            return this;
        if (statement.equals(NumberPool.NUMBER_CONST_0)) {
            multipliers.clear();
            multipliers.add(statement);
            return this;
        }
        if (statement.equals(NumberPool.NUMBER_CONST_M1))
            return (MultiplyStatement) getNegative();
        multipliers.add(statement);
        if (isAllNum()) {
            Statement number = NumberPool.getNumber(calculate(null));
            multipliers.clear();
            multipliers.add(number);
        }
        return this;
    }

    /**
     * Add several statements in the multiply statement.
     * @param statements a set of statements
     * @return this
     */
    public MultiplyStatement addMultipliers(Statement... statements) {
        for (Statement statement : statements)
            addMultiplier(statement);
        return this;
    }

    /**
     * {@inheritDoc}
     */
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
                normal.add(statement.getClone());
        }
        if (constNumber == 0)
            return NumberPool.NUMBER_CONST_0;
        MathStatement ms = new MathStatement(context, variables);
        for (int i = 0; i < normal.size(); i++) {
            MultiplyStatement multi = new MultiplyStatement(context, variables);
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
        return new MultiplyStatement(context, variables).addMultipliers(NumberPool.getNumber(constNumber), ms);
    }

    @Override
    public boolean merge(Statement statement) {
        if (!(statement instanceof MultiplyStatement))
            return false;
        multipliers.addAll(((MultiplyStatement) statement).multipliers.stream().map(Statement::getClone).collect(Collectors.toList()));
        return true;
    }
}
