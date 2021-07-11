/*
 * Copyright 2021 Nickid2018
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.nickid2018.smcl.example;

import com.github.nickid2018.smcl.*;

public class DerivativeDemo {

	static long timer;

	public static void main(String[] args) throws Exception {
		SMCL smcl = SMCL.getInstance();
		smcl.init();
		smcl.globalvars.registerVariables("x");
		smcl.settings.degreeAngle = true;
		Statement s = smcl.format("sin(x)*cos(2*x)*cos(x)");
		s = smcl.format("sin(x)*cos(2*x)*cos(x)");
		s = smcl.format("sin(x)*cos(2*x)*cos(x)");
		s = smcl.format("sin(x)*cos(2*x)*cos(x)");
		s = smcl.format("sin(x)*cos(2*x)*cos(x)");
		s = smcl.format("sin(x)*cos(2*x)*cos(x)");
		s = smcl.format("sin(x)*cos(2*x)*cos(x)");
		timer = System.nanoTime();
		s = smcl.format("sin(x)*cos(2*x)*cos(x)");
		timeOutput();
		s.toString();
		s.toString();
		s.toString();
		s.toString();
		s.toString();
		s.toString();
		timer = System.nanoTime();
		System.out.println(s);
		timeOutput();
		Statement d = s.derivative();
		d = s.derivative();
		d = s.derivative();
		d = s.derivative();
		d = s.derivative();
		d = s.derivative();
		d = s.derivative();
		d = s.derivative();
		d = s.derivative();
		timer = System.nanoTime();
		d = s.derivative();
		timeOutput();
		d.toString();
		d.toString();
		d.toString();
		d.toString();
		d.toString();
		timer = System.nanoTime();
		System.out.println(d);
		timeOutput();
	}

	static void timeOutput() {
		long time = System.nanoTime();
		System.out.println((time - timer) / 1000_000D);
		timer = System.nanoTime();
	}
}
