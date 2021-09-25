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
 * Basic class of the operator parser.
 */
public abstract class OperatorParser {

    /**
     * The priority of the operator, this will be used to control the parsing order.
     * @return the priority
     */
    public abstract int getPriority();

    /**
     * Returns whether the operator uses the left associate operation.
     * @return true if the operator is left associate
     */
    public abstract boolean isLeftAssoc();

    /**
     * Parse the argument into a statement.
     * @param smcl a context
     * @param variables a variable list
     * @param statements the arguments of the operator
     * @return a statement
     */
    public abstract Statement parseStatement(SMCLContext smcl, VariableList variables, Statement... statements);
}
