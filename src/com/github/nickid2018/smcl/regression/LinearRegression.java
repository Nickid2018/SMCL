package com.github.nickid2018.smcl.regression;

import java.util.*;
import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.optimize.*;
import com.github.nickid2018.smcl.statements.arith.*;

public class LinearRegression extends Regression {

	public static Statement doRegression(SMCL smcl, Map<Double, Double> values) {
		if (storagers.get() == null)
			storagers.set(new NumberStorager());
		NumberStorager storager = storagers.get();
		computeOLS(values, storager);
		double b = storager.getDouble();
		double a = storager.getDouble();
		storager.clear();
		MathStatement ms = new MathStatement(smcl, smcl.globalvars.toDefinedVariables());
		MultiplyStatement mls = new MultiplyStatement(smcl, smcl.globalvars.toDefinedVariables());
		mls.addMultipliers(NumberPool.get(smcl, b), smcl.globalvars.getVariable(independentVariable));
		ms.addStatements(NumberPool.get(smcl, a), mls);
		return ms;
	}

	public static void computeOLS(Map<Double, Double> values, NumberStorager storage) {
		int n = values.size();
		double xysum = 0;
		double xsum = 0;
		double ysum = 0;
		double x2sum = 0;
		for (Map.Entry<Double, Double> en : values.entrySet()) {
			double x = en.getKey();
			double y = en.getValue();
			xysum += x * y;
			xsum += x;
			ysum += y;
			x2sum += x * x;
		}
		double b;
		storage.putDouble(b = (xysum - xsum * ysum / n) / (x2sum - xsum * xsum / n));
		storage.putDouble((ysum - xsum * b) / n);
	}
}
