package com.github.nickid2018.smcl.regression;

import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.optimize.*;
import com.github.nickid2018.smcl.statements.*;

public class LinearModel extends RegressionModel {

	public static final LinearModel MODEL = new LinearModel();

	@Override
	public double transformIndependent(double x) {
		return x;
	}

	@Override
	public double transformDependent(double y) {
		return y;
	}

	@Override
	public Statement getTransformed(SMCL smcl, double b, double a) {
		MathStatement ms = smcl.obtain(MathStatement.class);
		Statement na = NumberPool.get(smcl, a);
		Statement nb = NumberPool.get(smcl, b);
		MultiplyStatement mls = smcl.obtain(MultiplyStatement.class);
		mls.setValues(nb, smcl.globalvars.getVariable(Regression.independentVariable));
		ms.setValues(na, mls);
		return ms;
	}

}
