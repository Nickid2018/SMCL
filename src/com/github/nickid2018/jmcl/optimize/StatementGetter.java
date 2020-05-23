package com.github.nickid2018.jmcl.optimize;

import com.github.nickid2018.jmcl.*;

public interface StatementGetter {
	
	public <T extends MathStatement> T obtain(Class<T> cls);
	
	public void free(MathStatement statement);
}
