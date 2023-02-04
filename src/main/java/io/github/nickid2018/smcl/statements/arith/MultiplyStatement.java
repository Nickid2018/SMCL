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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A statement stands for multiplication.
 */
public class MultiplyStatement extends Statement {

    private final List<Statement> multipliers;

    /**
     * Construct a multiply statement with a context and a variable list.
     * @param smcl a context
     * @param variables a variable list
     * @param multipliers multipliers
     */
    public MultiplyStatement(SMCLContext smcl, VariableList variables, Statement... multipliers) {
        this(smcl, variables, false, multipliers);
    }

    /**
     * Construct a multiply statement with a context and a variable list.
     * @param smcl a context
     * @param variables a variable list
     * @param multipliers multipliers
     */
    public MultiplyStatement(SMCLContext smcl, VariableList variables, List<Statement> multipliers) {
        this(smcl, variables, false, multipliers);
    }

    /**
     * Construct a multiply statement with a context and a variable list.
     * @param smcl a context
     * @param variables a variable list
     * @param multipliers multipliers
     * @param isNegative whether the statement is negative
     */
    public MultiplyStatement(SMCLContext smcl, VariableList variables, boolean isNegative, Statement... multipliers) {
        this(smcl, variables, isNegative, Arrays.asList(multipliers));
    }

    /**
     * Construct a multiply statement with a context and a variable list.
     * @param smcl a context
     * @param variables a variable list
     * @param multipliers multipliers
     * @param isNegative whether the statement is negative
     */
    public MultiplyStatement(SMCLContext smcl, VariableList variables, boolean isNegative, List<Statement> multipliers) {
        super(smcl, variables, isNegative);
        this.multipliers = Collections.unmodifiableList(multipliers);
    }

    @Override
    public Statement negate() {
        return new MultiplyStatement(context, variables, !isNegative, multipliers.stream().map(Statement::negate).collect(Collectors.toList()));
    }

    @Override
    public Statement deepCopy() {
        return new MultiplyStatement(context, variables, isNegative, multipliers.stream().map(Statement::deepCopy).collect(Collectors.toList()));
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
                normal.add(statement.deepCopy());
        }
        if (constNumber == 0)
            return NumberPool.NUMBER_CONST_0;
        List<Statement> statements = new ArrayList<>();
        for (int i = 0; i < normal.size(); i++) {
            List<Statement> list = new ArrayList<>();
            for (int j = 0; j < normal.size(); j++) {
                if (i == j) {
                    Statement derivative = normal.get(j).derivative();
                    if (derivative instanceof MultiplyStatement)
                        ((MultiplyStatement) derivative).multipliers.stream().map(Statement::deepCopy).forEach(list::add);
                    else
                        list.add(derivative);
                } else {
                    Statement st = normal.get(j);
                    if (st instanceof MultiplyStatement)
                        ((MultiplyStatement) st).multipliers.stream().map(Statement::deepCopy).forEach(list::add);
                    else
                       list.add(st);
                }
            }
            MultiplyStatement multi = new MultiplyStatement(context, variables, list);
            if (multi.isAllNum())
                statements.add(NumberPool.getNumber(multi.calculate(null)));
            else
                statements.add(multi);
        }
        MathStatement ms = new MathStatement(context, variables, statements);
        if (ms.isAllNum())
            return NumberPool.getNumber(constNumber * ms.calculate(null));
        if (constNumber == 1)
            return ms;
        if (constNumber == -1)
            return ms.negate();
        return new MultiplyStatement(context, variables, NumberPool.getNumber(constNumber), ms);
    }

}
