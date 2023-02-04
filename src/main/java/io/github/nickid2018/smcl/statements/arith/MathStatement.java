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
import io.github.nickid2018.smcl.functions.FunctionStatement;
import io.github.nickid2018.smcl.optimize.NumberPool;
import io.github.nickid2018.smcl.statements.NumberStatement;
import io.github.nickid2018.smcl.statements.Variable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A statement stands for calculating plus or minus.
 */
public class MathStatement extends Statement {

    private final List<Statement> subStatements = new ArrayList<>();

    /**
     * Construct a math statement with a context and a variable list.
     * @param smcl a context
     * @param variables a variable list
     */
    public MathStatement(SMCLContext smcl, VariableList variables) {
        super(smcl, variables);
    }

    /**
     * {@inheritDoc}
     */
    public double calculateInternal(VariableValueList list) {
        double t = 0;
        for (Statement en : subStatements) {
            t += en.calculate(list);
        }
        return t;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Statement subStatement : subStatements) {
            if (first) {
                first = false;
                if (subStatement.isNegative() && !isNoSign(subStatement))
                    sb.append("-");
                sb.append(subStatement);
                continue;
            }
            sb.append((isNoSign(subStatement) && subStatement.isNegative()) ? ""
                    : (subStatement.isNegative() ? "-" : "+")).append(subStatement);
        }
        return isNegative ? "-(" + sb.toString() + ")" : sb.toString();
    }

    private static boolean isNoSign(Statement statement) {
        return statement instanceof NumberStatement ||
                statement instanceof Variable ||
                statement instanceof FunctionStatement ||
                statement instanceof MathStatement;
    }

    /**
     * Returns whether the statement only has numbers.
     * @return true if the statement only has numbers
     */
    public boolean isAllNum() {
        for (Statement en : subStatements) {
            if (!(en instanceof NumberStatement))
                return false;
        }
        return true;
    }

    /**
     * Add a statement into the math statement.
     * @param statement another statement
     * @return this
     */
    public MathStatement addStatement(Statement statement) {
        if (statement.equals(this))
            throw new ArithmeticException("Add itself");
        if (statement.equals(NumberPool.NUMBER_CONST_0))
            return this;
        if (merge(statement))
            return this;
        subStatements.add(statement);
        if (isAllNum()) {
            Statement number = NumberPool.getNumber(calculate(null));
            subStatements.clear();
            subStatements.add(number);
        }
        return this;
    }

    /**
     * Add several statements in the math statement.
     * @param statements a set of statements
     * @return this
     */
    public MathStatement addStatements(Statement... statements) {
        for (Statement statement : statements)
            addStatement(statement);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Statement derivativeInternal() {
        MathStatement end = new MathStatement(context, variables);
        for (Statement s : subStatements) {
            Statement derivative = s.derivative();
            if (derivative.equals(NumberPool.NUMBER_CONST_0))
                continue;
            if (derivative instanceof MathStatement)
                end.addStatements(((MathStatement) derivative).subStatements.toArray(new Statement[0]));
            else
                end.addStatement(derivative);
        }
        return end.subStatements.size() > 0 ? (end.isAllNum() ? NumberPool.getNumber(end.calculate(null)) : end)
                : NumberPool.NUMBER_CONST_0;
    }

    @Override
    public boolean merge(Statement statement) {
        if (!(statement instanceof MathStatement))
            return false;
        MathStatement other = (MathStatement) statement;
        subStatements.addAll(other.subStatements.stream()
                .map(st -> other.isNegative ? st.getNegative() : st.getClone()).collect(Collectors.toList()));
        return true;
    }
}
