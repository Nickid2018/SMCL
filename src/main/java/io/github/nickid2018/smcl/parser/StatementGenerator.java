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
package io.github.nickid2018.smcl.parser;
import io.github.nickid2018.smcl.*;
import io.github.nickid2018.smcl.optimize.NumberPool;
import io.github.nickid2018.smcl.statements.Variable;
import io.github.nickid2018.smcl.statements.VoidStatement;

import java.util.*;

public class StatementGenerator {

    /**
     * Create a statement AST for the input string.
     * @param input a string contains a statement
     * @param smcl the context
     * @param variables a variable list
     * @return a statement
     * @throws StatementParseException throws if the string isn't a valid statement
     */
    public static Statement createAST(String input, SMCLContext smcl, VariableList variables) throws StatementParseException {
        Stack<Statement> operationStack = new Stack<>();
        List<StatementToken> rpnTokens = doRPN(input, smcl);
        validate(rpnTokens, input, smcl);
        SMCLRegister register = smcl.register;
        VariableList variableList = smcl.globalvars.toDefinedVariables();
        variableList.registerAll(variables);
        for (StatementToken token : rpnTokens) {
            switch (token.type) {
                case UNARY_OPERATOR:
                    operationStack.push(register.getRegisteredOperator(token.detail)
                            .parseStatement(smcl, variableList, operationStack.pop()));
                    continue;
                case OPERATOR:
                    Statement operand_o1 = operationStack.pop();
                    Statement operand_o2 = operationStack.pop();
                    operationStack.push(register.getRegisteredOperator(token.detail)
                            .parseStatement(smcl, variableList, operand_o2,
                            operand_o1));
                    continue;
                case VARIABLE:
                    String varName = token.detail;
                    if (register.containsConstant(varName))
                        operationStack.push(register.getConstant(varName));
                    else {
                        Variable var = variableList.getVariable(varName);
                        if (var == null)
                            error("Variable \"" + varName + "\" isn't declared", input, token);
                        operationStack.push(var);
                    }
                    continue;
                case FUNCTION:
                    String name = token.detail.toLowerCase(Locale.ROOT);
                    FunctionParser function = register.getRegisteredFunction(name);
                    if (function == null)
                        error("Unknown Function " + token.detail, input, token);
                    ArrayList<Statement> subOperands = new ArrayList<>();
                    while (!operationStack.isEmpty() && operationStack.peek() != VoidStatement.PARAMS_START_STATEMENT)
                        subOperands.add(operationStack.pop());
                    Collections.reverse(subOperands);
                    if (operationStack.peek() == VoidStatement.PARAMS_START_STATEMENT)
                        operationStack.pop();
                    operationStack.push(function.parseStatement(smcl, subOperands.toArray(new Statement[0])));
                    continue;
                case OPEN_PAREN:
                    operationStack.push(VoidStatement.PARAMS_START_STATEMENT);
                    continue;
                case NUMBER:
                    try {
                        operationStack.push(NumberPool.getNumber(Double.parseDouble(token.detail)));
                    } catch (NumberFormatException e) {
                        error("Can't parse the string \"" + token.detail + "\" into a number", input, token);
                    }
                    continue;
                case HEX_NUMBER:
                    try {
                        operationStack.push(NumberPool.getNumber(Long.parseLong(token.detail.substring(2), 16)));
                    } catch (NumberFormatException e) {
                        error("Can't parse the string \"" + token.detail + "\" into a hex number", input, token);
                    }
                    continue;
                default:
                    error("What's up? That's impossible!", input, token);
            }
        }
        return operationStack.pop();
    }

