package com.github.nickid2018.smcl.optimize;

public class ShiftKey {

	public int start;
	public int end;
	public int level;

	@Override
	public int hashCode() {
		return start * 1000_000 + end * 1000 + level;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ShiftKey))
			return false;
		ShiftKey key = (ShiftKey) obj;
		return key.start == start && key.end == end && key.level == level;
	}

	@Override
	public String toString() {
		return "From: " + start + " To: " + end + " at Level " + level;
	}
}
