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

import io.github.nickid2018.smcl.statements.Variable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The class is to store variables for global usage.
 */
public class GlobalVariables {

    private final SMCLContext smcl;
    private final Map<String, Variable> variablemap = new HashMap<>();

    public GlobalVariables(SMCLContext smcl) {
        this.smcl = smcl;
    }

    /**
     * Register a variable.
     * @param var the name of the variable
     * @return this
     */
    public GlobalVariables registerVariable(String var) {
        variablemap.put(var, new Variable(var));
        return this;
    }

    /**
     * Register variables.
     * @param vars the list of the names of the variables
     * @return this
     */
    public GlobalVariables registerVariables(String... vars) {
        for (String var : vars)
            variablemap.put(var, new Variable(var));
        return this;
    }

    /**
     * Unregister a variable.
     * @param var the name of the variable
     * @return this
     */
    public GlobalVariables unregisterVariable(String var) {
        variablemap.remove(var);
        return this;
    }

    /**
     * Returns whether the variable has been registered.
     * @param var the name of the variable
     * @return true if the variable has been registered
     */
    public boolean haveVariable(String var) {
        return variablemap.containsKey(var);
    }

    /**
     * Get the variable object for the name.
     * @param var the name of the variable
     * @return a variable object
     */
    public Variable getVariable(String var) {
        return variablemap.get(var);
    }

    /**
     * Get a set contains all variables registered in the object.
     * @return a set
     */
    public Set<String> getAllRegistered() {
        return variablemap.keySet();
    }

    /**
     * Convert this into a variable list.
     * @return a variable list
     */
    public VariableList toDefinedVariables() {
        return new VariableList(smcl).registerAll(getAllRegistered());
    }
}
