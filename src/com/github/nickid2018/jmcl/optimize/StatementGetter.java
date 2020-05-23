package com.github.nickid2018.jmcl.optimize;

import com.github.nickid2018.jmcl.*;

public interface StatementGetter {

	public <T extends Statement> T obtain(Class<T> cls);

	public void free(Statement statement);
}
