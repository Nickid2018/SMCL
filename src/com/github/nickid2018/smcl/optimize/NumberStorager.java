package com.github.nickid2018.smcl.optimize;

public class NumberStorager {

	private IntArray array;

	public NumberStorager() {
		array = new IntArray();
	}

	public void putInt(int integer) {
		array.putInt(integer);
	}

	public void putFloat(float flo) {
		putInt(Float.floatToIntBits(flo));
	}

	public void putLong(long integer) {
		long up = integer >>> 32;
		long down = integer & 0xFFFFFFFFL;
		array.putInt((int)((up & 0x80000000L) == 0L ?up : up | 0xFFFFFFFF80000000L));
		array.putInt((int)((down & 0x80000000L) == 0L ?down: down | 0xFFFFFFFF80000000L));
	}

	public void putDouble(double dou) {
		putLong(Double.doubleToLongBits(dou));
	}

	public int getInt() {
		return array.deleteAndShift(0);
	}

	public float getFloat() {
		return Float.intBitsToFloat(getInt());
	}

	public long getLong() {
		return (((long)getInt()) << 32) | (((long)getInt()) & 0xFFFFFFFFL);
	}

	public double getDouble() {
		return Double.longBitsToDouble(getLong());
	}

	public void clear() {
		array.clear();
	}
}
