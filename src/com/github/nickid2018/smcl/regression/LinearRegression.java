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
import com.github.nickid2018.smcl.optimize.NumberPool;
import com.github.nickid2018.smcl.optimize.NumberStorage;
import com.github.nickid2018.smcl.statements.arith.MathStatement;
import com.github.nickid2018.smcl.statements.arith.MultiplyStatement;

import java.util.Map;

public class LinearRegression extends Regression {

    public static Statement doRegression(SMCLContext smcl, Map<Double, Double> values) {
        if (storages.get() == null)
            storages.set(new NumberStorage());
        NumberStorage storage = storages.get();
        computeOLS(values, storage);
        double b = storage.getDouble();
        double a = storage.getDouble();
        storage.clear();
        MathStatement ms = new MathStatement(smcl, smcl.globalvars.toDefinedVariables());
        MultiplyStatement mls = new MultiplyStatement(smcl, smcl.globalvars.toDefinedVariables());
        mls.addMultipliers(NumberPool.get(smcl, b), smcl.globalvars.getVariable(independentVariable));
        ms.addStatements(NumberPool.get(smcl, a), mls);
        return ms;
    }

    public static void computeOLS(Map<Double, Double> values, NumberStorage storage) {
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
