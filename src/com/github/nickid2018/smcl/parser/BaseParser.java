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
                level.position++;
				entryQueue.offer(subParseStatement(cexpr, level, smcl));
				level.levelDown();
			} else if (now == ')') {
				throw new MathException("Parentheses are not paired: Excess right brackets", expr, pos);
			} else
				chars.putChar(now, pos);
		}
		return AbstractParser.nextStatement(cexpr, chars, level, entryQueue, 0, cexpr.string.length);
	}

	private static StructEntry subParseStatement(CharArrayString str, LevelInfo level, SMCL smcl) throws MathException {
        CharsInfo chars = new CharsInfo(smcl);
        Queue<StructEntry> entryQueue = new LinkedList<>();
        int pos;
        int posstart;
        for (posstart = level.position; (pos = level.position) < str.string.length; level.position++) {
            char now = str.string[pos];
            if (now == '(') {
                level.levelUp();
                level.position++;
                entryQueue.offer(subParseStatement(str, level, smcl));
                level.levelDown();
            } else if (now == ')') {
                if (posstart == pos)
                    throw new MathException("Empty Statement", new String(str.string), level.position);
                StructEntry entry = new StructEntry();
                entry.startIndex = posstart;
                entry.endIndex = pos;
                entry.formatted = AbstractParser.nextStatement(str, chars, level, entryQueue, posstart, pos);
                entry.markRead(str, smcl);
                level.position++;
                return entry;
            } else
                chars.putChar(now, pos);
		}
		throw new MathException("Parentheses are not paired: Missing right brackets", new String(str.string), level.position);
	}
}
