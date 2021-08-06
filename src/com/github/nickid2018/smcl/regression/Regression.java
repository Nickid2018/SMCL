/*
 * Copyright 2021 Nickid2018
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.nickid2018.smcl.regression;

import com.github.nickid2018.smcl.SMCLContext;
import com.github.nickid2018.smcl.Statement;
import com.github.nickid2018.smcl.optimize.NumberStorage;

import java.util.Collection;
import java.util.Map;

public class Regression {

    public static final String independentVariable = "x";
    protected static final ThreadLocal<NumberStorage> storages = new ThreadLocal<>();
    public static String dependentVariable = "y";

    public static Statement doRegression(SMCLContext smcl, Map<Double, Double> values) {
        return doRegression(smcl, values, LinearModel.MODEL);
    }

    public static Statement doRegression(SMCLContext smcl, Map<Double, Double> values, RegressionModel model) {
        smcl.globalvars.registerVariable(independentVariable);
        if (storages.get() == null)
            storages.set(new NumberStorage());
        NumberStorage storage = storages.get();
        computeLSM(values, model, storage);
        double b = storage.getDouble();
        double a = storage.getDouble();
        storage.clear();
        return model.getTransformed(smcl, b, a);
    }

    public static void computeLSM(Map<Double, Double> values, RegressionModel model, NumberStorage storage) {
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
        if (storages.get() == null)
            storages.set(new NumberStorage());
        NumberStorage storage = storages.get();
        computeLSM(values, model, storage);
        double b = storage.getDouble();
        double a = storage.getDouble();
        double yavg = storage.getDouble();
        double tss = 0;
        double ess = 0;
        for (Map.Entry<Double, Double> xy : values.entrySet()) {
            tss += Math.pow(model.transformDependent(xy.getValue()) - yavg, 2);
            ess += Math.pow(b * model.transformIndependent(xy.getKey()) + a - yavg, 2);
        }
        storage.clear();
        return ess / tss;
    }
}
