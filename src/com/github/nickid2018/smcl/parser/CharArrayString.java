package com.github.nickid2018.smcl.parser;

import java.util.*;

public class CharArrayString {

	public char[] string;

	public CharArrayString(String str) {
		string = str.toCharArray();
	}

	public CharArrayString fillWithChar(int start, int end, char ch) {
		Arrays.fill(string, start, end, ch);
		return this;
	}

    public String substring(int start, int end) {
        return new String(string, start, end - start);
    }
}
