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
import io.github.nickid2018.smcl.functions.FunctionStatement;
import io.github.nickid2018.smcl.number.NumberPool;
import io.github.nickid2018.smcl.statements.NumberStatement;
import io.github.nickid2018.smcl.statements.Variable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A statement stands for calculating plus or minus.
 */
public class MathStatement extends Statement {

    private final List<Statement> subStatements;

    /**
     * Construct a math statement with a context and a variable list.
     * @param smcl a context
     * @param variables a variable list
     * @param subStatements sub statements
     */
    public MathStatement(SMCLContext smcl, VariableList variables, Statement... subStatements) {
        this(smcl, variables, false, subStatements);
    }

    /**
     * Construct a math statement with a context and a variable list.
     * @param smcl a context
     * @param variables a variable list
     * @param subStatements sub statements
     */
    public MathStatement(SMCLContext smcl, VariableList variables, List<Statement> subStatements) {
        this(smcl, variables, false, subStatements);
    }

    /**
     * Construct a math statement with a context and a variable list.
     * @param smcl a context
     * @param variables a variable list
     * @param subStatements sub statements
     * @param isNegative whether the statement is negative
     */
    public MathStatement(SMCLContext smcl, VariableList variables, boolean isNegative, Statement... subStatements) {
        this(smcl, variables, isNegative, Arrays.asList(subStatements));
    }

    /**
     * Construct a math statement with a context and a variable list.
     * @param smcl a context
     * @param variables a variable list
     * @param subStatements sub statements
     * @param isNegative whether the statement is negative
     */
    public MathStatement(SMCLContext smcl, VariableList variables, boolean isNegative, List<Statement> subStatements) {
        super(smcl, variables, isNegative);
        this.subStatements = Collections.unmodifiableList(subStatements);
    }


    @Override
    public Statement negate() {
        return new MathStatement(context, variables, !isNegative, subStatements.stream().map(Statement::deepCopy).collect(Collectors.toList()));
    }

    @Override
    public Statement deepCopy() {
        return new MathStatement(context, variables, isNegative, subStatements.stream().map(Statement::deepCopy).collect(Collectors.toList()));
    }

    public double calculateInternal(VariableValueList list) {
        double t = 0;
        for (Statement en : subStatements) {
            t += en.calculate(list);
        }
        return t;
    }

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
        return isNegative ? "-(" + sb + ")" : sb.toString();
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

    @Override
    protected Statement derivativeInternal() {
        List<Statement> list = new ArrayList<>();
        for (Statement s : subStatements) {
            Statement derivative = s.derivative();
            if (derivative.equals(NumberPool.NUMBER_CONST_0))
                continue;
            if (derivative instanceof MathStatement)
                ((MathStatement) derivative).subStatements.stream().map(Statement::deepCopy).forEach(list::add);
            else
                list.add(derivative);
        }
        MathStatement end = new MathStatement(context, variables, list);
        return end.subStatements.size() > 0 ? (end.isAllNum() ? NumberPool.getNumber(end.calculate(null)) : end)
                : NumberPool.NUMBER_CONST_0;
    }

}
