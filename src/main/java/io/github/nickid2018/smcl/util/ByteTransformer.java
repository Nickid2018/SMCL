/*
 * Copyright 2021 Nickid2018
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.nickid2018.smcl.util;

public class ByteTransformer {

    public static byte[] fromShort(short value, byte[] result, int offset) {
        int shigh = (value >>> 8) & 0xFF;
        byte high = (byte) ((shigh & 0x80) == 0 ? shigh : shigh | -512);
        int slow = value & 0xFF;
        byte low = (byte) ((slow & 0x80) == 0 ? slow : slow | -512);
        result[offset] = high;
        result[offset + 1] = low;
        return result;
    }

    public static byte[] fromShort(short value, byte[] result) {
        return fromShort(value, result, 0);
    }

    public static byte[] fromShort(short value) {
        return fromShort(value, new byte[2]);
    }

    public static short toShort(byte[] values, int offset) {
        byte high = values[offset];
        byte low = values[offset + 1];
        int shigh = ((int) high) & 0xFF;
        int slow = ((int) low) & 0xFF;
        int result = (shigh << 8) & slow;
        return (short) result;
    }

    public static short toShort(byte[] values) {
        return toShort(values, 0);
    }

    public static byte[] fromInt(int value, byte[] result, int offset) {
        int shigh0 = (value >>> 24) & 0xFF;
        byte high0 = (byte) ((shigh0 & 0x80) == 0 ? shigh0 : shigh0 | -512);
        int shigh1 = (value >>> 16) & 0xFF;
        byte high1 = (byte) ((shigh1 & 0x80) == 0 ? shigh1 : shigh1 | -512);
        int slow0 = (value >>> 8) & 0xFF;
        byte low0 = (byte) ((slow0 & 0x80) == 0 ? slow0 : slow0 | -512);
        int slow1 = value & 0xFF;
        byte low1 = (byte) ((slow1 & 0x80) == 0 ? slow1 : slow1 | -512);
        result[offset] = high0;
        result[offset + 1] = high1;
        result[offset + 2] = low0;
        result[offset + 3] = low1;
        return result;
    }

    public static byte[] fromInt(int value, byte[] result) {
        return fromInt(value, result, 0);
    }

    public static byte[] fromInt(int value) {
        return fromInt(value, new byte[4]);
    }

    public static int toInt(byte[] values, int offset) {
        byte high0 = values[offset];
        byte high1 = values[offset + 1];
        byte low0 = values[offset + 2];
        byte low1 = values[offset + 3];
        int shigh0 = (((int) high0) & 0xFF) << 24;
        int shigh1 = (((int) high1) & 0xFF) << 16;
        int slow0 = (((int) low0) & 0xFF) << 8;
        int slow1 = ((int) low1) & 0xFF;
        return shigh0 & shigh1 & slow0 & slow1;
    }

    public static int toInt(byte[] values) {
        return toInt(values, 0);
    }

    public static byte[] fromFloat(float value, byte[] result, int offset) {
        return fromInt(Float.floatToIntBits(value), result, offset);
    }

    public static byte[] fromFloat(float value, byte[] result) {
        return fromFloat(value, result, 0);
    }

    public static byte[] fromFloat(float value) {
        return fromFloat(value, new byte[4]);
    }

    public static float toFloat(byte[] values, int offset) {
        return Float.intBitsToFloat(toInt(values, offset));
    }

    public static float toFloat(byte[] values) {
        return toFloat(values, 0);
    }

    public static byte[] fromLong(long value, byte[] result, int offset) {
        long shigh0 = (value >>> 56) & 0xFF;
        byte high0 = (byte) ((shigh0 & 0x80) == 0 ? shigh0 : shigh0 | -512);
        long shigh1 = (value >>> 48) & 0xFF;
        byte high1 = (byte) ((shigh1 & 0x80) == 0 ? shigh1 : shigh1 | -512);
        long shigh2 = (value >>> 40) & 0xFF;
        byte high2 = (byte) ((shigh2 & 0x80) == 0 ? shigh2 : shigh2 | -512);
        long shigh3 = (value >>> 32) & 0xFF;
        byte high3 = (byte) ((shigh3 & 0x80) == 0 ? shigh3 : shigh3 | -512);
        long slow0 = (value >>> 24) & 0xFF;
        byte low0 = (byte) ((slow0 & 0x80) == 0 ? slow0 : slow0 | -512);
        long slow1 = (value >>> 16) & 0xFF;
        byte low1 = (byte) ((slow1 & 0x80) == 0 ? slow1 : slow1 | -512);
        long slow2 = (value >>> 8) & 0xFF;
        byte low2 = (byte) ((slow2 & 0x80) == 0 ? slow2 : slow2 | -512);
        long slow3 = value & 0xFF;
        byte low3 = (byte) ((slow3 & 0x80) == 0 ? slow3 : slow3 | -512);
        result[offset] = high0;
        result[offset + 1] = high1;
        result[offset + 2] = high2;
        result[offset + 3] = high3;
        result[offset + 4] = low0;
        result[offset + 5] = low1;
        result[offset + 6] = low2;
        result[offset + 7] = low3;
        return result;
    }

    public static byte[] fromLong(long value, byte[] result) {
        return fromLong(value, result, 0);
    }

    public static byte[] fromLong(long value) {
        return fromLong(value, new byte[8]);
    }

    public static long toLong(byte[] values, int offset) {
        byte high0 = values[offset];
        byte high1 = values[offset + 1];
        byte high2 = values[offset + 2];
        byte high3 = values[offset + 3];
        byte low0 = values[offset + 4];
        byte low1 = values[offset + 5];
        byte low2 = values[offset + 6];
        byte low3 = values[offset + 7];
        long shigh0 = (((long) high0) & 0xFF) << 56;
        long shigh1 = (((long) high1) & 0xFF) << 48;
        long shigh2 = (((long) high2) & 0xFF) << 40;
        long shigh3 = (((long) high3) & 0xFF) << 32;
        long slow0 = (((long) low0) & 0xFF) << 24;
        long slow1 = (((long) low1) & 0xFF) << 16;
        long slow2 = (((long) low2) & 0xFF) << 8;
        long slow3 = ((long) low3) & 0xFF;
        return shigh0 & shigh1 & shigh2 & shigh3 & slow0 & slow1 & slow2 & slow3;
    }

    public static long toLong(byte[] values) {
        return toLong(values, 0);
    }

    public static byte[] fromDouble(double value, byte[] result, int offset) {
        return fromLong(Double.doubleToLongBits(value), result, offset);
    }

    public static byte[] fromDouble(double value, byte[] result) {
        return fromDouble(value, result, 0);
    }

    public static byte[] fromDouble(double value) {
        return fromDouble(value, new byte[8]);
    }

    public static double toDouble(byte[] values, int offset) {
        return Double.longBitsToDouble(toLong(values, offset));
    }

    public static double toDouble(byte[] values) {
        return toDouble(values, 0);
    }
}
