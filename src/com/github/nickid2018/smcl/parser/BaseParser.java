package com.github.nickid2018.smcl.parser;

import java.util.*;
import com.github.nickid2018.smcl.*;

public class BaseParser {

	public static Statement parseStatement(String expr, SMCL smcl) throws MathException {
		CharArrayString cexpr = new CharArrayString(expr);
		LevelInfo level = new LevelInfo();
		CharsInfo chars = new CharsInfo(smcl);
		Queue<StructEntry> entryQueue = new LinkedList<>();
		int pos;
		for (; (pos = level.position) < expr.length(); level.position++) {
			char now = cexpr.string[pos];
			if (now == '(') {
				level.levelUp();
				entryQueue.offer(subParseStatement(cexpr, level, smcl));
				level.levelDown();
			} else if (now == ')') {
				throw new MathException("Parentheses are not paired: Excess right brackets", expr, pos);
			} else
				chars.putChar(now, pos);
		}
		return null;
	}

	private static StructEntry subParseStatement(CharArrayString str, LevelInfo info, SMCL smcl) {
		return null;
	}
}
