package com.github.nickid2018.smcl.functions;

import java.util.function.*;
import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.util.*;

public class UnaryFunctionBuilder extends FunctionBuilder<UnaryFunctionGenStatement> {

	private DoubleConsumer domainCheck = ALL_DOMAIN;
	private Double2DoubleFunction calcFunction = DEFAULT_RESULT;
	private DoubleSMCLFunction resolveVariable = DEFAULT_RESOLVE;
	private DoubleSMCLFunction resolveEnd = DEFAULT_RESOLVE;
	private Function<Statement, Statement> derivativeResolver;

	public UnaryFunctionBuilder(String name) {
		super(name);
	}

	public static UnaryFunctionBuilder createBuilder(String name) {
		return new UnaryFunctionBuilder(name);
	}

	@Override
	public UnaryFunctionGenStatement create(SMCL smcl, Statement... statements) {
		UnaryFunctionGenStatement statement = new UnaryFunctionGenStatement(statements[0]).setFunction(this);
		statement.setSMCL(smcl);
		return statement;
	}

	public UnaryFunctionBuilder withResolve(DoubleSMCLFunction resolve) {
		resolveVariable = resolve;
		return this;
	}

	public UnaryFunctionBuilder andResolve(DoubleSMCLFunction resolve) {
		resolveVariable = resolveVariable.addThen(resolve);
		return this;
	}

	public UnaryFunctionBuilder withResolveEnd(DoubleSMCLFunction resolve) {
		resolveEnd = resolve;
		return this;
	}

	public UnaryFunctionBuilder andResolveEnd(DoubleSMCLFunction resolve) {
		resolveEnd = resolveEnd.addThen(resolve);
		return this;
	}

	public UnaryFunctionBuilder withDomain(DoubleConsumer domainCheck) {
		this.domainCheck = domainCheck;
		return this;
	}

	public UnaryFunctionBuilder andDomain(DoubleConsumer domainCheck) {
		this.domainCheck = this.domainCheck.andThen(domainCheck);
		return this;
	}

	public UnaryFunctionBuilder withFunction(Double2DoubleFunction function) {
		this.calcFunction = function;
		return this;
	}

	public UnaryFunctionBuilder andFunction(Double2DoubleFunction function) {
		calcFunction = calcFunction.addThen(function);
		return this;
	}

	public UnaryFunctionBuilder withDerivativeResolver(Function<Statement, Statement> derivativeResolver) {
		this.derivativeResolver = derivativeResolver;
		return this;
	}

	public UnaryFunctionBuilder copyWithoutFunction(String name) {
		return createBuilder(name).withDomain(domainCheck).withResolve(resolveVariable).withResolveEnd(resolveEnd);
	}

	public DoubleConsumer getDomainCheck() {
		return domainCheck;
	}

	public Double2DoubleFunction getCalcFunction() {
		return calcFunction;
	}

	public DoubleSMCLFunction getResolveVariable() {
		return resolveVariable;
	}

	public DoubleSMCLFunction getResolveEnd() {
		return resolveEnd;
	}

	public Function<Statement, Statement> getDerivativeResolver() {
		return derivativeResolver;
	}
}