    /**
     * Parse a string into an RPN Queue.
     * @param input a string contains a statement
     * @param smcl the context
     * @return a list of the RPN tokens
     * @throws StatementParseException throws if the RPN fails
     */
    public static List<StatementToken> doRPN(String input, SMCLContext smcl) throws StatementParseException {
        List<StatementToken> outputQueue = new ArrayList<>();
        Stack<StatementToken> stack = new Stack<>();
        StatementTokenizer tokenizer = new StatementTokenizer(smcl, input);
        StatementToken lastFunction = null;
        StatementToken previousToken = null;
        OperatorParser operator;
        SMCLRegister register = smcl.register;
        while (tokenizer.hasNextToken()) {
            StatementToken token = tokenizer.nextToken();
            switch (token.type) {
                case NUMBER:
                case HEX_NUMBER:
                    if (previousToken != null && (previousToken.type == StatementTokenType.NUMBER
                            || previousToken.type == StatementTokenType.HEX_NUMBER))
                        error("Missing an operator between two operands", input, token);
                    outputQueue.add(token);
                    break;
                case VARIABLE:
                    outputQueue.add(token);
                    break;
                case FUNCTION:
                    stack.push(token);
                    lastFunction = token;
                    break;
                case COMMA:
                    if (previousToken != null && previousToken.type == StatementTokenType.OPERATOR)
                        error("Missing parameter(s) for operator ", input, token);
                    while (!stack.isEmpty() && stack.peek().type != StatementTokenType.OPEN_PAREN)
                        outputQueue.add(stack.pop());
                    if (!stack.isEmpty())
                        break;
                    if (lastFunction == null)
                        error("Unexpected comma", input, token);
                    error("Parse error for function " + lastFunction, input, token);
                    break;
                case OPERATOR:
                    if (previousToken != null && (previousToken.type == StatementTokenType.COMMA
                            || previousToken.type == StatementTokenType.OPEN_PAREN))
                        error("Missing parameter(s) for operator '" + token + "'", input, token);
                    operator = register.getRegisteredOperator(token.detail);
                    if (operator == null)
                        error("Unknown operator " + token.detail.substring(0, token.detail.length() - 1), input, token);
                    dealOperator(outputQueue, stack, operator, register);
                    stack.push(token);
                    break;
                case UNARY_OPERATOR:
                    if (previousToken != null && previousToken.type != StatementTokenType.OPERATOR
                            && previousToken.type != StatementTokenType.COMMA
                            && previousToken.type != StatementTokenType.OPEN_PAREN)
                        error("Invalid position for unary operator " + token, input, token);
                    operator = register.getRegisteredOperator(token.detail);
                    if (operator == null)
                        error("Unknown unary operator " + token.detail.substring(0, token.detail.length() - 1), input,
                                token);
                    dealOperator(outputQueue, stack, operator, register);
                    stack.push(token);
                    break;
                case OPEN_PAREN:
                    if (previousToken != null) {
                        if (previousToken.type == StatementTokenType.NUMBER
                                || previousToken.type == StatementTokenType.CLOSE_PAREN
                                || previousToken.type == StatementTokenType.VARIABLE
                                || previousToken.type == StatementTokenType.HEX_NUMBER) {
                            // Add multiply operator automatically
                            StatementToken multiplication = new StatementToken();
                            multiplication.append("*");
                            multiplication.type = StatementTokenType.OPERATOR;
                            stack.push(multiplication);
                        }
                        if (previousToken.type == StatementTokenType.FUNCTION) {
                            outputQueue.add(token);
                        }
                    }
                    stack.push(token);
                    break;
                case CLOSE_PAREN:
                    if (previousToken != null && previousToken.type == StatementTokenType.OPERATOR)
                        error("Missing parameter(s) for operator " + previousToken, input, token);
                    while (!stack.isEmpty() && stack.peek().type != StatementTokenType.OPEN_PAREN)
                        outputQueue.add(stack.pop());
                    if (stack.isEmpty())
                        error("Mismatched parentheses", input, token);
                    stack.pop();
                    if (!stack.isEmpty() && stack.peek().type == StatementTokenType.FUNCTION)
                        outputQueue.add(stack.pop());
            }
            previousToken = token;
        }
        while (!stack.isEmpty()) {
            StatementToken element = stack.pop();
            if (element.type == StatementTokenType.OPEN_PAREN || element.type == StatementTokenType.CLOSE_PAREN)
                error("Mismatched parentheses", input, null);
            outputQueue.add(element);
        }
        return outputQueue;
    }

    /**
     * Validate the math statement.
     * @param rpnQueue RPN Queue of the statement
     * @param input the string of the statement
     * @param smcl the context
     * @throws StatementParseException throws if the validation fails
     */
    public static void validate(List<StatementToken> rpnQueue, String input, SMCLContext smcl) throws StatementParseException {
        Stack<Integer> stack = new Stack<>();
        stack.push(0);
        for (StatementToken token : rpnQueue) {
            switch (token.type) {
                case UNARY_OPERATOR:
                    if (stack.peek() >= 1)
                        continue;
                    error("Missing parameter(s) for operator " + token, input, token);
                case OPERATOR: {
                    if (stack.peek() < 2)
                        error("Missing parameter(s) for operator " + token, input, token);
                    stack.set(stack.size() - 1, stack.peek() - 2 + 1);
                    continue;
                }
                case FUNCTION: {
                    FunctionParser f = smcl.register.getRegisteredFunction(token.detail.toLowerCase(Locale.ROOT));
                    int numParams = stack.pop();
                    if (f == null)
                        error("Function " + token + " isn't defined.", input, token);
                    if (numParams != f.getNumParams() && f.getNumParams() != -1 /* -1 means unknown arguments */) {
                        error("Function " + token + (f.numParamsVaries() ? " (Multi-argument)" : "")
                                        + " expected " + f.getNumParams() + " parameters, but got " + numParams,
                                input, token);
                    }
                    if (stack.size() <= 0) {
                        error("Too many function calls, maximum scope exceeded", input, token);
                    }
                    stack.set(stack.size() - 1, stack.peek() + 1);
                    continue;
                }
                case OPEN_PAREN: {
                    stack.push(0);
                    continue;
                }
                default:
            }
            stack.set(stack.size() - 1, stack.peek() + 1);
        }
        if (stack.size() > 1)
            error("Too many unhandled function parameter lists", input, null);
        if (stack.peek() > 1)
            error("Too many numbers or variables", input, null);
        if (stack.peek() < 1)
            error("Empty expression", input, null);
    }

    /*
     * Throws math parse exception.
     */
    private static void error(String cause, String expr, StatementToken token) throws StatementParseException {
        throw new StatementParseException(cause, expr, token);
    }

    /*
     * Deal with the operator
     */
    private static void dealOperator(List<StatementToken> outputQueue, Stack<StatementToken> stack,
                                     OperatorParser operator, SMCLRegister register) {
        StatementToken nextToken = stack.isEmpty() ? null : stack.peek();
        while (nextToken != null
                && (nextToken.type == StatementTokenType.OPERATOR
                || nextToken.type == StatementTokenType.UNARY_OPERATOR)
                && (operator.isLeftAssoc()
                && operator.getPriority() <= register.getRegisteredOperator(nextToken.detail).getPriority()
                || operator.getPriority() < register.getRegisteredOperator(nextToken.detail).getPriority())) {
            outputQueue.add(stack.pop());
            nextToken = stack.isEmpty() ? null : stack.peek();
        }
    }
}
