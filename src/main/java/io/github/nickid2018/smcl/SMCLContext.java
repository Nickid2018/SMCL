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
package io.github.nickid2018.smcl;

import io.github.nickid2018.smcl.functions.Functions;
import io.github.nickid2018.smcl.parser.BinaryOperatorParser;
import io.github.nickid2018.smcl.parser.StatementGenerator;
import io.github.nickid2018.smcl.parser.UnaryMathematicsFunctionParser;
import io.github.nickid2018.smcl.parser.UnaryOperatorParser;
import io.github.nickid2018.smcl.statements.arith.DivideStatement;
import io.github.nickid2018.smcl.statements.arith.MathStatement;
import io.github.nickid2018.smcl.statements.arith.MultiplyStatement;
import io.github.nickid2018.smcl.statements.arith.PowerStatement;

public class SMCLContext {

    public static final VariableList EMPTY_ARGS = new VariableList();

    private static SMCLContext instance;
    public final SMCLSettings settings;
    public final SMCLRegister register;
    public final GlobalVariables globalvars;
    public SMCLContext(SMCLSettings setting) {
        settings = setting;
        register = new SMCLRegister(this);
        globalvars = new GlobalVariables();
    }

    public static synchronized SMCLContext getInstance() {
        return instance == null ? instance = new SMCLContext(new SMCLSettings()) : instance;
    }

    public final void init() {
        register.registerOperator("+", new BinaryOperatorParser<>(20, true, (smcl, statements, variables) -> {
            if (statements[0] instanceof MathStatement) {
                MathStatement get = (MathStatement) statements[0];
                return get.addStatement(statements[1]);
            } else {
                MathStatement statement = new MathStatement(smcl, variables);
                return statement.addStatements(statements);
            }
        }));
        register.registerUnaryOperator("+",
                new UnaryOperatorParser<>(60, false, (smcl, statement, variables) -> statement));
        register.registerOperator("-", new BinaryOperatorParser<>(20, true, (smcl, statements, variables) -> {
            if (statements[0] instanceof MathStatement) {
                MathStatement get = (MathStatement) statements[0];
                Statement other = statements[1];
                return get.addStatement(other.getNegative());
            } else {
                MathStatement statement = new MathStatement(smcl, variables);
                return statement.addStatements(statements[0], statements[1].getNegative());
            }
        }));
        register.registerUnaryOperator("-",
                new UnaryOperatorParser<>(60, false, (smcl, statement, variables) -> statement.getNegative()));
        register.registerOperator("*", new BinaryOperatorParser<>(30, true, (smcl, statements, variables) -> {
            if (statements[0] instanceof MultiplyStatement) {
                MultiplyStatement get = (MultiplyStatement) statements[0];
                Statement other = statements[1];
                return get.addMultiplier(other);
            } else {
                MultiplyStatement statement = new MultiplyStatement(smcl, variables);
                return statement.addMultipliers(statements);
            }
        }));
        register.registerOperator("/", new BinaryOperatorParser<>(30, true, (smcl, statements, variables) -> {
            if (statements[0] instanceof DivideStatement) {
                DivideStatement get = (DivideStatement) statements[0];
                Statement other = statements[1];
                return get.addDivisor(other);
            } else {
                DivideStatement statement = new DivideStatement(smcl, variables);
                return statement.putDividendAndDivisors(statements);
            }
        }));
        register.registerOperator("^", new BinaryOperatorParser<>(40, true, (smcl, statements, variables) -> {
            if (statements[0] instanceof PowerStatement) {
                PowerStatement get = (PowerStatement) statements[0];
                Statement other = statements[1];
                return get.addExponent(other);
            } else {
                PowerStatement statement = new PowerStatement(smcl, variables);
                return statement.putBaseAndExponents(statements);
            }
        }));
        register.registerFunction("sin", new UnaryMathematicsFunctionParser<>(Functions.SIN));
        register.registerFunction("cos", new UnaryMathematicsFunctionParser<>(Functions.COS));
        register.registerFunction("tan", new UnaryMathematicsFunctionParser<>(Functions.TAN));
        register.registerFunction("csc", new UnaryMathematicsFunctionParser<>(Functions.CSC));
        register.registerFunction("sec", new UnaryMathematicsFunctionParser<>(Functions.SEC));
        register.registerFunction("cot", new UnaryMathematicsFunctionParser<>(Functions.COT));
        register.registerFunction("asin", new UnaryMathematicsFunctionParser<>(Functions.ASIN));
        register.registerFunction("acos", new UnaryMathematicsFunctionParser<>(Functions.ACOS));
        register.registerFunction("atan", new UnaryMathematicsFunctionParser<>(Functions.ATAN));
        register.registerFunction("arcsin", new UnaryMathematicsFunctionParser<>(Functions.ASIN));
        register.registerFunction("arccos", new UnaryMathematicsFunctionParser<>(Functions.ACOS));
        register.registerFunction("arctan", new UnaryMathematicsFunctionParser<>(Functions.ATAN));
        register.registerFunction("sinh", new UnaryMathematicsFunctionParser<>(Functions.SINH));
        register.registerFunction("cosh", new UnaryMathematicsFunctionParser<>(Functions.COSH));
        register.registerFunction("tanh", new UnaryMathematicsFunctionParser<>(Functions.TANH));
        register.registerFunction("sh", new UnaryMathematicsFunctionParser<>(Functions.SINH));
        register.registerFunction("ch", new UnaryMathematicsFunctionParser<>(Functions.COSH));
        register.registerFunction("th", new UnaryMathematicsFunctionParser<>(Functions.TANH));
        register.registerFunction("ln", new UnaryMathematicsFunctionParser<>(Functions.LN));
        register.registerFunction("lg", new UnaryMathematicsFunctionParser<>(Functions.LG));
        register.registerFunction("sqrt", new UnaryMathematicsFunctionParser<>(Functions.SQRT));
        register.registerFunction("cbrt", new UnaryMathematicsFunctionParser<>(Functions.CBRT));
        register.registerFunction("exp", new UnaryMathematicsFunctionParser<>(Functions.EXP));
        register.registerFunction("abs", new UnaryMathematicsFunctionParser<>(Functions.ABS));
        register.registerFunction("sgn", new UnaryMathematicsFunctionParser<>(Functions.SGN));
        register.registerFunction("ceil", new UnaryMathematicsFunctionParser<>(Functions.CEIL));
        register.registerFunction("floor", new UnaryMathematicsFunctionParser<>(Functions.FLOOR));
        register.registerFunction("round", new UnaryMathematicsFunctionParser<>(Functions.ROUND));
    }

    public final void initWithLogicOperator() {
        init();

    }

    public final Statement format(String expr) throws MathParseException {
        return format(expr, DefinedVariables.EMPTY_VARIABLES);
    }

    public final Statement format(String expr, DefinedVariables variables) throws MathParseException {
        return StatementGenerator.createAST(expr, this, variables);
    }
}
