package com.github.nickid2018.smcl.parser;

import java.util.*;
import com.github.nickid2018.smcl.*;

public abstract class AbstractParser<T extends Statement> {

	public abstract T parseStatement(CharArrayString expr, CharsInfo chars, LevelInfo level,
			Queue<StructEntry> structs);

	public static Statement nextStatement(CharArrayString str, int start, int end, CharsInfo info,
			Queue<StructEntry> structs, LevelInfo level) {
		if (str.string[start] == info.smcl.settings.overrideCharacter) {
			return structs.poll().formatted;
		}
		CharsInfo sub = info.subInfo(start, end);
		if (!sub.charQueue.isEmpty()) {
			return info.smcl.register.getParser(sub.charQueue.poll().ch).parseStatement(str, sub, level, structs);
		}
		return null;
	}
}
