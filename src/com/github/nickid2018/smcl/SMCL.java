package com.github.nickid2018.smcl;

import com.github.nickid2018.smcl.parser.*;
import com.github.nickid2018.smcl.optimize.*;
import com.github.nickid2018.smcl.functions.*;
import com.github.nickid2018.smcl.statements.arith.*;

public class SMCL {

	public static final VariableList EMPTY_ARGS = new VariableList();

	private static SMCL instance;

	public static synchronized SMCL getInstance() {
		return instance == null ? instance = new SMCL(new SMCLSettings()) : instance;
	}

	public final SMCLSettings settings;
	public final SMCLRegister register;
	public final GlobalVariables globalvars;

	public SMCL(SMCLSettings setting) {
		settings = setting;
		register = new SMCLRegister(this);
		globalvars = new GlobalVariables();
	}

	public final void init() {
		register.registerOperator("+", new BinaryOperatorParser<>(20, true, (smcl, statements) -> {
			if (statements[0] instanceof MathStatement) {
				MathStatement get = (MathStatement) statements[0];
				return get.addStatement(statements[1]);
			} else {
				MathStatement statement = smcl.obtain(MathStatement.class);
				return statement.addStatements(statements);
			}
		}));
		register.registerUnaryOperator("+", new UnaryOperatorParser<>(60, false, (smcl, statement) -> statement));
		register.registerOperator("-", new BinaryOperatorParser<>(20, true, (smcl, statements) -> {
			if (statements[0] instanceof MathStatement) {
				MathStatement get = (MathStatement) statements[0];
				Statement other = statements[1];
				return get.addStatement(other.getNegative());
			} else {
				MathStatement statement = smcl.obtain(MathStatement.class);
				return statement.addStatements(statements[0], statements[1].getNegative());
			}
		}));
		register.registerUnaryOperator("-",
				new UnaryOperatorParser<>(60, false, (smcl, statement) -> statement.getNegative()));
		register.registerOperator("*", new BinaryOperatorParser<>(30, true, (smcl, statements) -> {
			if (statements[0] instanceof MultiplyStatement) {
				MultiplyStatement get = (MultiplyStatement) statements[0];
				Statement other = statements[1];
				return get.addMultiplier(other);
			} else {
				MultiplyStatement statement = smcl.obtain(MultiplyStatement.class);
				return statement.addMultipliers(statements);
			}
		}));
		register.registerOperator("/", new BinaryOperatorParser<>(30, true, (smcl, statements) -> {
			if (statements[0] instanceof DivideStatement) {
				DivideStatement get = (DivideStatement) statements[0];
				Statement other = statements[1];
				return get.addDivisor(other);
			} else {
				DivideStatement statement = smcl.obtain(DivideStatement.class);
				return statement.putDividendAndDivisors(statements);
			}
		}));
		register.registerOperator("^", new BinaryOperatorParser<>(40, true, (smcl, statements) -> {
			if (statements[0] instanceof PowerStatement) {
				PowerStatement get = (PowerStatement) statements[0];
				Statement other = statements[1];
				return get.addExponent(other);
			} else {
				PowerStatement statement = smcl.obtain(PowerStatement.class);
				return statement.putBaseAndExponents(statements);
			}
		}));
		register.registerFunction("sin", new UnaryMathematicsFunctionParser<>(Functions.SIN));
		register.registerFunction("cos", new UnaryMathematicsFunctionParser<>(Functions.COS));
		register.registerFunction("tan", new UnaryMathematicsFunctionParser<>(Functions.TAN));
		register.registerFunction("asin", new UnaryMathematicsFunctionParser<>(Functions.ASIN));
		register.registerFunction("acos", new UnaryMathematicsFunctionParser<>(Functions.ACOS));
		register.registerFunction("atan", new UnaryMathematicsFunctionParser<>(Functions.ATAN));
		register.registerFunction("ln", new UnaryMathematicsFunctionParser<>(Functions.LN));
		register.registerFunction("lg", new UnaryMathematicsFunctionParser<>(Functions.LG));
	}

	public final void initWithLogicOperator() {
		init();

	}

	private StatementGetter getter = new DefaultStatementGetter();

	public final <T extends Statement> T obtain(Class<T> cls) {
		T obj = getter.obtain(cls);
		obj.smcl = this;
		return obj;
	}

	public final Statement format(String expr) throws MathParseException {
		return format(expr, DefinedVariables.EMPTY_VARIABLES);
	}

	public final Statement format(String expr, DefinedVariables variables) throws MathParseException {
		return StatementGenerator.createAST(expr, this, variables);
	}
}
