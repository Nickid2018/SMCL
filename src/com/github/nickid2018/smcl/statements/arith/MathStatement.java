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
import com.github.nickid2018.smcl.statements.*;

public class MathStatement extends Statement {

    public MathStatement(SMCL smcl, DefinedVariables variables) {
        super(smcl, variables);
    }

    private final List<Statement> subs = new ArrayList<>();

    public double calculateInternal(VariableList list) {
        double t = 0;
        for (Statement en : subs) {
            t += en.calculate(list);
        }
        return t;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Statement en : subs) {
            if (first) {
                first = false;
                if (en.isNegative() && !(en instanceof NumberStatement || en instanceof Variable))
                    sb.append("-");
                sb.append(en.toString());
                continue;
            }
            sb.append(((en instanceof NumberStatement || en instanceof Variable) && en.isNegative()) ? ""
                    : (en.isNegative() ? "-" : "+")).append(en.toString());
        }
        return sb.toString();
    }

    public boolean isAllNum() {
        for (Statement en : subs) {
            if (!(en instanceof NumberStatement))
                return false;
        }
        return true;
    }

    public MathStatement addStatement(Statement statement) {
        subs.add(statement);
        return this;
    }

    public MathStatement addStatements(Statement... statements) {
        for (Statement statement : statements)
            addStatement(statement);
        return this;
    }

    @Override
    protected Statement derivativeInternal() {
        MathStatement end = new MathStatement(smcl, variables);
        for (Statement s : subs) {
            Statement deri = s.derivative();
            if (deri.equals(NumberPool.NUMBER_CONST_0))
                continue;
            if (deri instanceof MathStatement)
                end.addStatements(((MathStatement) deri).subs.toArray(new Statement[0]));
            else
                end.addStatement(deri);
        }
        return end.subs.size() > 0 ? (end.isAllNum() ? NumberPool.getNumber(end.calculate(null)) : end)
                : NumberPool.NUMBER_CONST_0;
    }
}
