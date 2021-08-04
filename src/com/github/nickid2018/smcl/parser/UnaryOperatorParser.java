/*
 * Copyright 2021 Nickid2018
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.nickid2018.smcl.parser;

import com.github.nickid2018.smcl.*;

public class UnaryOperatorParser<T extends Statement> extends OperatorParser<T> {

    private final int priority;
    private final boolean leftAssoc;
    private final TriFunction<SMCL, Statement, DefinedVariables, T> map;

    public UnaryOperatorParser(int priority, boolean leftAssoc, TriFunction<SMCL, Statement, DefinedVariables, T> map) {
        this.priority = priority;
        this.leftAssoc = leftAssoc;
        this.map = map;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public boolean isLeftAssoc() {
        return leftAssoc;
    }

    @Override
    public Statement parseStatement(SMCL smcl, DefinedVariables variables, Statement... pop) {
        return map.accept(smcl, pop[0], variables);
    }

}
