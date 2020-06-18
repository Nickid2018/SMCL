package com.github.nickid2018.jmcl;

import com.github.nickid2018.jmcl.functions.*;
import com.github.nickid2018.jmcl.optimize.*;
import com.github.nickid2018.jmcl.statements.*;

public class JMCL {

	public static final VariableList EMPTY_ARGS = new VariableList();
	
	private static JMCL instance;
	
	public static synchronized JMCL getInstance() {
		return instance == null? instance = new JMCL(new JMCLSettings()) : instance;
	}
	
	public final JMCLSettings settings;
	
	public JMCL(JMCLSettings setting) {
		settings = setting;
	}

	public static final void init() {
		JMCLRegister.register(MathStatement.class, new char[] { '+', '-' });
		JMCLRegister.register(MultiplyStatement.class, new char[] { '*' });
		JMCLRegister.register(DivideStatement.class, new char[] { '/' });
		JMCLRegister.register(PowerStatement.class, new char[] { '^' });
		JMCLRegister.registerFunc(Sin.class, "sin");
		JMCLRegister.registerFunc(Cos.class, "cos");
		JMCLRegister.registerFunc(Tan.class, "tan");
		JMCLRegister.registerFunc(Asin.class, "asin");
		JMCLRegister.registerFunc(Acos.class, "acos");
		JMCLRegister.registerFunc(Atan.class, "atan");
		JMCLRegister.registerFunc(Lg.class, "lg");
		JMCLRegister.registerFunc(Ln.class, "ln");
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
