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
