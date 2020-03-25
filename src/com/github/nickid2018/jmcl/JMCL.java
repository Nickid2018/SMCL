package com.github.nickid2018.jmcl;

import com.github.nickid2018.jmcl.func.*;

public class JMCL {

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
}
