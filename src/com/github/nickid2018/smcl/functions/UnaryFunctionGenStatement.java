package com.github.nickid2018.smcl.functions;

import com.github.nickid2018.smcl.*;

public class UnaryFunctionGenStatement extends UnaryFunctionStatement {

	private UnaryFunctionBuilder function;

	public UnaryFunctionGenStatement(Statement ms) {
		super(ms);
	}

	public UnaryFunctionGenStatement(SMCL smcl, Statement statement, UnaryFunctionBuilder function) {
		super(statement);
		this.smcl = smcl;
		this.function = function;
	}

	@Override
	public final String toString() {
		return getFunction().getName() + "(" + getInnerStatement() + ")";
	}

	@Override
	protected final double calculateInternal(VariableList list) {
		double innerResult = getInnerStatement().calculate(list);
		innerResult = getFunction().getResolveVariable().accept(innerResult, smcl);
		getFunction().getDomainCheck().accept(innerResult);
		return getFunction().getResolveEnd().accept(getFunction().getCalcFunction().accept(innerResult), smcl);
	}

	public UnaryFunctionBuilder getFunction() {
		return function;
	}

	public UnaryFunctionGenStatement setFunction(UnaryFunctionBuilder function) {
		this.function = function;
		return this;
	}

}
