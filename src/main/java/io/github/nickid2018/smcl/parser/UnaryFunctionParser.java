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

import io.github.nickid2018.smcl.SMCLContext;
import io.github.nickid2018.smcl.Statement;
import io.github.nickid2018.smcl.functions.UnaryFunctionBuilder;
import io.github.nickid2018.smcl.functions.UnaryFunctionStatement;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Parser for unary function.
 */
public class UnaryFunctionParser extends FunctionParser {

    private final Function<Statement[], ? extends Statement> map;

    /**
     * Construct a parser.
     * @param unary the function builder
     */
    public UnaryFunctionParser(UnaryFunctionBuilder unary) {
        map = unary::create;
    }

    @Override
    public boolean numParamsVaries() {
        return false;
    }

    @Override
    public int getNumParams() {
        return 1;
    }

    @Override
    public Statement parseStatement(SMCLContext smcl, Statement... statements) {
        return map.apply(statements);
    }

}
