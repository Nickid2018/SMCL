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
import io.github.nickid2018.smcl.functions.BinaryFunctionBuilder;
import io.github.nickid2018.smcl.functions.UnaryFunctionBuilder;
import io.github.nickid2018.smcl.functions.UnaryFunctionStatement;

import java.util.function.BiFunction;

/**
 * Parser for unary function.
 */
public class BinaryFunctionParser extends FunctionParser {

    private final BiFunction<SMCLContext, Statement[], ? extends Statement> map;

    /**
     * Construct a parser.
     * @param unary the function builder
     */
    public BinaryFunctionParser(BinaryFunctionBuilder unary) {
        map = unary::create;
    }

    /**
     * Construct a parser.
     * @param map function to map the statement
     */
    public BinaryFunctionParser(BiFunction<SMCLContext, Statement[], UnaryFunctionStatement> map) {
        this.map = map;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean numParamsVaries() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumParams() {
        return 2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Statement parseStatement(SMCLContext smcl, Statement... statements) {
        return map.apply(smcl, statements);
    }

}
