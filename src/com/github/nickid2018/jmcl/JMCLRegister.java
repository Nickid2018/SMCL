package com.github.nickid2018.jmcl;

import java.util.*;
import java.util.Map.*;
import java.lang.reflect.*;

import com.github.nickid2018.jmcl.functions.*;
import com.github.nickid2018.jmcl.optimize.*;
import com.github.nickid2018.jmcl.statements.*;

public class JMCLRegister {

	private static final Map<Class<? extends Statement>, char[]> registered = new HashMap<>();
	private static final Map<Class<? extends Statement>, String> registeredfunc = new HashMap<>();

	public static final void register(Class<? extends Statement> clazz, char[] sign) {
		registered.put(clazz, sign);
	}

	public static final void unregister(Class<? extends Statement> clazz) {
		registered.remove(clazz);
	}

	public static final void registerFunc(Class<? extends FunctionStatement> clazz, String sign) {
		registeredfunc.put(clazz, sign);
	}

	public static final void unregisterFunc(Class<? extends FunctionStatement> clazz) {
		registeredfunc.remove(clazz);
	}

	public static final Statement getStatement(String s) throws MathException {

		// Operator & String mapping
		String mapping = "";
		int intimes = 0;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '(')
				intimes++;
			if (c == ')')
				intimes--;
			if (intimes == 0)
				mapping += c;
		}

		// Search Operator
		for (Entry<Class<? extends Statement>, char[]> en : registered.entrySet()) {
			for (char c : en.getValue())
				if (mapping.indexOf(c) >= 0) {
					Class<? extends Statement> cls = en.getKey();
					try {
						Method m = cls.getMethod("format", String.class);
						Statement ms = (Statement) m.invoke(cls, s);
						if (ms.isAllNum() && JMCL.optimize)
							ms = NumberPool.getNumber(ms.calculate(JMCL.EMPTY_ARGS));
						return ms;
					} catch (Exception e) {
						if (e instanceof InvocationTargetException)
							throw new MathException("Parsing error", s, 0, e.getCause());
						throw new RuntimeException("JMCL Error", e);
					}
				}
		}

		// Search Function
		for (Entry<Class<? extends Statement>, String> en : registeredfunc.entrySet()) {
			if (s.startsWith(en.getValue())) {
				Class<? extends Statement> cls = en.getKey();
				try {
					Method m = cls.getMethod("format", String.class);
					Statement ms = (Statement) m.invoke(cls, s);
					if (ms.isAllNum() && JMCL.optimize)
						ms = NumberPool.getNumber(ms.calculate(JMCL.EMPTY_ARGS));
					return ms;
				} catch (Exception e) {
					if (e instanceof InvocationTargetException)
						throw new MathException("Parsing error", s, 0, e.getCause());
					throw new RuntimeException("JMCL Error", e);
				}
			}
		}

		// Sub Statement
		if (s.startsWith("(")) {
			return MathStatement.format(s.substring(1, s.length() - 1));
		}

		// Last Choice
		if (GlobalVariables.haveVariable(s)) {
			return GlobalVariables.getVariable(s);
		} else {
			try {
				double num = Double.parseDouble(s);
				return NumberPool.getNumber(num);
			} catch (NumberFormatException e) {
				throw new MathException("Parsing error", s, 0);
			}
		}
	}
}
