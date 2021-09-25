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
package io.github.nickid2018.smcl.functions;

import io.github.nickid2018.smcl.Statement;

/**
 * Statement for unary functions.
 */
public abstract class UnaryFunctionStatement extends FunctionStatement {

    protected Statement innerStatement;

    /**
     * Construct with an argument.
     * @param statement a statement
     */
    public UnaryFunctionStatement(Statement statement) {
        super(statement.getSMCL(), statement.getVariables());
        setInnerStatement(statement);
    }

    /**
     * Get the inner statement.
     * @return a statement
     */
    public Statement getInnerStatement() {
        return innerStatement;
    }

    /**
     * Set the inner statement.
     * @param innerStatement a statement
     * @return this
     */
    public UnaryFunctionStatement setInnerStatement(Statement innerStatement) {
        this.innerStatement = innerStatement;
        return this;
    }

}
