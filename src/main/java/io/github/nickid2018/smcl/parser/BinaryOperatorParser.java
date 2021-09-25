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
package io.github.nickid2018.smcl.parser;

import io.github.nickid2018.smcl.VariableList;
import io.github.nickid2018.smcl.SMCLContext;
import io.github.nickid2018.smcl.Statement;

/**
 * Parser for binary operator.
 */
public class BinaryOperatorParser extends OperatorParser {

    private final int priority;
    private final boolean leftAssoc;
    private final TriFunction<SMCLContext, Statement[], VariableList, Statement> map;

    /**
     * Construct a binary operator parser.
     * @param priority the priority of the parser
     * @param leftAssoc left associated
     * @param map function to map the statement
     */
    public BinaryOperatorParser(int priority, boolean leftAssoc,
                                TriFunction<SMCLContext, Statement[], VariableList, Statement> map) {
        this.priority = priority;
        this.leftAssoc = leftAssoc;
        this.map = map;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPriority() {
        return priority;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLeftAssoc() {
        return leftAssoc;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Statement parseStatement(SMCLContext smcl, VariableList variables, Statement... pop) {
        return map.accept(smcl, pop, variables);
    }

}
