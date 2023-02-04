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
package io.github.nickid2018.smcl.demo;

import io.github.nickid2018.smcl.SMCLContext;
import io.github.nickid2018.smcl.SnapshotAPI;
import io.github.nickid2018.smcl.Statement;
import io.github.nickid2018.smcl.regression.InvertedIndexModel;
import io.github.nickid2018.smcl.regression.Regression;

import java.util.HashMap;
import java.util.Map;

@SnapshotAPI
public class RegressionDemo {

    public static void main(String[] args) {
        SMCLContext smcl = SMCLContext.getInstance();
        smcl.globalvars.registerVariable("x");
        Map<Double, Double> map = new HashMap<>();
        map.put(1.0, 64.0);
        map.put(2.0, 8.0);
        map.put(3.0, 4.0);
        map.put(6.0, 2.0);
        Statement statement = Regression.doRegression(smcl, map, InvertedIndexModel.MODEL);
        System.out.println("Regression Result: " + statement);
        System.out.println("R2=" + Regression.getCoefficient(map, InvertedIndexModel.MODEL));
        System.out.println(Math.pow(Math.sqrt(3), 2));
    }
}
