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
import io.github.nickid2018.smcl.number.NumberObject;
import io.github.nickid2018.smcl.statements.NumberStatement;
import io.github.nickid2018.smcl.util.BinaryFunction;
import io.github.nickid2018.smcl.util.UnaryFunctionWithContext;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * The builder for creating binary function
 */
public class BinaryFunctionBuilder extends FunctionBuilder {

    private BiConsumer<SMCLContext, NumberObject> domainCheck1 = ALL_REALS;
    private BiConsumer<SMCLContext, NumberObject> domainCheck2 = ALL_REALS;
    private BinaryFunction calcFunction = (a, b) -> a;
    private UnaryFunctionWithContext resolveVariable1 = DEFAULT_RESOLVE;
    private UnaryFunctionWithContext resolveVariable2 = DEFAULT_RESOLVE;
    private UnaryFunctionWithContext resolveEnd = DEFAULT_RESOLVE;
    private BiFunction<Statement, Statement, Statement> derivativeResolver;

    /**
     * Construct a builder with a name.
     *
     * @param name the name of the function
     */
    public BinaryFunctionBuilder(String name) {
        super(name);
    }

    public BiConsumer<SMCLContext, NumberObject> getDomainCheck1() {
        return domainCheck1;
    }

    public BinaryFunctionBuilder withDomainCheck1(BiConsumer<SMCLContext, NumberObject> domainCheck1) {
        this.domainCheck1 = domainCheck1;
        return this;
    }

    public BiConsumer<SMCLContext, NumberObject> getDomainCheck2() {
        return domainCheck2;
    }

    public BinaryFunctionBuilder withDomainCheck2(BiConsumer<SMCLContext, NumberObject> domainCheck2) {
        this.domainCheck2 = domainCheck2;
        return this;
    }

    public BinaryFunction getCalcFunction() {
        return calcFunction;
    }

    public BinaryFunctionBuilder withCalcFunction(BinaryFunction calcFunction) {
        this.calcFunction = calcFunction;
        return this;
    }

    public UnaryFunctionWithContext getResolveVariable1() {
        return resolveVariable1;
    }

    public BinaryFunctionBuilder withResolveVariable1(UnaryFunctionWithContext resolveVariable1) {
        this.resolveVariable1 = resolveVariable1;
        return this;
    }

    public UnaryFunctionWithContext getResolveVariable2() {
        return resolveVariable2;
    }

    public BinaryFunctionBuilder withResolveVariable2(UnaryFunctionWithContext resolveVariable2) {
        this.resolveVariable2 = resolveVariable2;
        return this;
    }

    public UnaryFunctionWithContext getResolveEnd() {
        return resolveEnd;
    }

    public BinaryFunctionBuilder withResolveEnd(UnaryFunctionWithContext resolveEnd) {
        this.resolveEnd = resolveEnd;
        return this;
    }

    public BiFunction<Statement, Statement, Statement> getDerivativeResolver() {
        return derivativeResolver;
    }

    public BinaryFunctionBuilder withDerivativeResolver(BiFunction<Statement, Statement, Statement> derivativeResolver) {
        this.derivativeResolver = derivativeResolver;
        return this;
    }

    /**
     * Create a builder with a name.
     * @param name the name of the function
     * @return a builder
     */
    public static BinaryFunctionBuilder createBuilder(String name) {
        return new BinaryFunctionBuilder(name);
    }

    @Override
    public Statement create(boolean optimize, Statement... statements) {
        Statement source1 = statements[0];
        Statement source2 = statements[1];
        if(source1 instanceof NumberStatement && source2 instanceof NumberStatement && optimize) {
            NumberObject value1 = source1.calculate(null);
            value1 = resolveVariable1.accept(value1, source1.getSMCL());
            domainCheck1.accept(source1.getSMCL(), value1);
            NumberObject value2 = source2.calculate(null);
            value2 = resolveVariable2.accept(value2, source2.getSMCL());
            domainCheck2.accept(source1.getSMCL(), value2);
            return new NumberStatement(source1.getSMCL(), resolveEnd.accept(calcFunction.accept(value1, value2), source1.getSMCL()));
        } else
            return new BiFunctionGenStatement(source1, source2, this);
    }
}
