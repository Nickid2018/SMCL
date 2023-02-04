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

public abstract class BiFunctionStatement extends FunctionStatement {

    protected Statement innerStatement1;
    protected Statement innerStatement2;

    /**
     * Construct with arguments.
     * @param statement1 the first statement
     * @param statement2 the second statement
     */
    public BiFunctionStatement(Statement statement1, Statement statement2) {
        super(statement1.getSMCL(), statement1.getVariables());
        setInnerStatements(statement1, statement2);
    }

    /**
     * Get the first inner statement.
     * @return a statement
     */
    public Statement getInnerStatement1() {
        return innerStatement1;
    }

    /**
     * Get the second inner statement.
     * @return a statement
     */
    public Statement getInnerStatement2() {
        return innerStatement2;
    }

    /**
     * Set the inner statement.
     * @param innerStatement1 the first statement
     * @param innerStatement2 the second statement
     * @return this
     */
    public BiFunctionStatement setInnerStatements(Statement innerStatement1, Statement innerStatement2) {
        this.innerStatement1 = innerStatement1;
        this.innerStatement2 = innerStatement2;
        return this;
    }
}