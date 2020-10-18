package com.github.nickid2018.smcl.regression;

import com.github.nickid2018.smcl.*;

public abstract class RegressionModel {

	public abstract double transformIndependent(double x);

	public abstract double transformDependent(double y);

	public abstract Statement getTransformed(SMCL smcl, double b, double a);
}
