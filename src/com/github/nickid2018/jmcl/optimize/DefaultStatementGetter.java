package com.github.nickid2018.jmcl.optimize;

import com.github.nickid2018.jmcl.*;

public class DefaultStatementGetter implements StatementGetter {

	@Override
	public <T extends MathStatement> T obtain(Class<T> cls) {
		try {
			return cls.newInstance();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void free(MathStatement statement) {
	}
}
