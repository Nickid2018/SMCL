package com.github.nickid2018.smcl;

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
		register.register(MathStatement.class, new char[] { '+', '-' });
		register.register(MultiplyStatement.class, new char[] { '*' });
		register.register(DivideStatement.class, new char[] { '/' });
		register.register(PowerStatement.class, new char[] { '^' });
		register.registerFunc(Sin.class, "sin");
		register.registerFunc(Cos.class, "cos");
		register.registerFunc(Tan.class, "tan");
		register.registerFunc(Asin.class, "asin");
		register.registerFunc(Acos.class, "acos");
		register.registerFunc(Atan.class, "atan");
		register.registerFunc(Lg.class, "lg");
		register.registerFunc(Ln.class, "ln");
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

	public final Statement format(String expr) {
		return format(expr, DefinedVariables.EMPTY_VARIABLES);
	}

	public final Statement format(String expr, DefinedVariables variables) {
		return null;
	}
}
