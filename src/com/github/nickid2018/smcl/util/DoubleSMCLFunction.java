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
package com.github.nickid2018.smcl.util;

import java.util.*;
import com.github.nickid2018.smcl.*;

@FunctionalInterface
public interface DoubleSMCLFunction {

	public double accept(double arg, SMCL smcl);

	public default DoubleSMCLFunction addThen(DoubleSMCLFunction after) {
		Objects.requireNonNull(after);
		return (arg, smcl) -> after.accept(accept(arg, smcl), smcl);
	}
}
