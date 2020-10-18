package com.github.nickid2018.smcl.util;

import java.util.*;
import java.util.function.*;

public class CollectionUtils {

	public static <T> int binarySearch(List<T> list, ToIntFunction<T> equal) {
		int low = 0;
		int high = list.size() - 1;
		ListIterator<T> i = list.listIterator();

		while (low <= high) {
			int mid = (low + high) >>> 1;
			T midVal = get(i, mid);
			int cmp = equal.applyAsInt(midVal);

			if (cmp < 0)
				low = mid + 1;
			else if (cmp > 0)
				high = mid - 1;
			else
				return mid; // key found
		}
		return -(low + 1); // key not found
	}

	private static <T> T get(ListIterator<T> i, int index) {
		T obj = null;
		int pos = i.nextIndex();
		if (pos <= index) {
			do {
				obj = i.next();
			} while (pos++ < index);
		} else {
			do {
				obj = i.previous();
			} while (--pos > index);
		}
		return obj;
	}

	public static <T> T getOrNull(T[] array, int index) {
		return index < array.length ? array[index] : null;
	}
}
