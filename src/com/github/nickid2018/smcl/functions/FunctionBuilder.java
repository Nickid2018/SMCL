package com.github.nickid2018.smcl.functions;

import java.util.function.*;
import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.set.*;
import com.github.nickid2018.smcl.util.*;

public abstract class FunctionBuilder<T extends FunctionStatement> {

	public static final DoubleConsumer ALL_DOMAIN = arg -> {
		;
	};

	public static final DoubleConsumer checkDomainExclude(DoublePredicate exclude, DoubleFunction<String> errorString) {
		return arg -> {
			if (exclude.test(arg))
				throw new ArithmeticException(errorString.apply(arg));
		};
	}

	public static final DoubleConsumer checkDomainInclude(DoublePredicate include, DoubleFunction<String> errorString) {
		return arg -> {
			if (!include.test(arg))
				throw new ArithmeticException(errorString.apply(arg));
		};
	}

	public static final DoubleConsumer checkDomainExclude(NumberSet set, DoubleFunction<String> errorString) {
		return arg -> {
			if (set.isBelongTo(arg))
				throw new ArithmeticException(errorString.apply(arg));
		};
	}

	public static final DoubleConsumer checkDomainInclude(NumberSet set, DoubleFunction<String> errorString) {
		return arg -> {
			if (!set.isBelongTo(arg))
				throw new ArithmeticException(errorString.apply(arg));
		};
	}

	public static final Double2DoubleFunction DEFAULT_RESULT = arg -> arg;

	public static final DoubleSMCLFunction DEFAULT_RESOLVE = (arg, smcl) -> arg;

	public static final DoubleSMCLFunction RESOLVE_RADIANS = (arg,
			smcl) -> smcl.settings.degreeAngle ? Math.toRadians(arg) : arg;

	public static final DoubleSMCLFunction RESOLVE_DEGREES = (arg,
			smcl) -> smcl.settings.degreeAngle ? Math.toDegrees(arg) : arg;

	protected final String name;

	public FunctionBuilder(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public abstract T create(SMCL smcl, Statement... statements);
}
