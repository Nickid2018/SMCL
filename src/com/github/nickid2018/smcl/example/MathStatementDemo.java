package com.github.nickid2018.smcl.example;

import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.parser.*;

public class MathStatementDemo {

	public static void main(String[] args) throws Exception {
		SMCL smcl = SMCL.getInstance();
		smcl.init();
		smcl.globalvars.registerVariable("x");
		Statement statement = smcl.format("4.715841611644225E23^x");
		System.out.println(statement.calculate(new VariableList().addVariableValue("x", 0.1)));
		StatementTokenizer tokens = new StatementTokenizer(smcl, "1.0-3E10/tan(x)");
		while (tokens.hasNextToken()) {
			StatementToken token = tokens.nextToken();
			System.out.println(token + " " + token.type);
		}
		System.out.println(StatementGenerator.doRPN("1.0-3E10/tan(x)", smcl));
		for(StatementToken token:StatementGenerator.doRPN("1.0-3E10/tan(x)", smcl)) {
			System.out.println(token + " " + token.type);
		}
	}
}
