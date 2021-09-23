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
package io.github.nickid2018.smcl;

import io.github.nickid2018.smcl.statements.Variable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DefinedVariables {

    public static final DefinedVariables EMPTY_VARIABLES = new DefinedVariables();

    private static final Map<String, Variable> sharedVariables = new HashMap<>();
    private final Set<String> variables;

    public DefinedVariables() {
        variables = new HashSet<>();
    }

    public DefinedVariables register(String var) {
        variables.add(var);
        if (!sharedVariables.containsKey(var))
            sharedVariables.put(var, new Variable(var));
        return this;
    }

    public DefinedVariables registerAll(Set<String> vars) {
        vars.forEach(this::register);
        return this;
    }

    public DefinedVariables registerAll(DefinedVariables vars) {
        return registerAll(vars.variables);
    }

    public void unregister(String var) {
        variables.remove(var);
    }

    public boolean haveVariables(String var) {
        return variables.contains(var);
    }

    public Variable getVariable(String var) {
        return sharedVariables.get(var);
    }

    public int size() {
        return variables.size();
    }
}
