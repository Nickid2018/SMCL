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
package io.github.nickid2018.smcl.functions;

import io.github.nickid2018.smcl.Statement;
import io.github.nickid2018.smcl.number.NumberPool;
import io.github.nickid2018.smcl.statements.NumberStatement;
import io.github.nickid2018.smcl.statements.arith.DivideStatement;
import io.github.nickid2018.smcl.statements.arith.MathStatement;
import io.github.nickid2018.smcl.statements.arith.MultiplyStatement;
import io.github.nickid2018.smcl.statements.arith.PowerStatement;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * The class is to store derivatives of functions.
 */
public class FunctionDerivatives {

    public static final Function<Statement, Statement> DERIVATIVE_SIN = statement -> Functions.COS
            .create(statement.deepCopy());

    public static final Function<Statement, Statement> DERIVATIVE_COS = statement -> Functions.SIN
            .create(statement.deepCopy()).negate();

    public static final Function<Statement, Statement> DERIVATIVE_TAN = statement -> {
        Statement base = Functions.SEC.create(statement.deepCopy());
        return new PowerStatement(statement.getSMCL(), statement.getVariables(), base,
                NumberPool.getNumber(2));
    };

    public static final Function<Statement, Statement> DERIVATIVE_SEC = statement -> {
        Statement base = Functions.SEC.create(statement.deepCopy());
        Statement base2 = Functions.TAN.create(statement.deepCopy());
        return new MultiplyStatement(statement.getSMCL(), statement.getVariables(), base, base2);
    };

    public static final Function<Statement, Statement> DERIVATIVE_CSC = statement -> {
        Statement base = Functions.CSC.create(statement.deepCopy());
        Statement base2 = Functions.COT.create(statement.deepCopy());
        return new MultiplyStatement(statement.getSMCL(), statement.getVariables(), false,
                base, base2);
    };

    public static final Function<Statement, Statement> DERIVATIVE_COT = statement -> {
        Statement base = Functions.CSC.create(statement.deepCopy());
        return new PowerStatement(statement.getSMCL(), statement.getVariables(), false,
                base, NumberPool.getNumber(2));
    };

    public static final Function<Statement, Statement> DERIVATIVE_ASIN = statement -> {
        Statement pws = new PowerStatement(statement.getSMCL(), statement.getVariables(), false,
                statement.deepCopy(), NumberPool.getNumber(2));
        MathStatement base = new MathStatement(statement.getSMCL(), statement.getVariables(), NumberPool.NUMBER_CONST_1, pws);
        Statement sqrt = Functions.SQRT.create(base);
        return new DivideStatement(statement.getSMCL(), statement.getVariables(), NumberPool.NUMBER_CONST_1, sqrt);
    };

    public static final Function<Statement, Statement> DERIVATIVE_ACOS = statement -> {
        Statement pws = new PowerStatement(statement.getSMCL(), statement.getVariables(), false,
                statement.deepCopy(), NumberPool.getNumber(2));
        MathStatement base = new MathStatement(statement.getSMCL(), statement.getVariables(), NumberPool.NUMBER_CONST_1, pws);
        Statement sqrt = Functions.SQRT.create(base);
        return new DivideStatement(statement.getSMCL(), statement.getVariables(), false, NumberPool.NUMBER_CONST_1, sqrt);
    };

    public static final Function<Statement, Statement> DERIVATIVE_ATAN = statement -> {
        Statement pws = new PowerStatement(statement.getSMCL(), statement.getVariables(), statement.deepCopy(), NumberPool.getNumber(2));
        MathStatement base = new MathStatement(statement.getSMCL(), statement.getVariables(), NumberPool.NUMBER_CONST_1, pws);
        return new DivideStatement(statement.getSMCL(), statement.getVariables(), NumberPool.NUMBER_CONST_1, base);
    };

    public static final Function<Statement, Statement> DERIVATIVE_SINH = statement -> Functions.COSH
            .create(statement.deepCopy());

    public static final Function<Statement, Statement> DERIVATIVE_COSH = statement -> Functions.SINH
            .create(statement.deepCopy());

    public static final Function<Statement, Statement> DERIVATIVE_TANH = statement -> {
        Statement base = Functions.COSH.create(statement.deepCopy());
        return new DivideStatement(statement.getSMCL(), statement.getVariables(),
                NumberPool.NUMBER_CONST_1,
                new PowerStatement(statement.getSMCL(), statement.getVariables(), base, NumberPool.getNumber(2)));
    };

    public static final Function<Statement, Statement> DERIVATIVE_LN = statement -> new DivideStatement(
            statement.getSMCL(), statement.getVariables(), NumberPool.NUMBER_CONST_1, statement.deepCopy());

    public static final Function<Statement, Statement> DERIVATIVE_LG = statement -> new DivideStatement(
            statement.getSMCL(), statement.getVariables(), NumberPool.NUMBER_CONST_1, statement.deepCopy(),
            NumberPool.getNumber(Math.log(10)));

    public static final Function<Statement, Statement> DERIVATIVE_SQRT = statement -> new PowerStatement(
            statement.getSMCL(), statement.getVariables(), statement.deepCopy(), NumberPool.getNumber(-0.5));

    public static final Function<Statement, Statement> DERIVATIVE_CBRT = statement -> new PowerStatement(
            statement.getSMCL(), statement.getVariables(), statement.deepCopy(), NumberPool.getNumber(-2 / 3.0));

    public static final Function<Statement, Statement> DERIVATIVE_EXP = statement -> new PowerStatement(
            statement.getSMCL(), statement.getVariables(), NumberPool.getNumber(Math.E), statement.deepCopy());

    // Warning: Assigned x=0, the derivative doesn't exist!
    public static final Function<Statement, Statement> DERIVATIVE_ABS = statement -> Functions.SGN
            .create(statement.deepCopy());

    public static final BiFunction<Statement, Statement, Statement> DERIVATIVE_LOG = (statement1, statement2) -> {
        if(statement2 instanceof NumberStatement)
            return new DivideStatement(statement1.getSMCL(), statement1.getVariables(),
                    NumberPool.NUMBER_CONST_1, statement1.deepCopy(),
                    NumberPool.getNumber(Math.log(((NumberStatement)statement2).getNumber())));
        // log(f, g) = lnf/lng => (f'/f*lng-g'/g*lnf)/ln2g
        DivideStatement ds = new DivideStatement(statement1.getSMCL(), statement1.getVariables(),
                Functions.LN.create(statement1.deepCopy()),
                Functions.LN.create(statement2.deepCopy()));
        return ds.derivative();
    };
}
