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
package io.github.nickid2018.smcl.regression;

import io.github.nickid2018.smcl.SMCLContext;
import io.github.nickid2018.smcl.SnapshotAPI;
import io.github.nickid2018.smcl.Statement;
import io.github.nickid2018.smcl.optimize.NumberPool;
import io.github.nickid2018.smcl.statements.arith.MultiplyStatement;
import io.github.nickid2018.smcl.statements.arith.PowerStatement;

@SnapshotAPI
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
    public Statement getTransformed(SMCLContext smcl, double b, double a) {
        MultiplyStatement mls = new MultiplyStatement(smcl, smcl.globalvars.toDefinedVariables());
        PowerStatement pws = new PowerStatement(smcl, smcl.globalvars.toDefinedVariables());
        Statement nb = NumberPool.getNumber(b);
        Statement na = NumberPool.getNumber(Math.exp(a));
        pws.putBaseAndExponents(smcl.globalvars.getVariable(Regression.independentVariable), nb);
        mls.addMultipliers(na, pws);
        return mls;
    }

}
