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
package com.github.nickid2018.smcl.optimize;

import java.util.Arrays;
import java.util.function.IntConsumer;

public class IntArray {

    public static final float DEFAULT_INCREASE_FACTOR = 1f;
    public static final int DEFAULT_INIT_SIZE = 4;
    private final float factor;
    private int[] array;
    private int length;

    public IntArray() {
        this(DEFAULT_INIT_SIZE, DEFAULT_INCREASE_FACTOR);
    }

    public IntArray(int size, float factor) {
        array = new int[size];
        this.factor = factor;
        length = 0;
    }

    private void ensurePosition() {
        if (length == array.length) {
            int[] tmp = array;
            array = new int[(int) (length * (factor + 1))];
            System.arraycopy(tmp, 0, array, 0, length);
        }
    }

    private void testPosition(int pos) {
        if (pos >= length)
            throw new ArrayIndexOutOfBoundsException(pos);
    }

    public void putInt(int value) {
        ensurePosition();
        array[length++] = value;
    }

    public int setInt(int pos, int value) {
        testPosition(pos);
        int source = array[pos];
        array[pos] = value;
        return source;
    }

    public int deleteAndShift(int pos) {
        testPosition(pos);
        int value = array[pos];
        System.arraycopy(array, pos + 1, array, pos, length - pos - 1);
        array[--length] = 0;
        return value;
    }

    public void deleteRangeAndShift(int start, int len) {
        testPosition(start);
        testPosition(start + len);
        System.arraycopy(array, start + len, array, start, length - start - len);
        Arrays.fill(array, length - start - len, length - len, 0);
        length -= len;
    }

    /**
     * @deprecated This function may cause sorting error. Please use
     * {@linkplain #deleteAndShift(int)}.
     */
    @Deprecated
    public int delete(int pos) {
        testPosition(pos);
        int value = array[pos];
        array[pos] = 0;
        return value;
    }

    public int deleteLast() {
        testPosition(0);
        return array[--length];
    }

    public int get(int pos) {
        testPosition(pos);
        return array[pos];
    }

    public int length() {
        return length;
    }

    public void forEach(IntConsumer consumer) {
        for (int i = 0; i < length; i++)
            consumer.accept(i);
    }

    public void sort() {
        Arrays.sort(array, 0, length);
    }

    /**
     * This function must be used after sorting!
     */
    public int binarySearch(int key) {
        return Arrays.binarySearch(array, 0, length, key);
    }

    public void copyInArray(int[] copy) {
        System.arraycopy(array, 0, copy, 0, length);
    }

    public void insertInt(int pos, int value) {
        ensurePosition();
        testPosition(pos);
        System.arraycopy(array, pos, array, pos + 1, length - pos);
        array[pos] = value;
        length++;
    }

    public void clear() {
        Arrays.fill(array, 0);
        length = 0;
    }

    private void ensureLength(int lenLimit) {
        if (length > lenLimit) {
            int len = array.length;
            int[] tmp = array;
            while (len < lenLimit) {
                len = (int) (len * factor);
            }
            array = new int[len];
            System.arraycopy(tmp, 0, array, 0, tmp.length);
        }
    }

    public void copy(IntArray other, int dpos) {
        copy(other, 0, dpos, other.length);
    }

    public void copy(IntArray other, int spos, int dpos) {
        copy(other, spos, dpos, other.length - spos);
    }

    public void copy(IntArray other, int spos, int dpos, int lenCopy) {
        ensureLength(dpos + lenCopy);
        length = Math.max(length, dpos + lenCopy);
        System.arraycopy(other.array, spos, array, dpos, lenCopy);
    }

    public void copyArray(int[] other, int dpos) {
        copyArray(other, 0, dpos, other.length);
    }

    public void copyArray(int[] other, int spos, int dpos) {
        copyArray(other, spos, dpos, other.length - spos);
    }

    public void copyArray(int[] other, int spos, int dpos, int lenCopy) {
        ensureLength(dpos + lenCopy);
        length = Math.max(length, dpos + lenCopy);
        System.arraycopy(other, spos, array, dpos, lenCopy);
    }

    public void append(IntArray other) {
        insert(other, 0, length);
    }

    public void append(IntArray other, int spos) {
        insert(other, spos, length);
    }

    public void appendArray(int[] other) {
        insertArray(other, 0, length);
    }

    public void appendArray(int[] other, int spos) {
        insertArray(other, spos, length);
    }

    public void insert(IntArray other, int dpos) {
        insert(other, 0, dpos, other.length);
    }

    public void insert(IntArray other, int spos, int dpos) {
        insert(other, spos, dpos, other.length - spos);
    }

    public void insert(IntArray other, int spos, int dpos, int lenIns) {
        ensureLength(length + lenIns);
        length += lenIns;
        System.arraycopy(array, dpos, array, dpos + lenIns, length - dpos);
        System.arraycopy(other.array, spos, array, dpos, lenIns);
    }

    public void insertArray(int[] other, int dpos) {
        insertArray(other, 0, dpos, other.length);
    }

    public void insertArray(int[] other, int spos, int dpos) {
        insertArray(other, spos, dpos, other.length - spos);
    }

    public void insertArray(int[] other, int spos, int dpos, int lenIns) {
        ensureLength(length + lenIns);
        length += lenIns;
        System.arraycopy(array, dpos, array, dpos + lenIns, length - dpos);
        System.arraycopy(other, spos, array, dpos, lenIns);
    }
}
