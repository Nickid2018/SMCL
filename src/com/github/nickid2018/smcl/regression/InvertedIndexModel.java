package com.github.nickid2018.smcl.regression;

import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.optimize.*;
import com.github.nickid2018.smcl.statements.arith.*;

public class InvertedIndexModel extends RegressionModel {

	public static final InvertedIndexModel MODEL = new InvertedIndexModel();

	@Override
	public double transformIndependent(double x) {
		return 1 / x;
	}

	@Override
	public double transformDependent(double y) {
		return Math.log(y);
	}

	@Override
	public Statement getTransformed(SMCL smcl, double b, double a) {
		MultiplyStatement mls = new MultiplyStatement(smcl, smcl.globalvars.toDefinedVariables());
		PowerStatement pws = new PowerStatement(smcl, smcl.globalvars.toDefinedVariables());
		DivideStatement ds = new DivideStatement(smcl, smcl.globalvars.toDefinedVariables());
		Statement na = NumberPool.get(smcl, Math.exp(a));
		ds.putDividendAndDivisors(NumberPool.get(smcl, 1), smcl.globalvars.getVariable(Regression.independentVariable));
		pws.putBaseAndExponents(NumberPool.get(smcl, Math.exp(b)), ds);
		mls.addMultipliers(na, pws);
		return mls;
	}

}
