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
import io.github.nickid2018.smcl.Statement;
import io.github.nickid2018.smcl.VariableValueList;

/**
 * A demo for statement.
 */
public class MathStatementDemo {

    static long timer;

    public static void main(String[] args) throws Exception {
        SMCLContext smcl = SMCLContext.getInstance();
        smcl.init();
        smcl.globalvars.registerVariables("x");
//        smcl.settings.degreeAngle = true;
//        smcl.settings.invalidArgumentWarn =true;
        timer = System.nanoTime();
        Statement s = smcl.parse("x^ln(x)^x");
        timeOutput();
        System.out.println(s);
        timeOutput();
        VariableValueList list = new VariableValueList();
        list.addVariableValue("x", 0.53);
        System.out.println(s.calculate(list));
        timeOutput();
        Statement s2;
        System.out.println(s2 = s.derivative());
        timeOutput();
        System.out.println(s2.calculate(list));
        timeOutput();
    }

    static void timeOutput() {
        long time = System.nanoTime();
        System.out.println((time - timer) / 1000_000D);
        timer = System.nanoTime();
    }
}