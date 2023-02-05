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
import io.github.nickid2018.smcl.statements.NumberStatement;
import io.github.nickid2018.smcl.util.UnaryFunction;
import io.github.nickid2018.smcl.util.UnaryFunctionWithContext;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Function builder for functions has one argument.
 */
public class UnaryFunctionBuilder extends FunctionBuilder {

    private Consumer<NumberObject> domainCheck = ALL_REALS;
    private UnaryFunction calcFunction = DEFAULT_RESULT;
    private UnaryFunctionWithContext resolveVariable = DEFAULT_RESOLVE;
    private UnaryFunctionWithContext resolveEnd = DEFAULT_RESOLVE;
    private Function<Statement, Statement> derivativeResolver;

    public UnaryFunctionBuilder(String name) {
        super(name);
    }

    /**
     * Create a builder with a name.
     * @param name the name of the function
     * @return a builder
     */
    public static UnaryFunctionBuilder createBuilder(String name) {
        return new UnaryFunctionBuilder(name);
    }

    @Override
    public Statement create(Statement... statements) {
        Statement source = statements[0];
        if (source instanceof NumberStatement) {
            NumberObject value = source.calculate(null);
            value = resolveVariable.accept(value, source.getSMCL());
            domainCheck.accept(value);
            return new NumberStatement(source.getSMCL(), resolveEnd.accept(calcFunction.accept(value), source.getSMCL()));
        } else {
            return new UnaryFunctionGenStatement(source, this);
        }
    }

    /**
     * Add a resolver for variables before computing functions.
     * @param resolve a resolver
     * @return this
     */
    public UnaryFunctionBuilder withResolve(UnaryFunctionWithContext resolve) {
        resolveVariable = resolve;
        return this;
    }

    /**
     * Add a resolver for variables behind previous resolvers before computing functions.
     * @param resolve a resolver
     * @return this
     */
    public UnaryFunctionBuilder andResolve(UnaryFunctionWithContext resolve) {
        resolveVariable = resolveVariable.addThen(resolve);
        return this;
    }

    /**
     * Add a resolver for variables behind computing functions.
     * @param resolve a resolver
     * @return this
     */
    public UnaryFunctionBuilder withResolveEnd(UnaryFunctionWithContext resolve) {
        resolveEnd = resolve;
        return this;
    }

    /**
     * Add a resolver for variables behind previous resolvers behind computing functions.
     * @param resolve a resolver
     * @return this
     */
    public UnaryFunctionBuilder andResolveEnd(UnaryFunctionWithContext resolve) {
        resolveEnd = resolveEnd.addThen(resolve);
        return this;
    }

    /**
     * Add a checker for variables resolved.
     * @param domainCheck a checker for domain
     * @return this
     */
    public UnaryFunctionBuilder withDomain(Consumer<NumberObject> domainCheck) {
        this.domainCheck = domainCheck;
        return this;
    }

    /**
     * Add a checker for variables resolved behind previous checkers.
     * @param domainCheck a checker for domain
     * @return this
     */
    public UnaryFunctionBuilder andDomain(Consumer<NumberObject> domainCheck) {
        this.domainCheck = this.domainCheck.andThen(domainCheck);
        return this;
    }

    /**
     * Set a calculating resolver for the function.
     * @param function a resolver to calculate
     * @return this
     */
    public UnaryFunctionBuilder withFunction(UnaryFunction function) {
        this.calcFunction = function;
        return this;
    }

    /**
     * Add a calculating resolver for the function behind previous resolvers.
     * @param function a resolver to calculate
     * @return this
     */
    public UnaryFunctionBuilder andFunction(UnaryFunction function) {
        calcFunction = calcFunction.addThen(function);
        return this;
    }

    /**
     * Set a derivative resolver for the function.
     * @param derivativeResolver a resolver for derivative
     * @return this
     */
    public UnaryFunctionBuilder withDerivativeResolver(Function<Statement, Statement> derivativeResolver) {
        this.derivativeResolver = derivativeResolver;
        return this;
    }

    /**
     * Copy the instance without function.
     * @param name the name of the function
     * @return a new builder
     */
    public UnaryFunctionBuilder copyWithoutFunction(String name) {
        return createBuilder(name).withDomain(domainCheck).withResolve(resolveVariable).withResolveEnd(resolveEnd);
    }

    /**
     * Get the checker for domain.
     * @return a checker
     */
    public Consumer<NumberObject> getDomainCheck() {
        return domainCheck;
    }

    /**
     * Get the resolver for the function.
     * @return a resolver
     */
    public UnaryFunction getCalcFunction() {
        return calcFunction;
    }

    /**
     * Get the resolver before the calculation.
     * @return a resolver
     */
    public UnaryFunctionWithContext getResolveVariable() {
        return resolveVariable;
    }

    /**
     * Get the resolver behind the calculation.
     * @return a resolver
     */
    public UnaryFunctionWithContext getResolveEnd() {
        return resolveEnd;
    }

    /**
     * Get the resolver to derivative.
     * @return a resolver
     */
    public Function<Statement, Statement> getDerivativeResolver() {
        return derivativeResolver;
    }
}
