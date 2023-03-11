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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A variable list is to store the names of the variables will appear in the statement.
 */
public class VariableList {

    /**
     * An empty variable list.
     */
    public static final VariableList EMPTY_VARIABLES = new VariableList(SMCLContext.getInstance());

    private final SMCLContext smcl;
    private final Map<String, Variable> variables;

    /**
     * Construct a variable list.
     */
    public VariableList(SMCLContext smcl) {
        variables = new HashMap<>();
        this.smcl = smcl;
    }

    /**
     * Register a variable to the list.
     * @param var the name of the variable
     * @return this
     */
    public VariableList register(String var) {
        Variable v = new Variable(smcl, var);
        v.setSMCL(smcl);
        variables.put(var, v);
        return this;
    }

    /**
     * Register several variables to the list.
     * @param vars a set contains the names of the variables
     * @return this
     */
    public VariableList registerAll(Set<String> vars) {
        vars.forEach(this::register);
        return this;
    }

    /**
     * Register several variables in the list.
     * @param vars a variable list
     * @return this
     */
    public VariableList registerAll(VariableList vars) {
        return registerAll(vars.variables.keySet());
    }

    /**
     * Unregister a variable.
     * @param var the name of the variable
     */
    public void unregister(String var) {
        variables.remove(var);
    }

    /**
     * Returns whether the variable has been registered.
     * @param var the name of the variable
     * @return true if the variable has been registered
     */
    public boolean haveVariables(String var) {
        return variables.containsKey(var);
    }

    /**
     * Get the variable object for the name.
     * @param var the name of the variable
     * @return a variable object
     */
    public Variable getVariable(String var) {
        return variables.get(var);
    }

    /**
     * Get the count of the list.
     * @return a count number
     */
    public int size() {
        return variables.size();
    }
}
