package com.github.nickid2018.smcl.util;

import java.util.*;

@FunctionalInterface
public interface Double2DoubleFunction {

	public double accept(double arg);

	public default Double2DoubleFunction addThen(Double2DoubleFunction after) {
		Objects.requireNonNull(after);
		return arg -> after.accept(accept(arg));
	}
}
