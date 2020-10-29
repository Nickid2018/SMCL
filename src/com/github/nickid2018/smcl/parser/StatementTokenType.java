package com.github.nickid2018.smcl.parser;

public enum StatementTokenType {

	/**
	 * For variables defined in SMCL
	 */
	VARIABLE,
	/**
	 * For functions like sin or atan
	 */
	FUNCTION,
	/**
	 * Plain numbers
	 */
	NUMBER,
	/**
	 * Operators like '+' or '-'
	 */
	OPERATOR,
	/**
	 * Unary operator
	 */
	UNARY_OPERATOR,
	/**
	 * Left blanket (
	 */
	OPEN_PAREN,
	/**
	 * Comma ,
	 */
	COMMA,
	/**
	 * Right blanket )
	 */
	CLOSE_PAREN,
	/**
	 * Hex number
	 */
	HEX_NUMBER
}
