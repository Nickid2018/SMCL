package com.github.nickid2018.smcl.parser;

public interface TriFunction<T, U, V, R> {

	public R accept(T t, U u, V v);
}
