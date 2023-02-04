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

public class BaseFunctions {

    /**
     * Compute factorial in double. When the number is greater than 171, the result will overflow.
     * @param arg a natural number
     * @return the result
     */
    public static double factorial(double arg) {
        double answer = 1;
        for(int i = 2; i < arg; i++) {
            answer *= i;
            if(Double.isInfinite(answer))
                throw new ArithmeticException("Number overflow in factorial computing - " + arg);
        }
        return answer;
    }

    /**
     * Compute logarithm with a certain base.
     * @param N a number
     * @param a base
     * @return the result
     */
    public static double log(double N, double a) {
        if(a == 1)
            throw new ArithmeticException("base is 1");
        return Math.log(N) / Math.log(a);
    }
}
