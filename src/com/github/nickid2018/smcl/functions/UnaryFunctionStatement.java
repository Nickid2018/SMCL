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
package com.github.nickid2018.smcl.functions;

import com.github.nickid2018.smcl.*;

public abstract class UnaryFunctionStatement extends FunctionStatement {

	protected Statement innerStatement;

	public UnaryFunctionStatement(Statement statement) {
		super(statement.getSMCL(), statement.getVariables());
		setInnerStatement(statement);
	}

	public Statement getInnerStatement() {
		return innerStatement;
	}

	public UnaryFunctionStatement setInnerStatement(Statement innerStatement) {
		this.innerStatement = innerStatement;
		return this;
	}

}