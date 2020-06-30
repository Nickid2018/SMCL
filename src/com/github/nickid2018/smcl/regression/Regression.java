package com.github.nickid2018.smcl.regression;

import java.util.*;
import com.github.nickid2018.smcl.*;

public class Regression {

	public static String independentVariable = "x";
	public static String dependentVariable = "y";

	public static Statement doRegression(SMCL smcl, Map<Double,Double> values) {
		return doRegression(smcl, values, LinearModel.MODEL);
	}

	public static Statement doRegression(SMCL smcl, Map<Double,Double> values, RegressionModel model) {
		int n = values.size();
		double xysum = 0;
		double xsum = 0;
		double ysum = 0;
		double x2sum = 0;
		for (Map.Entry<Double,Double> en:values.entrySet()) {
			double x = model.transformIndependent(en.getKey());
			double y = model.transformDependent(en.getValue());
			xysum += x * y;
			xsum += x;
			ysum += y;
			x2sum += x * x;
		}
		double b = (xysum - xsum * ysum / n) / (x2sum - xsum * xsum / n);
		double a = (ysum - xsum * b) / n;
		return model.getTransformed(smcl, b, a);
	}
}
