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
package io.github.nickid2018.smcl;

import io.github.nickid2018.smcl.number.NumberObject;

import java.util.HashMap;
import java.util.Map;

/**
 * The class to store the values of the variables
 */
public class VariableValueList {

    private final SMCLContext context;

    private final Map<String, NumberObject> value = new HashMap<>();

    public VariableValueList(SMCLContext context) {
        this.context = context;
    }

    /**
     * Fill the variable with the value.
     * @param var the name of the variable
     * @param v the value
     * @return this
     */
    public final VariableValueList addVariableValue(String var, double v) {
        value.put(var, context.numberProvider.fromStdNumber(v));
        return this;
    }

    /**
     * Fill the variable with the value.
     * @param var the name of the variable
     * @param v the value
     * @return this
     */
    public final VariableValueList addVariableValue(String var, NumberObject v) {
        value.put(var, v);
        return this;
    }

    /**
     * Get the value of the variable.
     * @param var the name of the variable
     * @return the value of the variable
     */
    public final NumberObject getVariableValue(String var) {
        if (!value.containsKey(var))
            throw new MetaArithmeticException("smcl.compute.no_variable", var);
        return value.get(var);
    }

    /**
     * Return if the variable is in the list
     * @param var the name of the variable
     * @return true if the variable is int the list
     */
    public final boolean containsValue(String var) {
        return value.containsKey(var);
    }
}
