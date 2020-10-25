package com.github.nickid2018.smcl.parser;

import com.github.nickid2018.smcl.*;

public class StructEntry {

	public int startIndex;
	public int endIndex;

	public Statement formatted;

	public CharArrayString markRead(CharArrayString now, SMCL smcl) {
		now.string[startIndex] = smcl.settings.overrideCharacter;
        return now;
	}
}
