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
package io.github.nickid2018.smcl.number;

import io.github.nickid2018.smcl.statements.NumberStatement;

import java.util.HashMap;
import java.util.Map;

/**
 * A pool to store the numbers.
 */
public class NumberPool {

    public static final NumberStatement NUMBER_CONST_1 = new NumberStatement(1);
    public static final NumberStatement NUMBER_CONST_0 = new NumberStatement(0);
    public static final NumberStatement NUMBER_CONST_M1 = new NumberStatement(-1);

    private static final Map<Double, NumberStatement> numbers = new HashMap<>();

    static {
        numbers.put(1.0, NUMBER_CONST_1);
        numbers.put(0.0, NUMBER_CONST_0);
        numbers.put(-0.0, NUMBER_CONST_0);
        numbers.put(-1.0, NUMBER_CONST_M1);
    }

    /**
     * Get or create a number statement in the pool.
     * @param value a number
     * @return a number statement
     */
    public static NumberStatement getNumber(double value) {
        if (numbers.containsKey(value))
            return numbers.get(value);
        NumberStatement ns = new NumberStatement(value);
        numbers.put(value, ns);
        return ns;
    }

    /**
     * Returns if the number is special (-1, 0, 1)
     * @param value a number
     * @return true if the number is special
     */
    public static boolean isSpecial(double value) {
        return value == -1.0 || value == 0.0 || value == 1.0;
    }
}
