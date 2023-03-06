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

import io.github.nickid2018.smcl.Statement;
import io.github.nickid2018.smcl.number.NumberObject;
import io.github.nickid2018.smcl.number.SingleValue;
import io.github.nickid2018.smcl.set.NumberSet;
import io.github.nickid2018.smcl.util.UnaryFunction;
import io.github.nickid2018.smcl.util.UnaryFunctionWithContext;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A builder for functions to build.
 */
public abstract class FunctionBuilder {

    /**
     * Domain "R"
     */
    public static final Consumer<NumberObject> ALL_REALS = checkNumberTypeInclude(SingleValue.class)
            .andThen(arg -> {
                if (arg.isReal() && !Double.isFinite(arg.toStdNumber()))
                    throw new ArithmeticException("Infinite numbers and NaNs are not supported");
            });

    /**
     * Default function
     */
    public static final UnaryFunction DEFAULT_RESULT = arg -> arg;
    /**
     * Default result resolver
     */
    public static final UnaryFunctionWithContext DEFAULT_RESOLVE = (arg, smcl) -> arg;
    /**
     * Resolution for radian angles
     */
    public static final UnaryFunctionWithContext RESOLVE_RADIANS = (arg, smcl) ->
            smcl.settings.degreeAngle && arg.isReal() ? smcl.numberProvider.fromStdNumber(Math.toRadians(arg.toStdNumber())) : arg;
    /**
     * Resolution for degree angles
     */
    public static final UnaryFunctionWithContext RESOLVE_DEGREES = (arg, smcl) ->
            smcl.settings.degreeAngle && arg.isReal() ? smcl.numberProvider.fromStdNumber(Math.toDegrees(arg.toStdNumber())) : arg;

    protected final String name;

    /**
     * Construct a builder with a name.
     *
     * @param name the name of the function
     */
    public FunctionBuilder(String name) {
        this.name = name;
    }

    /**
     * Returns a checker to check if the value is excluded the domain.
     *
     * @param exclude     a predicate to check the value
     * @param errorString a string supplier for error string
     * @return a checker
     */
    public static Consumer<NumberObject> checkDomainExclude(Predicate<NumberObject> exclude, Function<NumberObject, String> errorString) {
        return arg -> {
            if (arg.isReal() && !Double.isFinite(arg.toStdNumber()))
                throw new ArithmeticException("Infinite numbers and NaNs are not supported");
            if (exclude.test(arg))
                throw new ArithmeticException(errorString.apply(arg));
        };
    }

    /**
     * Returns a checker to check if the value is included the domain.
     *
     * @param include     a predicate to check the value
     * @param errorString a string supplier for error string
     * @return a checker
     */
    public static Consumer<NumberObject> checkDomainInclude(Predicate<NumberObject> include, Function<NumberObject, String> errorString) {
        return arg -> {
            if (arg.isReal() && !Double.isFinite(arg.toStdNumber()))
                throw new ArithmeticException("Infinite numbers and NaNs are not supported");
            if (!include.test(arg))
                throw new ArithmeticException(errorString.apply(arg));
        };
    }

    /**
     * Returns a checker to check if the value is excluded the set of the domain.
     *
     * @param set         a set to check the value
     * @param errorString a string supplier for error string
     * @return a checker
     */
    public static Consumer<NumberObject> checkDomainExclude(NumberSet set, Function<NumberObject, String> errorString) {
        return checkDomainExclude(set::isBelongTo, errorString);
    }

    /**
     * Returns a checker to check if the value is included the set of the domain.
     *
     * @param set         a set to check the value
     * @param errorString a string supplier for error string
     * @return a checker
     */
    public static Consumer<NumberObject> checkDomainInclude(NumberSet set, Function<NumberObject, String> errorString) {
        return checkDomainInclude(set::isBelongTo, errorString);
    }

    /**
     * Returns a checker to check if the value is excluded the set of the domain.
     *
     * @param clazz classes to check the value
     * @return a checker
     */
    public static Consumer<NumberObject> checkNumberTypeExclude(Class<?>... clazz) {
        return number -> {
            for (Class<?> c : clazz)
                if (c.isInstance(number))
                    throw new ArithmeticException("The number type " + number.getClass().getSimpleName() + " is not supported");
        };
    }

    /**
     * Returns a checker to check if the value is excluded the set of the domain.
     *
     * @param clazz classes to check the value
     * @return a checker
     */
    public static Consumer<NumberObject> checkNumberTypeInclude(Class<?>... clazz) {
        return number -> {
            boolean flag = false;
            for (Class<?> c : clazz)
                if (c.isInstance(number)) {
                    flag = true;
                    break;
                }
            if (!flag)
                throw new ArithmeticException("The number type " + number.getClass().getSimpleName() + " is not supported");
        };
    }

    /**
     * Get the name of the function.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Create a new function statement.
     *
     * @param statements an array contains arguments
     * @return a statement
     */
    public abstract Statement create(boolean optimize, Statement... statements);
}
