package com.github.nickid2018.smcl.optimize;

import java.util.*;
import java.lang.reflect.*;
import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.functions.*;

public class DefaultStatementGetter implements StatementGetter {

	private static Map<Class<?>, Constructor<? extends UnaryFunctionStatement>> unaries = new HashMap<>();

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Statement> T obtain(Class<T> cls) {
		try {
			if (cls.getSuperclass().equals(UnaryFunctionStatement.class)) {
				return (T) unaries.computeIfAbsent(cls, m -> {
					try {
						return (Constructor<? extends UnaryFunctionStatement>) cls.getConstructor(Statement.class);
					} catch (Exception e) {
						return null;
					}
				}).newInstance((Object) null);
			} else {
				return cls.newInstance();
			}
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void free(Statement statement) {
	}
}
