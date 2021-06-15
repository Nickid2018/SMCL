package com.github.nickid2018.smcl.functions;

import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.optimize.*;
import com.github.nickid2018.smcl.statements.*;
import com.github.nickid2018.smcl.statements.arith.*;

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
		return (isNegative ? "-" : "") + getFunction().getName() + "(" + innerStatement + ")";
	}

	@Override
	protected final double calculateInternal(VariableList list) {
		double innerResult = innerStatement.calculate(list);
		innerResult = function.getResolveVariable().accept(innerResult, smcl);
		function.getDomainCheck().accept(innerResult);
		return function.getResolveEnd().accept(getFunction().getCalcFunction().accept(innerResult), smcl);
	}

	public UnaryFunctionBuilder getFunction() {
		return function;
	}

	public UnaryFunctionGenStatement setFunction(UnaryFunctionBuilder function) {
		this.function = function;
		return this;
	}

	@Override
	protected Statement derivativeInternal() {
		Statement partDesi = function.getDerivativeResolver().apply(innerStatement);
		Statement end = innerStatement.derivative();
		if (end instanceof NumberStatement) {
			double get = end.calculate(null);
			if (get == 0)
				return NumberPool.NUMBER_CONST_0;
			if (get == 1)
				return partDesi;
			if (get == -1)
				return partDesi.getNewNegative();
		}
		return new MultiplyStatement(smcl, variables).addMultipliers(end, partDesi);
	}

}
