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
import io.github.nickid2018.smcl.number.NumberObject;
import io.github.nickid2018.smcl.statements.NumberStatement;

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
    public NumberObject calculateInternal(VariableValueList list) {
        NumberObject all = context.numberProvider.getOne();
        for (Statement ms : multipliers)
            all = all.multiply(ms.calculate(list));
        return all;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        List<String> pieces = new ArrayList<>();
        if (isNegative)
            sb.append("-");
        for (Statement ms : multipliers) {
            if (shouldAddParentheses(ms))
                pieces.add("(" + ms + ")");
            else
                pieces.add(ms.toString());
        }
        sb.append(String.join("*", pieces));
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

    public List<Statement> getMultipliers() {
        return multipliers;
    }

    @Override
    // (f1f2..fn)' = f1'f2f3...fn+f1f2'f3...fn+f1f2f3'...fn+...+f1f2f3...fn'
    // Optimize
    // 0..., =0
    protected Statement derivativeInternal() {
        NumberObject constNumber = context.numberProvider.getOne();
        List<Statement> normal = new ArrayList<>();
        for (Statement statement : multipliers) {
            if (statement instanceof NumberStatement)
                constNumber = constNumber.multiply(statement.calculate(null));
            else
                normal.add(statement.deepCopy());
        }
        if (constNumber.isZero())
            return new NumberStatement(context, context.numberProvider.getZero());
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
                statements.add(new NumberStatement(context, multi.calculate(null)));
            else
                statements.add(multi);
        }
        MathStatement ms = new MathStatement(context, variables, statements);
        if (ms.isAllNum())
            return new NumberStatement(ms.getSMCL(), constNumber.multiply(ms.calculate(null)));
        if (constNumber.isOne())
            return ms;
        if (constNumber.isMinusOne())
            return ms.negate();
        return new MultiplyStatement(context, variables, new NumberStatement(context, constNumber), ms);
    }

}
