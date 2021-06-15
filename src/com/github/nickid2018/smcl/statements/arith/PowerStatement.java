package com.github.nickid2018.smcl.statements.arith;

import java.util.*;
import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.functions.*;
import com.github.nickid2018.smcl.optimize.NumberPool;
import com.github.nickid2018.smcl.statements.*;

public class PowerStatement extends Statement {

	public PowerStatement(SMCL smcl, DefinedVariables variables) {
		super(smcl, variables);
	}

	private Statement base;
	private List<Statement> exponents = new ArrayList<>();

	@Override
	public double calculateInternal(VariableList list) {
		double ret = base.calculate(list);
		for (Statement ms : exponents) {
			double mul = ms.calculate(list);
			ret = Math.pow(ret, mul);
		}
		return ret;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (!(base instanceof NumberStatement) && !(base instanceof Variable))
			sb.append("(" + base + ")");
		else
			sb.append(base);
		for (Statement en : exponents) {
			if ((!(en instanceof NumberStatement) && !(en instanceof Variable)
					&& !(en instanceof UnaryFunctionStatement)) || en.isNegative())
				sb.append("^(" + en + ")");
			else
				sb.append("^" + en);
		}
		return sb + "";
	}

	public PowerStatement addExponent(Statement statement) {
		exponents.add(statement);
		return this;
	}

	public PowerStatement putBaseAndExponents(Statement... statements) {
		base = statements[0];
		for (int i = 1; i < statements.length; i++) {
			addExponent(statements[i]);
		}
		return this;
	}

	@Override
	// (f^g)' = (f^g)¡¤(glnf)'
	// Optimize:
	// 1) f = C: (C^g)' = (C^g)¡¤g'¡¤lnC
	// 2) g = C: (f^C)' = Cf'f^(C-1)
	protected Statement derivativeInternal() {
		if (exponents.size() > 1) {
			Statement now = base;
			for (Statement statement : exponents)
				now = new PowerStatement(smcl, variables).putBaseAndExponents(now, statement);
			return now.derivative();
		}
		Statement exponent = exponents.get(0);
		boolean baseN = base instanceof NumberStatement;
		boolean exponentN = exponent instanceof NumberStatement;
		if (baseN && exponentN)
			return NumberPool.NUMBER_CONST_0;
		if (baseN) {
			double constNumber = Math.log(base.calculate(null));
			Statement deri = exponent.derivative();
			if (deri instanceof NumberStatement)
				constNumber *= deri.calculate(null);
			if (constNumber == 0)
				return NumberPool.NUMBER_CONST_0;
			if (constNumber == 1 || constNumber == -1) {
				MultiplyStatement end = new MultiplyStatement(smcl, variables).addMultiplier(this);
				return constNumber == 1 ? (deri instanceof NumberStatement) ? end : end.addMultiplier(deri)
						: (deri instanceof NumberStatement) ? end : end.addMultiplier(deri).getNegative();
			} else {
				MultiplyStatement end = new MultiplyStatement(smcl, variables)
						.addMultipliers(NumberPool.getNumber(constNumber), this);
				return (deri instanceof NumberStatement) ? end : end.addMultiplier(deri);
			}
		}
		if (exponentN) {
			double exp = exponent.calculate(null);
			double constNumber = exp;
			if (exp == 0)
				return NumberPool.NUMBER_CONST_0;
			if (exp == 1)
				return base.derivative();
			Statement deri = base.derivative();
			if (deri instanceof NumberStatement)
				constNumber *= deri.calculate(null);
			if (constNumber == 0)
				return NumberPool.NUMBER_CONST_0;
			Statement trexp = exp == 2 ? base
					: new PowerStatement(smcl, variables).putBaseAndExponents(base, NumberPool.getNumber(exp - 1));
			if (constNumber == 1 || constNumber == -1) {
				MultiplyStatement end = new MultiplyStatement(smcl, variables).addMultiplier(trexp);
				return constNumber == 1 ? (deri instanceof NumberStatement) ? end : end.addMultiplier(deri)
						: (deri instanceof NumberStatement) ? end : end.addMultiplier(deri).getNegative();
			} else {
				MultiplyStatement end = new MultiplyStatement(smcl, variables)
						.addMultipliers(NumberPool.getNumber(constNumber), trexp);
				return (deri instanceof NumberStatement) ? end : end.addMultiplier(deri);
			}
		}
		Statement multi = new MultiplyStatement(smcl, variables)
				.addMultipliers(exponent, Functions.LN.create(smcl, base)).derivative();
		if (multi instanceof NumberStatement) {
			double constNumber = multi.calculate(null);
			if (constNumber == 0)
				return NumberPool.NUMBER_CONST_0;
			if (constNumber == 1)
				return this;
			if (constNumber == -1)
				return getNewNegative();
			return new MultiplyStatement(smcl, variables).addMultipliers(multi, this);
		} else
			return new MultiplyStatement(smcl, variables).addMultipliers(this, multi);
	}
}
