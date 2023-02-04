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
package io.github.nickid2018.smcl;

import io.github.nickid2018.smcl.functions.Functions;
import io.github.nickid2018.smcl.optimize.NumberPool;
import io.github.nickid2018.smcl.parser.*;
import io.github.nickid2018.smcl.statements.arith.DivideStatement;
import io.github.nickid2018.smcl.statements.arith.MathStatement;
import io.github.nickid2018.smcl.statements.arith.MultiplyStatement;
import io.github.nickid2018.smcl.statements.arith.PowerStatement;

/**
 * The context of the SMCL System, which controls the parsing, derivative and computing operations
 */
public class SMCLContext {

    // --- Default instance
    private static SMCLContext instance;

    /**
     * The settings of the context
     */
    public final SMCLSettings settings;

    /**
     * The parser register of the context
     */
    public final SMCLRegister register;

    /**
     * The global variables of the context
     */
    public final GlobalVariables globalvars;

    /**
     * Construct a context with a settings.
     * @param setting a SMCL settings
     */
    public SMCLContext(SMCLSettings setting) {
        settings = setting;
        register = new SMCLRegister(this);
        globalvars = new GlobalVariables(this);
    }

    /**
     * Get the default SMCL instance.
     * @return default instance
     */
    public static synchronized SMCLContext getInstance() {
        return instance == null ? instance = new SMCLContext(new SMCLSettings()) : instance;
    }

    /**
     * Initialize the SMCL System, register the basic constant, operators and functions.
     */
    public final void init() {
        register.registerConstant("pi", NumberPool.getNumber(Math.PI));
        register.registerConstant("e", NumberPool.getNumber(Math.E));
        register.registerOperator("+", new BinaryOperatorParser(20, true,
                (smcl, statements, variables) -> new MathStatement(smcl, variables).addStatements(statements)));
        register.registerUnaryOperator("+",
                new UnaryOperatorParser(60, false, (smcl, statement, variables) -> statement));
        register.registerOperator("-", new BinaryOperatorParser(20, true, (smcl, statements, variables) ->
                new MathStatement(smcl, variables).addStatements(statements[0], statements[1].getNegative())));
        register.registerUnaryOperator("-",
                new UnaryOperatorParser(60, false, (smcl, statement, variables) -> statement.getNegative()));
        register.registerOperator("*", new BinaryOperatorParser(30, true,
                (smcl, statements, variables) -> new MultiplyStatement(smcl, variables).addMultipliers(statements)));
        register.registerOperator("/", new BinaryOperatorParser(30, true, (smcl, statements, variables) -> {
            if (statements[0] instanceof DivideStatement)
                return ((DivideStatement) statements[0]).addDivisor(statements[1]);
            else
                return new DivideStatement(smcl, variables).putDividendAndDivisors(statements);
        }));
        register.registerOperator("^", new BinaryOperatorParser(40, true, (smcl, statements, variables) -> {
            if (statements[0] instanceof PowerStatement) {
                PowerStatement get = (PowerStatement) statements[0];
                Statement other = statements[1];
                return get.addExponent(other);
            } else {
                PowerStatement statement = new PowerStatement(smcl, variables);
                return statement.putBaseAndExponents(statements);
            }
        }));
        register.registerFunction("sin", new UnaryFunctionParser(Functions.SIN));
        register.registerFunction("cos", new UnaryFunctionParser(Functions.COS));
        register.registerFunction("tan", new UnaryFunctionParser(Functions.TAN));
        register.registerFunction("csc", new UnaryFunctionParser(Functions.CSC));
        register.registerFunction("sec", new UnaryFunctionParser(Functions.SEC));
        register.registerFunction("cot", new UnaryFunctionParser(Functions.COT));
        register.registerFunction("asin", new UnaryFunctionParser(Functions.ASIN));
        register.registerFunction("acos", new UnaryFunctionParser(Functions.ACOS));
        register.registerFunction("atan", new UnaryFunctionParser(Functions.ATAN));
        register.registerFunction("arcsin", new UnaryFunctionParser(Functions.ASIN));
        register.registerFunction("arccos", new UnaryFunctionParser(Functions.ACOS));
        register.registerFunction("arctan", new UnaryFunctionParser(Functions.ATAN));
        register.registerFunction("sinh", new UnaryFunctionParser(Functions.SINH));
        register.registerFunction("cosh", new UnaryFunctionParser(Functions.COSH));
        register.registerFunction("tanh", new UnaryFunctionParser(Functions.TANH));
        register.registerFunction("sh", new UnaryFunctionParser(Functions.SINH));
        register.registerFunction("ch", new UnaryFunctionParser(Functions.COSH));
        register.registerFunction("th", new UnaryFunctionParser(Functions.TANH));
        register.registerFunction("ln", new UnaryFunctionParser(Functions.LN));
        register.registerFunction("lg", new UnaryFunctionParser(Functions.LG));
        register.registerFunction("sqrt", new UnaryFunctionParser(Functions.SQRT));
        register.registerFunction("cbrt", new UnaryFunctionParser(Functions.CBRT));
        register.registerFunction("exp", new UnaryFunctionParser(Functions.EXP));
        register.registerFunction("abs", new UnaryFunctionParser(Functions.ABS));
        register.registerFunction("sgn", new UnaryFunctionParser(Functions.SGN));
        register.registerFunction("ceil", new UnaryFunctionParser(Functions.CEIL));
        register.registerFunction("floor", new UnaryFunctionParser(Functions.FLOOR));
        register.registerFunction("round", new UnaryFunctionParser(Functions.ROUND));
        register.registerFunction("fact", new UnaryFunctionParser(Functions.FACTORIAL));
        register.registerFunction("mod", new BinaryFunctionParser(Functions.MOD));
        register.registerFunction("log", new BinaryFunctionParser(Functions.LOG));
    }

    /**
     * TODO Initialize the SMCL System, register the basic operators including logic operators and functions.
     */
    public final void initWithLogicOperator() {
        init();

    }

    /**
     * Parse a string into a statement.
     * @param expr a string contains a statement
     * @return a statement can use it to compute
     * @throws StatementParseException throws if the statement is invalid
     */
    public final Statement parse(String expr) throws StatementParseException {
        return parse(expr, VariableList.EMPTY_VARIABLES);
    }

    /**
     * Parse a string into a statement with a variable list.
     * @param expr a string contains a statement
     * @param variables a variable list
     * @return a statement can use it to compute
     * @throws StatementParseException throws if the statement is invalid
     */
    public final Statement parse(String expr, VariableList variables) throws StatementParseException {
        return StatementGenerator.createAST(expr, this, variables);
    }
}
