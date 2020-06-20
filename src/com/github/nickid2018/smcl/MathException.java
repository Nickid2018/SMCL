package com.github.nickid2018.smcl;

public class MathException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7821386249067385803L;

	private String statement;
	private int pos;

	public MathException(String s, String statement, int pos) {
		super(s);
		this.statement = statement;
		this.pos = pos;
	}

	public MathException(String s, String statement, int pos, Throwable e) {
		super(s, e);
		this.statement = statement;
		this.pos = pos;
	}

	@Override
	public String getMessage() {
		String s = "";
		for (int i = 0; i < pos; i++)
			s += " ";
		return super.getMessage() + " (Position: " + pos + ")\n" + statement + "\n" + s + "^";
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}
}
