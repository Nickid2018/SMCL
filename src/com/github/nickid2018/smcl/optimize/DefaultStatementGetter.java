package com.github.nickid2018.smcl.optimize;

import com.github.nickid2018.smcl.*;

public class DefaultStatementGetter implements StatementGetter {

	@Override
	public <T extends Statement> T obtain(Class<T> cls) {
		try {
			return cls.newInstance();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void free(Statement statement) {
	}
}
