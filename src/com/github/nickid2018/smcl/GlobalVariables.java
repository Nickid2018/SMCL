/*
 *
 *  * Copyright 2021 Nickid2018
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */
package com.github.nickid2018.smcl;

import java.util.*;

import com.github.nickid2018.smcl.statements.*;

public class GlobalVariables {

    private final Map<String, Variable> variablemap = new HashMap<>();

    public GlobalVariables registerVariable(String var) {
        variablemap.put(var, new Variable(var));
        return this;
    }

    public GlobalVariables registerVariables(String... vars) {
        for (String var : vars)
            variablemap.put(var, new Variable(var));
        return this;
    }

    public GlobalVariables unregisterVariable(String var) {
        variablemap.remove(var);
        return this;
    }

    public boolean haveVariable(String var) {
        return variablemap.containsKey(var);
    }

    public Variable getVariable(String var) {
        return variablemap.get(var);
    }

    public Set<String> getAllRegistered() {
        return variablemap.keySet();
    }

    public DefinedVariables toDefinedVariables() {
        return new DefinedVariables().registerAll(getAllRegistered());
    }
}
