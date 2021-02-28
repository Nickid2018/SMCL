package com.github.nickid2018.smcl.functions;

import java.util.function.*;
import static com.github.nickid2018.smcl.functions.FunctionBuilder.*;

public class Functions {

	// Domain of the logarithm
	public static final DoubleConsumer DOMAIN_LOG = checkDomainExclude(arg -> arg <= 0,
			arg -> "invalid argument for logarithm functions: " + arg + " <=0");
	public static final DoubleConsumer DOMAIN_SQRT = checkDomainExclude(arg -> arg < 0,
			arg -> "invalid argument for square root: " + arg + " <0");
	// Domain of the tangent/cotangent
	public static final DoubleConsumer DOMAIN_TAN = checkDomainExclude(arg -> {
		double deg = arg * 2 / Math.PI;
		int multi = (int) deg;
		if (Math.abs(deg - multi) > 1E-5)
			return false;
		return multi % 2 == 1;
	}, arg -> "invalid argument for tan/cot: " + arg);
	// Domain of the arcsin and arccos
	public static final DoubleConsumer DOMAIN_ARC = checkDomainInclude(arg -> arg <= 1 && arg >= -1,
			arg -> "invalid argument for asin/acos: " + arg + " doesn't belong [-1,1]");

	// Basic trigonometric functions
	// sine (sin)
	public static final UnaryFunctionBuilder SIN = UnaryFunctionBuilder.createBuilder("sin")
			.withResolve(RESOLVE_RADIANS).withFunction(Math::sin);
	// cosine (cos)
	public static final UnaryFunctionBuilder COS = UnaryFunctionBuilder.createBuilder("cos")
			.withResolve(RESOLVE_RADIANS).withFunction(Math::cos);
	// tangent (tan)
	public static final UnaryFunctionBuilder TAN = UnaryFunctionBuilder.createBuilder("tan").withDomain(DOMAIN_TAN)
			.withResolve(RESOLVE_RADIANS).withFunction(Math::tan);

	// Advanced trigonometric functions
	// cosecant (csc)
	public static final UnaryFunctionBuilder CSC = SIN.copyWithoutFunction("csc")
			.withFunction(arg -> 1 / Math.sin(arg));
	// secant (sec)
	public static final UnaryFunctionBuilder SEC = COS.copyWithoutFunction("sec")
			.withFunction(arg -> 1 / Math.cos(arg));
	// cotangent (cot)
	public static final UnaryFunctionBuilder COT = TAN.copyWithoutFunction("cot")
			.withFunction(arg -> 1 / Math.tan(arg));

	// Basic inverse trigonometric functions
	// arc-sine (asin, arcsin)
	public static final UnaryFunctionBuilder ASIN = UnaryFunctionBuilder.createBuilder("asin").withDomain(DOMAIN_ARC)
			.withFunction(Math::asin).withResolveEnd(RESOLVE_DEGREES);
	// arc-cosine (acos, arccos)
	public static final UnaryFunctionBuilder ACOS = UnaryFunctionBuilder.createBuilder("acos").withDomain(DOMAIN_ARC)
			.withFunction(Math::acos).withResolveEnd(RESOLVE_DEGREES);
	// arc-tangent (atan, arctan)
	public static final UnaryFunctionBuilder ATAN = UnaryFunctionBuilder.createBuilder("atan").withFunction(Math::atan)
			.withResolveEnd(RESOLVE_DEGREES);

	// Basic hyperbolic functions
	// hyperbolic sine (sinh, sh)
	public static final UnaryFunctionBuilder SINH = UnaryFunctionBuilder.createBuilder("sinh").withFunction(Math::sinh);
	// hyperbolic cosine (cosh, ch)
	public static final UnaryFunctionBuilder COSH = UnaryFunctionBuilder.createBuilder("cosh").withFunction(Math::cosh);
	// hyperbolic tangent (tanh, th)
	public static final UnaryFunctionBuilder TANH = UnaryFunctionBuilder.createBuilder("tanh").withFunction(Math::tanh);

	// Basic logarithm functions
	// natural logarithm function (ln)
	public static final UnaryFunctionBuilder LN = UnaryFunctionBuilder.createBuilder("ln").withDomain(DOMAIN_LOG)
			.withFunction(Math::log);
	// common logarithm function (lg)
	public static final UnaryFunctionBuilder LG = UnaryFunctionBuilder.createBuilder("lg").withDomain(DOMAIN_LOG)
			.withFunction(Math::log10);

	// Basic power & root functions
	// square root (sqrt)
	public static final UnaryFunctionBuilder SQRT = UnaryFunctionBuilder.createBuilder("sqrt").withDomain(DOMAIN_SQRT)
			.withFunction(Math::sqrt);
	// cubic root (cbrt)
	public static final UnaryFunctionBuilder CBRT = UnaryFunctionBuilder.createBuilder("cbrt").withFunction(Math::cbrt);
	// e's power (exp)
	public static final UnaryFunctionBuilder EXP = UnaryFunctionBuilder.createBuilder("exp").withFunction(Math::exp);

	// Common functions
	// absolute value (abs or |x|)
	public static final UnaryFunctionBuilder ABS = UnaryFunctionBuilder.createBuilder("abs").withFunction(Math::abs);
	// signum function (sgn)
	public static final UnaryFunctionBuilder SGN = UnaryFunctionBuilder.createBuilder("sgn").withFunction(Math::signum);
	// round-up function or "ceil" function (ceil)
	public static final UnaryFunctionBuilder CEIL = UnaryFunctionBuilder.createBuilder("ceil").withFunction(Math::ceil);
	// round-down function or "floor" function (floor)
	public static final UnaryFunctionBuilder FLOOR = UnaryFunctionBuilder.createBuilder("floor")
			.withFunction(Math::floor);
	// rounding function (round)
	public static final UnaryFunctionBuilder ROUND = UnaryFunctionBuilder.createBuilder("round")
			.withFunction(Math::round);
}
