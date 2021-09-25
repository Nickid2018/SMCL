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
import io.github.nickid2018.smcl.optimize.NumberPool;
import io.github.nickid2018.smcl.statements.NumberStatement;
import io.github.nickid2018.smcl.statements.Variable;

import java.util.ArrayList;
import java.util.List;

/**
 * A statement stands for calculating plus or minus.
 */
public class MathStatement extends Statement {

    private final List<Statement> subs = new ArrayList<>();

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
        for (Statement en : subs) {
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
        for (Statement en : subs) {
            if (first) {
                first = false;
                if (en.isNegative() && !(en instanceof NumberStatement || en instanceof Variable))
                    sb.append("-");
                sb.append(en);
                continue;
            }
            sb.append(((en instanceof NumberStatement || en instanceof Variable) && en.isNegative()) ? ""
                    : (en.isNegative() ? "-" : "+")).append(en);
        }
        return sb.toString();
    }

    /**
     * Returns whether the statement only has numbers.
     * @return true if the statement only has numbers
     */
    public boolean isAllNum() {
        for (Statement en : subs) {
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
        if(statement.equals(this))
            throw new ArithmeticException("Add itself");
        if (statement.equals(NumberPool.NUMBER_CONST_0))
            return this;
        subs.add(statement);
        if (isAllNum()) {
            Statement number = NumberPool.get(smcl, calculate(null));
            subs.clear();
            subs.add(number);
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
        MathStatement end = new MathStatement(smcl, variables);
        for (Statement s : subs) {
            Statement derivative = s.derivative();
            if (derivative.equals(NumberPool.NUMBER_CONST_0))
                continue;
            if (derivative instanceof MathStatement)
                end.addStatements(((MathStatement) derivative).subs.toArray(new Statement[0]));
            else
                end.addStatement(derivative);
        }
        return end.subs.size() > 0 ? (end.isAllNum() ? NumberPool.getNumber(end.calculate(null)) : end)
                : NumberPool.NUMBER_CONST_0;
    }
}