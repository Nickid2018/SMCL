package com.github.nickid2018.jmcl.optimize;

import java.util.*;
import com.github.nickid2018.jmcl.statements.*;

public class NumberPool {

	private static final Map<Double, NumberStatement> numbers = new HashMap<>();

	public static NumberStatement getNumber(double value) {
		if (numbers.containsKey(value))
			return numbers.get(value);
		NumberStatement ns = new NumberStatement(value);
		numbers.put(value, ns);
		return ns;
	}
}
