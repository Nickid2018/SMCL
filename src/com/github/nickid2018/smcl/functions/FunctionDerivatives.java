package com.github.nickid2018.smcl.functions;

import java.util.function.*;
import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.optimize.*;
import com.github.nickid2018.smcl.statements.arith.*;

public class FunctionDerivatives {

	public static final Function<Statement, Statement> DERIVATIVE_SIN = statement -> Functions.COS
			.create(statement.getSMCL(), statement);

	public static final Function<Statement, Statement> DERIVATIVE_COS = statement -> Functions.SIN
			.create(statement.getSMCL(), statement).getNegative();

	public static final Function<Statement, Statement> DERIVATIVE_TAN = statement -> {
		Statement base = Functions.SEC.create(statement.getSMCL(), statement);
		return new PowerStatement(statement.getSMCL(), statement.getVariables()).putBaseAndExponents(base,
				NumberPool.getNumber(2));
	};

	public static final Function<Statement, Statement> DERIVATIVE_SEC = statement -> {
		Statement base = Functions.SEC.create(statement.getSMCL(), statement);
		Statement base2 = Functions.TAN.create(statement.getSMCL(), statement);
		return new MultiplyStatement(statement.getSMCL(), statement.getVariables()).addMultipliers(base, base2);
	};

	public static final Function<Statement, Statement> DERIVATIVE_CSC = statement -> {
		Statement base = Functions.CSC.create(statement.getSMCL(), statement);
		Statement base2 = Functions.COT.create(statement.getSMCL(), statement);
		return new MultiplyStatement(statement.getSMCL(), statement.getVariables()).addMultipliers(base, base2)
				.getNegative();
	};

	public static final Function<Statement, Statement> DERIVATIVE_COT = statement -> {
		Statement base = Functions.CSC.create(statement.getSMCL(), statement);
		return new PowerStatement(statement.getSMCL(), statement.getVariables())
				.putBaseAndExponents(base, NumberPool.getNumber(2)).getNegative();
	};

	public static final Function<Statement, Statement> DERIVATIVE_ASIN = statement -> {
		Statement pws = new PowerStatement(statement.getSMCL(), statement.getVariables())
				.putBaseAndExponents(statement, NumberPool.getNumber(2)).getNegative();
		MathStatement base = new MathStatement(statement.getSMCL(), statement.getVariables());
		base.addStatements(NumberPool.NUMBER_CONST_1, pws);
		Statement sqrt = Functions.SQRT.create(statement.getSMCL(), base);
		return new DivideStatement(statement.getSMCL(), statement.getVariables())
				.putDividendAndDivisors(NumberPool.NUMBER_CONST_1, sqrt);
	};

	public static final Function<Statement, Statement> DERIVATIVE_ACOS = statement -> {
		Statement pws = new PowerStatement(statement.getSMCL(), statement.getVariables())
				.putBaseAndExponents(statement, NumberPool.getNumber(2)).getNegative();
		MathStatement base = new MathStatement(statement.getSMCL(), statement.getVariables());
		base.addStatements(NumberPool.NUMBER_CONST_1, pws);
		Statement sqrt = Functions.SQRT.create(statement.getSMCL(), base);
		return new DivideStatement(statement.getSMCL(), statement.getVariables())
				.putDividendAndDivisors(NumberPool.NUMBER_CONST_1, sqrt).getNegative();
	};

	public static final Function<Statement, Statement> DERIVATIVE_ATAN = statement -> {
		Statement pws = new PowerStatement(statement.getSMCL(), statement.getVariables()).putBaseAndExponents(statement,
				NumberPool.getNumber(2));
		MathStatement base = new MathStatement(statement.getSMCL(), statement.getVariables());
		base.addStatements(NumberPool.NUMBER_CONST_1, pws);
		return new DivideStatement(statement.getSMCL(), statement.getVariables())
				.putDividendAndDivisors(NumberPool.NUMBER_CONST_1, base);
	};

	public static final Function<Statement, Statement> DERIVATIVE_SINH = statement -> Functions.COSH
			.create(statement.getSMCL(), statement);

	public static final Function<Statement, Statement> DERIVATIVE_COSH = statement -> Functions.SINH
			.create(statement.getSMCL(), statement);

	public static final Function<Statement, Statement> DERIVATIVE_TANH = statement -> {
		Statement base = Functions.COSH.create(statement.getSMCL(), statement);
		return new DivideStatement(statement.getSMCL(), statement.getVariables()).putDividendAndDivisors(
				NumberPool.NUMBER_CONST_1, new PowerStatement(statement.getSMCL(), statement.getVariables())
						.putBaseAndExponents(base, NumberPool.getNumber(2)));
	};

	public static final Function<Statement, Statement> DERIVATIVE_LN = statement -> new DivideStatement(
			statement.getSMCL(), statement.getVariables()).putDividendAndDivisors(NumberPool.NUMBER_CONST_1, statement);

	public static final Function<Statement, Statement> DERIVATIVE_LG = statement -> new DivideStatement(
			statement.getSMCL(), statement.getVariables()).putDividendAndDivisors(NumberPool.NUMBER_CONST_1, statement,
					NumberPool.getNumber(Math.log(10)));

	public static final Function<Statement, Statement> DERIVATIVE_SQRT = statement -> new PowerStatement(
			statement.getSMCL(), statement.getVariables()).putBaseAndExponents(statement, NumberPool.getNumber(-0.5));

	public static final Function<Statement, Statement> DERIVATIVE_CBRT = statement -> new PowerStatement(
			statement.getSMCL(), statement.getVariables()).putBaseAndExponents(statement, NumberPool.getNumber(-2 / 3));

	public static final Function<Statement, Statement> DERIVATIVE_EXP = statement -> new PowerStatement(
			statement.getSMCL(), statement.getVariables()).putBaseAndExponents(NumberPool.getNumber(Math.E), statement);

	// Warning: Assigned x=0, the derivative doesn't exist!
	public static final Function<Statement, Statement> DERIVATIVE_ABS = statement -> Functions.SGN
			.create(statement.getSMCL(), statement);
}
