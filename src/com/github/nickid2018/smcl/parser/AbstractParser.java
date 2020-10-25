package com.github.nickid2018.smcl.parser;

import java.util.*;
import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.optimize.*;

public abstract class AbstractParser<T extends Statement> {

	public abstract T parseStatement(CharArrayString expr, CharsInfo chars, LevelInfo level,
                                     Queue<StructEntry> structs, int from, int end);

    public static int nextOperator(char ch, CharsInfo chars) {
        CharInfo info = chars.charQueue.peek();
        if (info.ch != ch)
            return -1;
        chars.charQueue.poll();
        return info.position;
    }

	public static Statement nextStatement(CharArrayString str, CharsInfo info, LevelInfo level,
                                          Queue<StructEntry> structs, int start, int end) throws MathException {
		CharsInfo sub = info.subInfo(start, end);
		if (!sub.charQueue.isEmpty()) {
			return info.smcl.register.getParser(sub.charQueue.poll().ch).parseStatement(str, sub, level, structs, start, end);
		}
        if (str.string[start] == info.smcl.settings.overrideCharacter) {
            return structs.poll().formatted;
		}
        //Either a number or a variable
        String text = str.substring(start, end);
        if (info.smcl.globalvars.haveVariable(text))
            return info.smcl.globalvars.getVariable(text);
        try {
            return NumberPool.get(info.smcl, Double.parseDouble(text));
        } catch (NumberFormatException e) {
            throw new MathException("Cannot parse the string", new String(str.string), start);
        }
	}
}
