package com.github.nickid2018.smcl.functions;

import java.util.function.*;
import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.util.*;

public class UnaryFunctionBuilder extends FunctionBuilder<UnaryFunctionGenStatement> {

	private DoubleConsumer domainCheck = ALL_DOMAIN;
	private Double2DoubleFunction calcFunction = DEFAULT_RESULT;
	private DoubleSMCLFunction resolveVariable = DEFAULT_RESOLVE;
	private DoubleSMCLFunction resolveEnd = DEFAULT_RESOLVE;

	public UnaryFunctionBuilder(String name) {
		super(name);
	}

	public static UnaryFunctionBuilder createBuilder(String name) {
		return new UnaryFunctionBuilder(name);
	}

	@Override
	public UnaryFunctionGenStatement create(SMCL smcl, Statement... statements) {
		return new UnaryFunctionGenStatement(smcl, statements[0], name, calcFunction, domainCheck, resolveVariable,
				resolveEnd);
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
}
