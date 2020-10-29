package com.github.nickid2018.smcl.parser;

public class StatementToken {
	
	public String detail = "";
	public StatementTokenType type;
	public int pos;

	public void append(char c) {
		this.detail = this.detail + c;
	}

	public void append(String s) {
		this.detail = this.detail + s;
	}

	public char charAt(int pos) {
		return this.detail.charAt(pos);
	}

	public int length() {
		return this.detail.length();
	}

	public String toString() {
		return this.detail;
	}
}
