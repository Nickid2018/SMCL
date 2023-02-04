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
import io.github.nickid2018.smcl.functions.FunctionStatement;

/**
 * Basic class for parser of functions.
 */
public abstract class FunctionParser {

    /**
     * Returns whether it has two or more arguments.
     * @return true if the arguments count >1
     */
    public abstract boolean numParamsVaries();

    /**
     * Returns the count of arguments of the function.
     * @return the count of arguments
     */
    public abstract int getNumParams();

    /**
     * Parse the arguments into statement.
     * @param smcl a context
     * @param statements the arguments
     * @return a statement
     */
    public abstract Statement parseStatement(SMCLContext smcl, Statement... statements);
}
