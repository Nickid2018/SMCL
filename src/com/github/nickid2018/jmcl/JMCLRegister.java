package com.github.nickid2018.jmcl;

import java.util.*;
import java.util.Map.*;

import com.github.nickid2018.jmcl.func.*;

import java.lang.reflect.*;

public class JMCLRegister {

	private static final Map<Class<?>, char[]> registered = new HashMap<>();
	private static final Map<Class<?>, String> registeredfunc = new HashMap<>();
	private static final Set<String> variablemap = new HashSet<>();

	public static final void register(Class<? extends MathStatement> clazz, char[] sign) {
		registered.put(clazz, sign);
	}

	public static final void registerFunc(Class<? extends Function> clazz, String sign) {
		registeredfunc.put(clazz, sign);
	}

	public static final void registerVariable(String name) {
		variablemap.add(name);
	}

	public static final MathStatement getStatement(String s) throws MathException {
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
		for (Entry<Class<?>, char[]> en : registered.entrySet()) {
			for (char c : en.getValue())
				if (mapping.indexOf(c) >= 0) {
					Class<?> cls = en.getKey();
					try {
						Method m = cls.getMethod("format", String.class);
						MathStatement ms = (MathStatement) m.invoke(cls, s);
						if (ms.isAllNum())
							ms = new Number(ms.calc(new HashMap<>()));
						return ms;
					} catch (Exception e) {
						throw new RuntimeException("JMCL Error");
					}
				}
		}
		for (Map.Entry<Class<?>, String> en : registeredfunc.entrySet()) {
			if (s.startsWith(en.getValue())) {
				Class<?> cls = en.getKey();
				try {
					Method m = cls.getMethod("format", String.class);
					MathStatement ms = (MathStatement) m.invoke(cls, s);
					if (ms.isAllNum())
						ms = new Number(ms.calc(new HashMap<>()));
					return ms;
				} catch (Exception e) {
					if (e instanceof InvocationTargetException)
						throw new MathException("Parsing error", s, 0, e.getCause());
					throw new RuntimeException("JMCL Error");
				}
			}
		}
		if (s.startsWith("(")) {
			return MathStatement.format(s.substring(1, s.length() - 1));
		}
		if (variablemap.contains(s)) {
			return new Variable(s);
		} else {
			try {
				double num = Double.parseDouble(s);
				return new Number(num);
			} catch (NumberFormatException e) {
				throw new MathException("Parsing error", s, 0);
			}
		}
	}
}
