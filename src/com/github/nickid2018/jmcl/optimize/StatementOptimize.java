package com.github.nickid2018.jmcl.optimize;

import java.util.*;
import com.github.nickid2018.jmcl.*;
import com.github.nickid2018.jmcl.statements.*;

public class StatementOptimize {

	public static Statement optimizeMathStatement(MathStatement ms) {
		
		// Shorten Distance
		if (ms.getSubs().size() == 1 && ms.getSubs().get(0).value) {
			Statement statement = ms.getSubs().get(0).key;
			ms.tryOnlyFree();
			return statement;
		}

		// Merge Math Statements
		Set<Pair<Statement, Boolean>> mss = new HashSet<>();
		for (Pair<Statement, Boolean> en : ms.getSubs()) {
			if (en.getKey().getClass().equals(MathStatement.class)) {
				mss.add(en);
			}
		}
		if (mss.size() > 0) {
			for (Pair<Statement, Boolean> n : mss) {
				ms.getSubs().remove(n);
				ms.getSubs().addAll(((MathStatement) (n.key)).getSubs());
			}
		}

		// Number-Onlys
		if (ms.isAllNum())
			return NumberPool.getNumber(ms.calculate(JMCL.EMPTY_ARGS));

		// Merge Numbers
		double all = 0;
		mss.clear();
		for (Pair<Statement, Boolean> en : ms.getSubs()) {
			if (en.getKey() instanceof NumberStatement) {
				mss.add(en);
				if (en.getValue())
					all += en.getKey().calculate(JMCL.EMPTY_ARGS);
				else
					all -= en.getKey().calculate(JMCL.EMPTY_ARGS);
			}
		}
		if (mss.size() > 1) {
			for (Pair<Statement, Boolean> n : mss) {
				ms.getSubs().remove(n);
			}
			if (all != 0) {
				NumberStatement num = NumberPool.getNumber(Math.abs(all));
				ms.getSubs().add(new Pair<>(num, all > 0));
			}
		}

		return ms;
	}
}
