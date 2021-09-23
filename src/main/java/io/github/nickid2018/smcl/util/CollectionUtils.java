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

import java.util.List;
import java.util.ListIterator;
import java.util.function.ToIntFunction;

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
        T obj;
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
