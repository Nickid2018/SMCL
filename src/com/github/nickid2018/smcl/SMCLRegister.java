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
package com.github.nickid2018.smcl;

import com.github.nickid2018.smcl.parser.FunctionParser;
import com.github.nickid2018.smcl.parser.OperatorParser;
import com.github.nickid2018.smcl.parser.StatementTokenizer;
import com.github.nickid2018.smcl.parser.UnaryOperatorParser;

import java.util.HashMap;
import java.util.Map;

public final class SMCLRegister {

    private final Map<String, OperatorParser<?>> operators = new HashMap<>();
    private final Map<String, FunctionParser<?>> functions = new HashMap<>();

    private final SMCLContext smcl;

    SMCLRegister(SMCLContext smcl) {
        this.smcl = smcl;
    }

    public SMCLContext getSMCL() {
        return smcl;
    }

    public void registerOperator(String operator, OperatorParser<?> parser) {
        operators.put(operator, parser);
    }

    public void registerUnaryOperator(String operator, UnaryOperatorParser<?> parser) {
        operators.put(operator + StatementTokenizer.unaryOperatorSuffix, parser);
    }

    public void removeOperator(String operator) {
        operators.remove(operator);
    }

    public OperatorParser<?> getRegisteredOperator(String operator) {
        return operators.get(operator);
    }

    public boolean containsOperator(String operator) {
        return operators.containsKey(operator)
                || operators.containsKey(operator + StatementTokenizer.unaryOperatorSuffix);
    }

    public void registerFunction(String function, FunctionParser<?> parser) {
        functions.put(function, parser);
    }

    public void removeFunction(String function) {
        functions.remove(function);
    }

    public FunctionParser<?> getRegisteredFunction(String function) {
        return functions.get(function);
    }

    public boolean containsFunction(String function) {
        return functions.containsKey(function);
    }
}
