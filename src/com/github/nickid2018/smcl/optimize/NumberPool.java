package com.github.nickid2018.smcl.optimize;

import java.util.*;
import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.statements.*;

public class NumberPool {

	private static final Map<Double, NumberStatement> numbers = new HashMap<>();

	public static NumberStatement getNumber(double value) {
		if (numbers.containsKey(value))
			return numbers.get(value);
		NumberStatement ns = new NumberStatement(value);
		numbers.put(value, ns);
		return ns;
	}

	public static Statement get(SMCL smcl, double value) {
		return smcl.settings.disableNumberPool ? new NumberStatement(value) : getNumber(value);
	}
}
