package com.github.nickid2018.smcl.regression;

import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.optimize.*;
import com.github.nickid2018.smcl.statements.*;

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
		MultiplyStatement mls = smcl.obtain(MultiplyStatement.class);
		PowerStatement pws = smcl.obtain(PowerStatement.class);
		DivideStatement ds = smcl.obtain(DivideStatement.class);
		Statement na = NumberPool.get(smcl, Math.exp(a));
		ds.setValues(NumberPool.get(smcl, 1), smcl.globalvars.getVariable(Regression.independentVariable));
		pws.setValues(NumberPool.get(smcl, Math.exp(b)), ds);
		mls.setValues(na, pws);
		return mls;
	}

}
