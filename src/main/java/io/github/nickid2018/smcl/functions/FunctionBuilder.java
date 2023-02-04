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
package io.github.nickid2018.smcl.functions;

import io.github.nickid2018.smcl.SMCLContext;
import io.github.nickid2018.smcl.Statement;
import io.github.nickid2018.smcl.set.NumberSet;
import io.github.nickid2018.smcl.util.Double2DoubleFunction;
import io.github.nickid2018.smcl.util.DoubleSMCLFunction;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;

/**
 * A builder for functions to build.
 */
public abstract class FunctionBuilder {

    /**
     * Domain "R"
     */
    public static final DoubleConsumer ALL_DOMAIN = arg -> {
        if (!Double.isFinite(arg))
            throw new ArithmeticException("Infinite numbers and NaNs are not supported");
    };

    /**
     * Default function
     */
    public static final Double2DoubleFunction DEFAULT_RESULT = arg -> arg;
    /**
     * Default result resolver
     */
    public static final DoubleSMCLFunction DEFAULT_RESOLVE = (arg, smcl) -> arg;
    /**
     * Resolution for radian angles
     */
    public static final DoubleSMCLFunction RESOLVE_RADIANS = (arg, smcl) -> smcl.settings.degreeAngle ? Math.toRadians(arg) : arg;
    /**
     * Resolution for degree angles
     */
    public static final DoubleSMCLFunction RESOLVE_DEGREES = (arg, smcl) -> smcl.settings.degreeAngle ? Math.toDegrees(arg) : arg;

    protected final String name;

    /**
     * Construct a builder with a name.
     * @param name the name of the function
     */
    public FunctionBuilder(String name) {
        this.name = name;
    }

    /**
     * Returns a checker to check if the value is excluded the domain.
     * @param exclude a predicate to check the value
     * @param errorString a string supplier for error string
     * @return a checker
     */
    public static DoubleConsumer checkDomainExclude(DoublePredicate exclude, DoubleFunction<String> errorString) {
        return arg -> {
            if (!Double.isFinite(arg))
                throw new ArithmeticException("Infinite numbers and NaNs are not supported");
            if (exclude.test(arg))
                throw new ArithmeticException(errorString.apply(arg));
        };
    }

    /**
     * Returns a checker to check if the value is included the domain.
     * @param include a predicate to check the value
     * @param errorString a string supplier for error string
     * @return a checker
     */
    public static DoubleConsumer checkDomainInclude(DoublePredicate include, DoubleFunction<String> errorString) {
        return arg -> {
            if (!Double.isFinite(arg))
                throw new ArithmeticException("Infinite numbers and NaNs are not supported");
            if (!include.test(arg))
                throw new ArithmeticException(errorString.apply(arg));
        };
    }

    /**
     * Returns a checker to check if the value is excluded the set of the domain.
     * @param set a set to check the value
     * @param errorString a string supplier for error string
     * @return a checker
     */
    public static DoubleConsumer checkDomainExclude(NumberSet set, DoubleFunction<String> errorString) {
        return checkDomainExclude(set::isBelongTo, errorString);
    }

    /**
     * Returns a checker to check if the value is included the set of the domain.
     * @param set a set to check the value
     * @param errorString a string supplier for error string
     * @return a checker
     */
    public static DoubleConsumer checkDomainInclude(NumberSet set, DoubleFunction<String> errorString) {
        return checkDomainInclude(set::isBelongTo, errorString);
    }

    /**
     * Get the name of the function.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Create a new function statement.
     * @param statements an array contains arguments
     * @return a statement
     */
    public abstract Statement create(Statement... statements);
}
