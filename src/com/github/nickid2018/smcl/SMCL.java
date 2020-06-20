package com.github.nickid2018.smcl;

import com.github.nickid2018.smcl.functions.*;
import com.github.nickid2018.smcl.optimize.*;
import com.github.nickid2018.smcl.statements.*;

public class SMCL {

	public static final VariableList EMPTY_ARGS = new VariableList();
	
	private static SMCL instance;
	
	public static synchronized SMCL getInstance() {
		return instance == null? instance = new SMCL(new SMCLSettings()) : instance;
	}
	
	public final SMCLSettings settings;
	
	public SMCL(SMCLSettings setting) {
		settings = setting;
	}

	public static final void init() {
		SMCLRegister.register(MathStatement.class, new char[] { '+', '-' });
		SMCLRegister.register(MultiplyStatement.class, new char[] { '*' });
		SMCLRegister.register(DivideStatement.class, new char[] { '/' });
		SMCLRegister.register(PowerStatement.class, new char[] { '^' });
		SMCLRegister.registerFunc(Sin.class, "sin");
		SMCLRegister.registerFunc(Cos.class, "cos");
		SMCLRegister.registerFunc(Tan.class, "tan");
		SMCLRegister.registerFunc(Asin.class, "asin");
		SMCLRegister.registerFunc(Acos.class, "acos");
		SMCLRegister.registerFunc(Atan.class, "atan");
		SMCLRegister.registerFunc(Lg.class, "lg");
		SMCLRegister.registerFunc(Ln.class, "ln");
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
		obj.jmcl = this;
		return obj;
	}

	public final void free(Statement statement) {
		getter.free(statement);
	}
}
