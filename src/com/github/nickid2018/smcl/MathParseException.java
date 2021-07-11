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
package com.github.nickid2018.smcl;

import com.github.nickid2018.smcl.parser.*;

public class MathParseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7821386249067385803L;

	private String statement;
	private StatementToken token;

	public MathParseException(String s, String statement, StatementToken token) {
		super(s);
		this.statement = statement;
		this.token = token;
	}

	@Override
	public String getMessage() {
		return super.getMessage() + (token != null
				? " (Position: " + token.pos + ", type: " + token.type + "): " + statement.substring(0, token.pos) + "["
						+ token + "]" + statement.substring(token.pos + token.length)
				: ": [" + statement + "]");
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}
}
