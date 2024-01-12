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

import static java.lang.System.arraycopy;
import static java.util.Objects.requireNonNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

/**
 * Low-level functionality for index iteration.
 * {@snippet lang=java:
 * final var range = Range.of(Index.of(1, 2, 3), Extent.of(2, 2, 2));
 * final var cursor = IndexCursor.forward(
 *     range,
 *     Precedence.natural(range.dimensionality())
 * );
 * final var index = new int[cursor.dimensionality()];
 *
 * while (cursor.next(index)) {
 *     System.out.println(Arrays.toString(index));
 * }
 *
 * // Produced output.
 * // > [1, 2, 3]
 * // > [2, 2, 3]
 * // > [1, 3, 3]
 * // > [2, 3, 3]
 * // > [1, 2, 4]
 * // > [2, 2, 4]
 * // > [1, 3, 4]
 * // > [2, 3, 4]
 * }
 * The example above shows how to do a <em>forward</em> iteration if an
 * {@code int[]} array in <em>natural</em> order ({@link Precedence}).
 *
 * @see Precedence
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public abstract class IndexCursor implements Dimensional {
    final int[] start;
    final int[] end;
    final int[] order;
    final int[] cursor;

    private IndexCursor(int[] start, int[] end, int[] order, int[] cursor) {
        if (start.length != end.length ||
            start.length != order.length ||
            start.length != cursor.length ||
            start.length == 0)
        {
            throw new IllegalArgumentException();
        }

        this.start = start;
        this.end = end;
        this.order = order;
        this.cursor = cursor;
    }

    @Override
    public int dimensionality() {
        return start.length;
    }

    /**
     * Writes the current value to the given {@code index} array and moves the
     * cursor forward.
     * {@snippet lang=java:
     * final var range = Range.of(Index.of(1, 2, 3), Extent.of(2, 2, 2));
     * final var cursor = IndexCursor.forward(
     *     range,
     *     Precedence.natural(range.dimensionality())
     * );
     * final var index = new int[cursor.dimensionality()];
     *
     * while (cursor.next(index)) {
     *     System.out.println(Arrays.toString(index));
     * }
     * }
     * The example above shows how to use the cursor for index iteration.
     *
     * @param index the {@code int[]} array where the index values are written
     *        to
     * @return {@code true} if an index value has been written to the output
     *         {@code index} array
     * @throws NullPointerException if the given parameter is {@code null}
     * @throws IndexOutOfBoundsException if {@code index.length < dimensionality()}
     */
    public abstract boolean next(int[] index);

    /**
     * Create a new index-cursor for forward iteration.
     *
     * @param range the index iteration range
     * @param precedence the index precedence used for the cursor iteration
     * @return a new index-cursor for forward iteration
     * @throws NullPointerException if one of the parameters is {@code null}
     */
    public static IndexCursor forward(Range range, Precedence precedence) {
        return new Forward(
            range.start().toArray(),
            range.end().toArray(),
            precedence.order(),
            range.start().toArray()
        );
    }

    /**
     * Create a new index-cursor for backward iteration.
     *
     * @param range the index iteration range
     * @param precedence the index precedence used for the cursor iteration
     * @return a new index-cursor for backward iteration
     * @throws NullPointerException if one of the parameters is {@code null}
     */
    public static IndexCursor backward(Range range, Precedence precedence) {
        return new Backward(
            range.start().toArray(),
            dec(range.end().toArray()),
            precedence.order(),
            dec(range.end().toArray())
        );
    }

    /**
     * Return an index {@link Iterable} from the given {@code cursor} (supplier).
     * {@snippet lang=java:
     * final var indexes = IndexCursor.iterable(() ->
     *     IndexCursor.forward(
     *         range,
     *         Precedence.natural(range.dimensionality())
     *     )
     * );
     * for (int[] index : indexes) {
     *     System.out.println(Arrays.toString(index));
     * }
     * }
     *
     * @param cursor the cursor (supplier) to create the iterable from
     * @return a new iterable from the given {@code cursor}
     * @throws NullPointerException if the given parameter is {@code null}
     */
    public static Iterable<int[]> iterable(Supplier<IndexCursor> cursor) {
        requireNonNull(cursor);
        return () -> new IndexIterator(cursor.get());
    }

    /* *************************************************************************
     * Cursor implementations
     * ************************************************************************/

    private static final class Forward extends IndexCursor {
        private Forward(int[] start, int[] end, int[] order, int[] cursor) {
            super(start, end, order, cursor);
        }

        @Override
        public boolean next(int[] index) {
            final boolean hasNext =
                cursor[order[order.length - 1]] <
                end[order[order.length - 1]];

            if (hasNext) {
                arraycopy(cursor, 0, index, 0, cursor.length);

                for (int k = 0; k < order.length; ++k) {
                    final int i = order[k];
                    ++cursor[i];

                    if (cursor[i] >= end[i] && k < start.length - 1) {
                        for (int j = 0; j < k + 1; ++j) {
                            cursor[order[j]] = start[order[j]];
                        }
                    } else {
                        break;
                    }
                }
            }

            return hasNext;
        }
    }

    private static final class Backward extends IndexCursor {
        private Backward(int[] start, int[] end, int[] order, int[] cursor) {
            super(start, end, order, cursor);
        }

        @Override
        public boolean next(int[] index) {
            final boolean hasNext =
                cursor[order[order.length - 1]] >=
                start[order[order.length - 1]];

            if (hasNext) {
                arraycopy(cursor, 0, index, 0, cursor.length);

                for (int k = 0; k < order.length; ++k) {
                    final int i = order[k];
                    --cursor[i];

                    if (cursor[i] < start[i] && k < start.length - 1) {
                        for (int j = 0; j < k + 1; ++j) {
                            cursor[order[j]] = end[order[j]];
                        }
                    } else {
                        break;
                    }
                }
            }

            return hasNext;
        }
    }

    private static int[] dec(int[] array) {
        for (int i = 0; i < array.length; ++i) {
            --array[i];
        }
        return array;
    }

    private static final class IndexIterator implements Iterator<int[]> {
        private final IndexCursor cursor;
        private final int[] index;

        private boolean hasNext = false;
        private boolean needNext = true;

        private IndexIterator(IndexCursor cursor) {
            this.cursor = cursor;
            this.index = new int[cursor.dimensionality()];
        }

        @Override
        public boolean hasNext() {
            if (needNext) {
                hasNext = cursor.next(index);
                needNext = false;
            }
            return hasNext;
        }

        @Override
        public int[] next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            needNext = true;
            return index.clone();
        }
    }

}
