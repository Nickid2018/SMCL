package com.github.nickid2018.smcl.regression;

import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.optimize.*;
import com.github.nickid2018.smcl.statements.arith.*;

public class PowerModel extends RegressionModel {

	public static final PowerModel MODEL = new PowerModel();

	@Override
	public double transformIndependent(double x) {
		return Math.log(x);
	}

	@Override
	public double transformDependent(double y) {
		return Math.log(y);
	}

	@Override
	public Statement getTransformed(SMCL smcl, double b, double a) {
		MultiplyStatement mls = new MultiplyStatement(smcl, smcl.globalvars.toDefinedVariables());
		PowerStatement pws = new PowerStatement(smcl, smcl.globalvars.toDefinedVariables());
		Statement nb = NumberPool.get(smcl, b);
		Statement na = NumberPool.get(smcl, Math.exp(a));
		pws.putBaseAndExponents(smcl.globalvars.getVariable(Regression.independentVariable), nb);
		mls.addMultipliers(na, pws);
		return mls;
	}

}
