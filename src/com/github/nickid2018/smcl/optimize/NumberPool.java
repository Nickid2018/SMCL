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
package com.github.nickid2018.smcl.optimize;

import java.util.*;
import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.statements.*;

public class NumberPool {

	private static final Map<Double, NumberStatement> numbers = new HashMap<>();

	public static final NumberStatement NUMBER_CONST_1 = new NumberStatement(1);
	public static final NumberStatement NUMBER_CONST_0 = new NumberStatement(0);
	public static final NumberStatement NUMBER_CONST_N1 = new NumberStatement(-1);

	public static NumberStatement getNumber(double value) {
		if (numbers.containsKey(value))
			return numbers.get(value);
		NumberStatement ns = new NumberStatement(value);
		numbers.put(value, ns);
		return ns;
	}

	public static Statement get(SMCL smcl, double value) {
		return smcl.settings.disableNumberPool && !isSpecial(value) ? new NumberStatement(value) : getNumber(value);
	}

	public static boolean isSpecial(double value) {
		return value == 0.0 || value == 1.0 || value == -1.0;
	}

	static {
		numbers.put(1.0, NUMBER_CONST_1);
		numbers.put(0.0, NUMBER_CONST_0);
		numbers.put(-0.0, NUMBER_CONST_0);
		numbers.put(-1.0, NUMBER_CONST_N1);
	}
}
