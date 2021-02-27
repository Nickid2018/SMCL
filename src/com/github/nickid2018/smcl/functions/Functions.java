package com.github.nickid2018.smcl.functions;

import static com.github.nickid2018.smcl.functions.FunctionBuilder.*;

public class Functions {

	public static final UnaryFunctionBuilder SIN = UnaryFunctionBuilder.createBuilder("sin")
			.withResolve(RESOLVE_RADIANS).withFunction(Math::sin);
	public static final UnaryFunctionBuilder COS = UnaryFunctionBuilder.createBuilder("cos")
			.withResolve(RESOLVE_RADIANS).withFunction(Math::cos);
	public static final UnaryFunctionBuilder TAN = UnaryFunctionBuilder.createBuilder("tan")
			.withResolve(RESOLVE_RADIANS).withFunction(Math::tan);
	public static final UnaryFunctionBuilder ASIN = UnaryFunctionBuilder.createBuilder("asin").withFunction(Math::asin)
			.withResolveEnd(RESOLVE_DEGREES);
	public static final UnaryFunctionBuilder ACOS = UnaryFunctionBuilder.createBuilder("acos").withFunction(Math::acos)
			.withResolveEnd(RESOLVE_DEGREES);
	public static final UnaryFunctionBuilder ATAN = UnaryFunctionBuilder.createBuilder("atan").withFunction(Math::atan)
			.withResolveEnd(RESOLVE_DEGREES);
	public static final UnaryFunctionBuilder LN = UnaryFunctionBuilder.createBuilder("ln").withFunction(Math::log);
	public static final UnaryFunctionBuilder LG = UnaryFunctionBuilder.createBuilder("lg").withFunction(Math::log10);

}
