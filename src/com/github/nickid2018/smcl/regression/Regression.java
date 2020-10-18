package com.github.nickid2018.smcl.regression;

import java.util.*;
import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.optimize.*;

public class Regression {

	public static String independentVariable = "x";
	public static String dependentVariable = "y";

	protected static final ThreadLocal<NumberStorager> storagers = new ThreadLocal<>();

	public static Statement doRegression(SMCL smcl, Map<Double, Double> values) {
		return doRegression(smcl, values, LinearModel.MODEL);
	}

	public static Statement doRegression(SMCL smcl, Map<Double, Double> values, RegressionModel model) {
		if (storagers.get() == null)
			storagers.set(new NumberStorager());
		NumberStorager storager = storagers.get();
		computeLSM(values, model, storager);
		double b = storager.getDouble();
		double a = storager.getDouble();
		storager.clear();
		return model.getTransformed(smcl, b, a);
	}

	public static void computeLSM(Map<Double, Double> values, RegressionModel model, NumberStorager storage) {
		int n = values.size();
		double xysum = 0;
		double xsum = 0;
		double ysum = 0;
		double x2sum = 0;
		for (Map.Entry<Double, Double> en : values.entrySet()) {
			double x = model.transformIndependent(en.getKey());
			double y = model.transformDependent(en.getValue());
			xysum += x * y;
			xsum += x;
			ysum += y;
			x2sum += x * x;
		}
		double b;
		storage.putDouble(b = (xysum - xsum * ysum / n) / (x2sum - xsum * xsum / n));
		storage.putDouble((ysum - xsum * b) / n);
		storage.putDouble(ysum / n);
	}

	public static double getAverage(Collection<Double> total) {
		double sum = 0;
		for (double v : total) {
			sum += v;
		}
		return sum / total.size();
	}

	public static double getCoefficient(Map<Double, Double> values, RegressionModel model) {
		if (storagers.get() == null)
			storagers.set(new NumberStorager());
		NumberStorager storager = storagers.get();
		computeLSM(values, model, storager);
		double b = storager.getDouble();
		double a = storager.getDouble();
		double yavg = storager.getDouble();
		double tss = 0;
		double ess = 0;
		for (Map.Entry<Double, Double> xy : values.entrySet()) {
			tss += Math.pow(model.transformDependent(xy.getValue()) - yavg, 2);
			ess += Math.pow(b * model.transformIndependent(xy.getKey()) + a - yavg, 2);
		}
		storager.clear();
		return ess / tss;
	}
}
