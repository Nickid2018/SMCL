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
package io.github.nickid2018.smcl.statements;

import io.github.nickid2018.smcl.VariableList;
import io.github.nickid2018.smcl.Statement;
import io.github.nickid2018.smcl.VariableValueList;

/**
 * A void statement, which will not calculate or derivative.
 */
public class VoidStatement extends Statement {

    /**
     * Use for signing the function arguments.
     */
    public static final VoidStatement PARAMS_START_STATEMENT = new VoidStatement();

    /**
     * Close construction.
     */
    private VoidStatement() {
        super(null, VariableList.EMPTY_VARIABLES);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "void";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double calculateInternal(VariableValueList list) {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Statement derivativeInternal() {
        return null;
    }
}
