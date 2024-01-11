/*
 * Java Lattice Library (@__identifier__@).
 * Copyright (c) @__year__@ Franz Wilhelmstötter
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
 *
 * Author:
 *    Franz Wilhelmstötter (franz.wilhelmstoetter@gmail.com)
 */
package io.jenetics.lattices.structure;

import java.util.Arrays;
import java.util.Comparator;

/**
 * This class allows defining a total order on {@code int[]} arrays of a given
 * {@link #length()}.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public final class Precedence implements Comparator<int[]> {
    private final int[] order;

    private Precedence(int[] order) {
        this.order = order;
    }

    public int at(final int index) {
        return order[index];
    }

    public int length() {
        return order.length;
    }

    public int[] order() {
        return order.clone();
    }

    @Override
    public int compare(int[] a, int[] b) {
        if (a.length != length() || b.length != length()) {
            throw new IllegalArgumentException();
        }

        for (int i = length(); --i >= 0; ) {
            final int cmp = Integer.compare(a[at(i)], b[at(i)]);
            if (cmp != 0) {
                return cmp;
            }
        }

        return 0;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(order);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Precedence precedence && Arrays.equals(order, precedence.order);
    }

    @Override
    public String toString() {
        return Arrays.toString(order);
    }

    public static Precedence of(int... order) {
        if (order.length == 0) {
            throw new IllegalArgumentException("Precedence array must not be empty.");
        }
        if (!isPermutation(order)) {
            throw new IllegalArgumentException(
                "Precedence must be a permutation: " +
                    Arrays.toString(order)
            );
        }

        return new Precedence(order.clone());
    }

    private static boolean isPermutation(int[] values) {
        final var array = values.clone();
        Arrays.sort(array);

        for (int i = 0; i < array.length; ++i) {
            if (array[i] != i) {
                return false;
            }
        }

        return true;
    }

    public static Precedence regular(int length) {
        final var order = new int[length];
        for (int i = 0; i < length; ++i) {
            order[i] = i;
        }

        return new Precedence(order);
    }

    public static Precedence reverse(int length) {
        final var order = new int[length];
        for (int i = 0; i < length; ++i) {
            order[i] = length - i - 1;
        }

        return new Precedence(order);
    }

}
