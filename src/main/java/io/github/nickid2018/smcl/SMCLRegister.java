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

import io.github.nickid2018.smcl.parser.FunctionParser;
import io.github.nickid2018.smcl.parser.OperatorParser;
import io.github.nickid2018.smcl.parser.StatementTokenizer;
import io.github.nickid2018.smcl.parser.UnaryOperatorParser;

import java.util.HashMap;
import java.util.Map;

/**
 * The class to register function/statement parsers
 * @see #registerFunction(String, FunctionParser)
 * @see #registerOperator(String, OperatorParser)
 * @see #registerUnaryOperator(String, UnaryOperatorParser)
 */
public final class SMCLRegister {

    // --- Parsers
    private final Map<String, OperatorParser> operators = new HashMap<>();
    private final Map<String, FunctionParser> functions = new HashMap<>();

    // --- Context
    private final SMCLContext smcl;

    // --- Close constructor
    SMCLRegister(SMCLContext smcl) {
        this.smcl = smcl;
    }

    /**
     * Get related SMCL instance.
     * @return related SMCL instance
     */
    public SMCLContext getSMCL() {
        return smcl;
    }

    /**
     * Register a parser to parse an operator into a statement.
     * @param operator the operator in string
     * @param parser the operator parser
     */
    public void registerOperator(String operator, OperatorParser parser) {
        operators.put(operator, parser);
    }

    /**
     * Register a parser to parse a unary operator into a statement.
     * @param operator the unary operator in string
     * @param parser the operator parser
     */
    public void registerUnaryOperator(String operator, UnaryOperatorParser parser) {
        operators.put(operator + StatementTokenizer.unaryOperatorSuffix, parser);
    }

    /**
     * Remove the parser for the operator. For unary operators, use <code>operator+u</code> (e.g. +u) to remove the parser.
     * @param operator the parser for the operator to remove
     */
    public void removeOperator(String operator) {
        operators.remove(operator);
    }

    /**
     * Get an operator parser for the operator.For unary operators, use <code>operator+u</code> (e.g. +u) to get the parser.
     * @param operator the parser for the operator to get
     * @return the parser for the operator
     */
    public OperatorParser getRegisteredOperator(String operator) {
        return operators.get(operator);
    }

    /**
     * Return true if the operator has been registered.
     * @param operator an operator
     * @return true if the operator has been registered, otherwise false
     */
    public boolean containsOperator(String operator) {
        return operators.containsKey(operator)
                || operators.containsKey(operator + StatementTokenizer.unaryOperatorSuffix);
    }

    /**
     * Register a parser to parse a function into a statement.
     * @param function the name of the function
     * @param parser the parser of the function
     */
    public void registerFunction(String function, FunctionParser parser) {
        functions.put(function, parser);
    }

    /**
     * Remove the parser for the function.
     * @param function the parser for the function to remove
     */
    public void removeFunction(String function) {
        functions.remove(function);
    }

    /**
     * Get an operator parser for the function.
     * @param function the name of the function
     * @return the parser for the function
     */
    public FunctionParser getRegisteredFunction(String function) {
        return functions.get(function);
    }

    /**
     * Return true if the function has been registered.
     * @param function a function
     * @return true if the function has been registered, otherwise false
     */
    public boolean containsFunction(String function) {
        return functions.containsKey(function);
    }
}
