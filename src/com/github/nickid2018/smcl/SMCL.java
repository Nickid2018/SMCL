package com.github.nickid2018.smcl;

import com.github.nickid2018.smcl.parser.*;
import com.github.nickid2018.smcl.optimize.*;
import com.github.nickid2018.smcl.functions.*;
import com.github.nickid2018.smcl.statements.*;

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
				statement.setValues(statements);
				return statement;
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
				statement.setValues(statements[0], statements[1].getNegative());
				return statement;
			}
		}));
		register.registerUnaryOperator("-",
				new UnaryOperatorParser<>(60, false, (smcl, statement) -> statement.getNegative()));
		register.registerOperator("*", new BinaryOperatorParser<>(30, true, (smcl, statements) -> {
			if (statements[0] instanceof MultiplyStatement) {
				MultiplyStatement get = (MultiplyStatement) statements[0];
				Statement other = statements[1];
				return get.addStatement(other);
			} else {
				MultiplyStatement statement = smcl.obtain(MultiplyStatement.class);
				statement.setValues(statements);
				return statement;
			}
		}));
		register.registerOperator("/", new BinaryOperatorParser<>(30, true, (smcl, statements) -> {
			if (statements[0] instanceof DivideStatement) {
				DivideStatement get = (DivideStatement) statements[0];
				Statement other = statements[1];
				return get.addStatement(other);
			} else {
				DivideStatement statement = smcl.obtain(DivideStatement.class);
				statement.setValues(statements);
				return statement;
			}
		}));
		register.registerOperator("^", new BinaryOperatorParser<>(40, true, (smcl, statements) -> {
			if (statements[0] instanceof PowerStatement) {
				PowerStatement get = (PowerStatement) statements[0];
				Statement other = statements[1];
				return get.addStatement(other);
			} else {
				PowerStatement statement = smcl.obtain(PowerStatement.class);
				statement.setValues(statements);
				return statement;
			}
		}));
		register.registerFunction("sin", new MathematicsFunctionParser<>(1,
				(smcl, statements) -> (Sin) (smcl.obtain(Sin.class).setValues(statements[0]))));
		register.registerFunction("cos", new MathematicsFunctionParser<>(1,
				(smcl, statements) -> (Cos) (smcl.obtain(Cos.class).setValues(statements[0]))));
		register.registerFunction("tan", new MathematicsFunctionParser<>(1,
				(smcl, statements) -> (Tan) (smcl.obtain(Tan.class).setValues(statements[0]))));
		register.registerFunction("asin", new MathematicsFunctionParser<>(1,
				(smcl, statements) -> (Asin) (smcl.obtain(Asin.class).setValues(statements[0]))));
		register.registerFunction("acos", new MathematicsFunctionParser<>(1,
				(smcl, statements) -> (Acos) (smcl.obtain(Acos.class).setValues(statements[0]))));
		register.registerFunction("atan", new MathematicsFunctionParser<>(1,
				(smcl, statements) -> (Atan) (smcl.obtain(Atan.class).setValues(statements[0]))));
		register.registerFunction("ln", new MathematicsFunctionParser<>(1,
				(smcl, statements) -> (Ln) (smcl.obtain(Ln.class).setValues(statements[0]))));
		register.registerFunction("lg", new MathematicsFunctionParser<>(1,
				(smcl, statements) -> (Lg) (smcl.obtain(Lg.class).setValues(statements[0]))));
	}

	private StatementGetter getter = new DefaultStatementGetter();

	public final void setStatementGetter(StatementGetter getter) {
		this.getter = getter;
	}

	public final StatementGetter getGetter() {
		return getter;
	}

	public final <T extends Statement> T obtain(Class<T> cls) {
		T obj = getter.obtain(cls);
		obj.smcl = this;
		return obj;
	}

	public final void free(Statement statement) {
		getter.free(statement);
	}

	public final Statement format(String expr) throws MathException {
		return format(expr, DefinedVariables.EMPTY_VARIABLES);
	}

	public final Statement format(String expr, DefinedVariables variables) throws MathException {
		return StatementGenerator.createAST(expr, this, variables);
	}
}
