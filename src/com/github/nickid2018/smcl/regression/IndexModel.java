package com.github.nickid2018.smcl.regression;

import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.optimize.*;
import com.github.nickid2018.smcl.statements.arith.*;

public class IndexModel extends RegressionModel {

	public static final IndexModel MODEL = new IndexModel();

	@Override
	public double transformIndependent(double x) {
		return x;
	}

	@Override
	public double transformDependent(double y) {
		return Math.log(y);
	}

	@Override
	public Statement getTransformed(SMCL smcl, double b, double a) {
		MultiplyStatement mls = smcl.obtain(MultiplyStatement.class);
		PowerStatement pws = smcl.obtain(PowerStatement.class);
		Statement na = NumberPool.get(smcl, Math.exp(a));
		pws.putBaseAndExponents(NumberPool.get(smcl, Math.exp(b)),
				smcl.globalvars.getVariable(Regression.independentVariable));
		mls.addMultipliers(na, pws);
		return mls;
	}

}
