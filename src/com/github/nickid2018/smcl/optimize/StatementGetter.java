package com.github.nickid2018.smcl.optimize;

import com.github.nickid2018.smcl.*;

public interface StatementGetter {

	public <T extends Statement> T obtain(Class<T> cls);

	public void free(Statement statement);
}
