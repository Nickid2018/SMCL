package com.github.nickid2018.smcl.functions;

import java.util.function.*;
import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.util.*;

public class UnaryFunctionGenStatement extends UnaryFunctionStatement {

	private String name;
	private DoubleConsumer check;
	private DoubleSMCLFunction resolveVariable;
	private Double2DoubleFunction result;
	private DoubleSMCLFunction resolveEnd;

	public UnaryFunctionGenStatement(SMCL smcl, Statement statement, String name, Double2DoubleFunction result,
			DoubleConsumer check, DoubleSMCLFunction resolveVariable, DoubleSMCLFunction resolveEnd) {
		super(statement);
		this.smcl = smcl;
		this.name = name;
		this.result = result;
		this.check = check;
		this.resolveVariable = resolveVariable;
		this.resolveEnd = resolveEnd;
	}

	@Override
	public String toString() {
		return name + "(" + innerStatement + ")";
	}

	@Override
	protected double calculateInternal(VariableList list) {
		double innerResult = innerStatement.calculate(list);
		innerResult = resolveVariable.accept(innerResult, smcl);
		check.accept(innerResult);
		return resolveEnd.accept(result.accept(innerResult), smcl);
	}

}
