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
import io.github.nickid2018.smcl.optimize.NumberPool;
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
            .create(statement.getSMCL(), statement.getClone());

    public static final Function<Statement, Statement> DERIVATIVE_COS = statement -> Functions.SIN
            .create(statement.getSMCL(), statement.getClone()).getNegative();

    public static final Function<Statement, Statement> DERIVATIVE_TAN = statement -> {
        Statement base = Functions.SEC.create(statement.getSMCL(), statement.getClone());
        return new PowerStatement(statement.getSMCL(), statement.getVariables()).putBaseAndExponents(base,
                NumberPool.getNumber(2));
    };

    public static final Function<Statement, Statement> DERIVATIVE_SEC = statement -> {
        Statement base = Functions.SEC.create(statement.getSMCL(), statement.getClone());
        Statement base2 = Functions.TAN.create(statement.getSMCL(), statement.getClone());
        return new MultiplyStatement(statement.getSMCL(), statement.getVariables()).addMultipliers(base, base2);
    };

    public static final Function<Statement, Statement> DERIVATIVE_CSC = statement -> {
        Statement base = Functions.CSC.create(statement.getSMCL(), statement.getClone());
        Statement base2 = Functions.COT.create(statement.getSMCL(), statement.getClone());
        return new MultiplyStatement(statement.getSMCL(), statement.getVariables()).addMultipliers(base, base2)
                .getNegative();
    };

    public static final Function<Statement, Statement> DERIVATIVE_COT = statement -> {
        Statement base = Functions.CSC.create(statement.getSMCL(), statement.getClone());
        return new PowerStatement(statement.getSMCL(), statement.getVariables())
                .putBaseAndExponents(base, NumberPool.getNumber(2)).getNegative();
    };

    public static final Function<Statement, Statement> DERIVATIVE_ASIN = statement -> {
        Statement pws = new PowerStatement(statement.getSMCL(), statement.getVariables())
                .putBaseAndExponents(statement.getClone(), NumberPool.getNumber(2)).getNegative();
        MathStatement base = new MathStatement(statement.getSMCL(), statement.getVariables());
        base.addStatements(NumberPool.NUMBER_CONST_1, pws);
        Statement sqrt = Functions.SQRT.create(statement.getSMCL(), base);
        return new DivideStatement(statement.getSMCL(), statement.getVariables())
                .putDividendAndDivisors(NumberPool.NUMBER_CONST_1, sqrt);
    };

    public static final Function<Statement, Statement> DERIVATIVE_ACOS = statement -> {
        Statement pws = new PowerStatement(statement.getSMCL(), statement.getVariables())
                .putBaseAndExponents(statement.getClone(), NumberPool.getNumber(2)).getNegative();
        MathStatement base = new MathStatement(statement.getSMCL(), statement.getVariables());
        base.addStatements(NumberPool.NUMBER_CONST_1, pws);
        Statement sqrt = Functions.SQRT.create(statement.getSMCL(), base);
        return new DivideStatement(statement.getSMCL(), statement.getVariables())
                .putDividendAndDivisors(NumberPool.NUMBER_CONST_1, sqrt).getNegative();
    };

    public static final Function<Statement, Statement> DERIVATIVE_ATAN = statement -> {
        Statement pws = new PowerStatement(statement.getSMCL(), statement.getVariables())
                .putBaseAndExponents(statement.getClone(), NumberPool.getNumber(2));
        MathStatement base = new MathStatement(statement.getSMCL(), statement.getVariables());
        base.addStatements(NumberPool.NUMBER_CONST_1, pws);
        return new DivideStatement(statement.getSMCL(), statement.getVariables())
                .putDividendAndDivisors(NumberPool.NUMBER_CONST_1, base);
    };

    public static final Function<Statement, Statement> DERIVATIVE_SINH = statement -> Functions.COSH
            .create(statement.getSMCL(), statement.getClone());

    public static final Function<Statement, Statement> DERIVATIVE_COSH = statement -> Functions.SINH
            .create(statement.getSMCL(), statement.getClone());

    public static final Function<Statement, Statement> DERIVATIVE_TANH = statement -> {
        Statement base = Functions.COSH.create(statement.getSMCL(), statement.getClone());
        return new DivideStatement(statement.getSMCL(), statement.getVariables()).putDividendAndDivisors(
                NumberPool.NUMBER_CONST_1, new PowerStatement(statement.getSMCL(), statement.getVariables())
                        .putBaseAndExponents(base, NumberPool.getNumber(2)));
    };

    public static final Function<Statement, Statement> DERIVATIVE_LN = statement -> new DivideStatement(
            statement.getSMCL(), statement.getVariables()).putDividendAndDivisors(NumberPool.NUMBER_CONST_1, statement.getClone());

    public static final Function<Statement, Statement> DERIVATIVE_LG = statement -> new DivideStatement(
            statement.getSMCL(), statement.getVariables()).putDividendAndDivisors(NumberPool.NUMBER_CONST_1, statement.getClone(),
            NumberPool.getNumber(Math.log(10)));

    public static final Function<Statement, Statement> DERIVATIVE_SQRT = statement -> new PowerStatement(
            statement.getSMCL(), statement.getVariables()).putBaseAndExponents(statement.getClone(), NumberPool.getNumber(-0.5));

    public static final Function<Statement, Statement> DERIVATIVE_CBRT = statement -> new PowerStatement(
            statement.getSMCL(), statement.getVariables()).putBaseAndExponents(statement.getClone(), NumberPool.getNumber(-2 / 3.0));

    public static final Function<Statement, Statement> DERIVATIVE_EXP = statement -> new PowerStatement(
            statement.getSMCL(), statement.getVariables()).putBaseAndExponents(NumberPool.getNumber(Math.E), statement.getClone());

    // Warning: Assigned x=0, the derivative doesn't exist!
    public static final Function<Statement, Statement> DERIVATIVE_ABS = statement -> Functions.SGN
            .create(statement.getSMCL(), statement.getClone());

    public static final BiFunction<Statement, Statement, Statement> DERIVATIVE_LOG = (statement1, statement2) -> {
        if(statement2 instanceof NumberStatement) {
            DivideStatement ds = new DivideStatement(statement1.getSMCL(), statement1.getVariables());
            ds.putDividendAndDivisors(NumberPool.NUMBER_CONST_1, statement1.getClone(),
                    NumberPool.getNumber(Math.log(((NumberStatement)statement2).getNumber())));
            return ds;
        }
        // log(f, g) = lnf/lng => (f'/f*lng-g'/g*lnf)/ln2g
        DivideStatement ds = new DivideStatement(statement1.getSMCL(), statement1.getVariables());
        ds.putDividendAndDivisors(Functions.LN.create(statement1.getSMCL(), statement1.getClone()),
                Functions.LN.create(statement2.getSMCL(), statement2.getClone()));
        return ds.derivative();
    };
}
