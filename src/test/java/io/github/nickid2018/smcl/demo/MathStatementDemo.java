/*
 * Copyright 2021-2023 Nickid2018
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
import io.github.nickid2018.smcl.Statement;
import io.github.nickid2018.smcl.VariableValueList;
import io.github.nickid2018.smcl.number.MatrixObject;
import io.github.nickid2018.smcl.number.NumberObject;
import io.github.nickid2018.smcl.number.StdNumberObject;

/**
 * A demo for statement.
 */
public class MathStatementDemo {

    public static void main(String[] args) throws Exception {
        SMCLContext smcl = SMCLContext.getInstance();
        smcl.init();
        smcl.globalvars.registerVariables("x");
        smcl.globalvars.registerVariables("y");
        Statement s = smcl.parse("det(y^det(-2+x^2*y)*x)-543/4^y");
        System.out.println(s);
        VariableValueList list = new VariableValueList(smcl);
        list.addVariableValue("x", new MatrixObject(new NumberObject[][]{
                {StdNumberObject.PROVIDER.fromStdNumber(0.5), StdNumberObject.PROVIDER.fromStdNumber(Math.sqrt(3) / 2)},
                {StdNumberObject.PROVIDER.fromStdNumber(Math.sqrt(3) / 2), StdNumberObject.PROVIDER.fromStdNumber(0.5)}
        }));
        list.addVariableValue("y", StdNumberObject.PROVIDER.fromStdNumber(2));
        System.out.println(s.calculate(list));
    }
}
