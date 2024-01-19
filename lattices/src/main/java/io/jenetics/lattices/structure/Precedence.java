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
 * This class defines a total order for {@code int[]} arrays. {@code int[]}
 * arrays have no natural order, like scalars. You can define such an order by
 * combining the order of its components, starting comparing the array component
 * with the highest <em>precedence</em>. If these two values are equal, the
 * comparison proceeds with the array index with the second-highest precedence.
 * <pre>{@code
 *   0   1   2            0   1   2
 * +---+---+---+        +---+---+---+
 * | 5 | 1 | 4 |    <   | 3 | 2 | 4 |
 * +---+---+---+        +---+---+---+
 * }</pre>
 * The example compares two arrays of length 3, where the elements with index 2
 * have the highest precedence and elements with index 0 the lowest precedence.
 * With this assumption, the first array is <em>smaller</em> than the second one.
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

    /**
     * Return the array length {@code this} precedence object can compare.
     *
     * @return the array length {@code this} precedence object can compare
     */
    public int length() {
        return order.length;
    }

    /**
     * Return the precedence order array.
     *
     * @return the precedence order array
     */
    public int[] order() {
        return order.clone();
    }

    int[] sort(int... values) {
        if (values.length != order.length) {
            throw new IllegalArgumentException();
        }

        final var sorted = new int[values.length];
        for (int i = 0; i < order.length; ++i) {
            sorted[i] = values[order[i]];
        }
        return sorted;
    }

    @Override
    public int compare(int[] a, int[] b) {
        if (a.length != length() || b.length != length()) {
            throw new IllegalArgumentException(
                "The given arrays must have a length of %d, but got %d and %d"
                    .formatted(length(), a.length, b.length)
            );
        }

        for (int i = length(); --i >= 0; ) {
            final int cmp = Integer.compare(a[order[i]], b[order[i]]);
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

    /**
     * Create a new precedence object with the given {@code order}. The first
     * element of the given {@code order} array defines the index with the
     * lowest precedence and the last element of the {@code order} array defines
     * the index with the highest precedence.
     * {@snippet lang=java:
     * final var precedence = Precedence.of(1, 2, 0);
     * final var a = new int[] {3, 7, 4};
     * final var b = new int[] {3, 2, 5};
     * assert precedence.compare(a, b) < 0;
     * }
     * The example above creates a new precedence object, where the array elements
     * with index 0 have the highest precedence and elements with index 1 the
     * lowest.
     *
     * @param order the precedence order array
     * @return a new precedence object with the given precedence order
     * @throws IllegalArgumentException if the given {@code order} array is empty
     *         or the array elements are not a permutation of the elements of
     *         {@code [0,..,n)}
     */
    public static Precedence of(int... order) {
        if (order.length == 0) {
            throw new IllegalArgumentException(
                "Precedence array must not be empty."
            );
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

    /**
     * Create a new precedence object for arrays with the given {@code length}.
     * <em>Natural</em> precedence means that array elements with index 0 have
     * the lowest precedence and array elements with index {@code length-1}
     * have the highest precedence.
     * {@snippet lang=java:
     * final var p1 = Precedence.natural(3);
     * final var p2 = Precedence.of(0, 1, 2);
     * assert p1.equals(p2);
     * }
     *
     * @see #reverse(int)
     *
     * @param length the length of the arrays which should be compared
     * @return a new precedence object for arrays with the given {@code length}
     */
    public static Precedence natural(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException(
                "Length must be greater than 0: " + length
            );
        }

        final var order = new int[length];
        for (int i = 0; i < length; ++i) {
            order[i] = i;
        }

        return new Precedence(order);
    }

    /**
     * Create a new precedence object for arrays with the given {@code length}.
     * <em>Reverse</em> precedence means that array elements with index 0 have
     * the highest precedence and array elements with index {@code length-1}
     * have the lowest precedence.
     * {@snippet lang=java:
     * final var p1 = Precedence.reverse(3);
     * final var p2 = Precedence.of(2, 1, 0);
     * assert p1.equals(p2);
     * }
     *
     * @see #natural(int)
     *
     * @param length the length of the arrays which should be compared
     * @return a new precedence object for arrays with the given {@code length}
     */
    public static Precedence reverse(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException(
                "Length must be greater than 0: " + length
            );
        }

        final var order = new int[length];
        for (int i = 0; i < length; ++i) {
            order[i] = length - i - 1;
        }

        return new Precedence(order);
    }

}
