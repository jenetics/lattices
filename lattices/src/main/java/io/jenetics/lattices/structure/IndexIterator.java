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

import static java.util.Objects.requireNonNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

/**
 * Index iterator class. It allows iterating indexes in different orders. The
 * following example shows how to do this.
 * {@snippet lang=java:
 * final var range = Range.of(Extent.of(2, 2, 2));
 *
 * final var it = new IndexIterator.LowMajorForward(range);
 * while (it.hasNext()) {
 *     System.out.println(Arrays.toString(it.next()));
 * }
 *
 * // Produced output.
 * // > [0, 0, 0]
 * // > [0, 0, 1]
 * // > [0, 1, 0]
 * // > [0, 1, 1]
 * // > [1, 0, 0]
 * // > [1, 0, 1]
 * // > [1, 1, 0]
 * // > [1, 1, 1]
 * }
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public sealed abstract class IndexIterator implements Iterator<int[]> {
    final int[] start;
    final int[] end;
    final int[] cursor;

    private IndexIterator(int[] start, int[] end, int[] cursor) {
        this.start = start;
        this.end = end;
        this.cursor = cursor;
    }

    /**
     * Forward iterator, where the lower indexes are increased after the higher
     * indexes.
     * {@snippet lang=java:
     * final var range = Range.of(Extent.of(2, 2, 2));
     *
     * final var it = new IndexIterator.LowMajorForward(range);
     * while (it.hasNext()) {
     *     System.out.println(Arrays.toString(it.next()));
     * }
     *
     * // Produced output.
     * // > [0, 0, 0]
     * // > [0, 0, 1]
     * // > [0, 1, 0]
     * // > [0, 1, 1]
     * // > [1, 0, 0]
     * // > [1, 0, 1]
     * // > [1, 1, 0]
     * // > [1, 1, 1]
     * }
     *
     * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
     * @since 3.0
     * @version 3.0
     */
    public static final class LowMajorForward extends IndexIterator {

        /**
         * Create a new index iterator for the given range.
         *
         * @param range the index range the iterator is iterating
         */
        public LowMajorForward(Range range) {
            super(
                range.start().toArray(),
                range.end().toArray(),
                range.start().toArray()
            );
        }

        @Override
        public boolean hasNext() {
            return cursor[0] < end[0];
        }

        @Override
        public int[] next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            final var next = cursor.clone();

            for (int i = start.length; --i >= 0;) {
                cursor[i] = next[i] + 1;

                if (cursor[i] >= end[i] && i > 0) {
                    for (int j = start.length; --j >= i;) {
                        cursor[j] = start[i];
                    }
                } else {
                    break;
                }
            }

            return next;
        }
    }

    /**
     * Backward iterator, where the lower indexes are decreased after the higher
     * indexes.
     * {@snippet lang=java:
     * final var range = Range.of(Extent.of(2, 2, 2));
     *
     * final var it = new IndexIterator.LowMajorBackward(range);
     * while (it.hasNext()) {
     *     System.out.println(Arrays.toString(it.next()));
     * }
     *
     * // Produced output.
     * // > [1, 1, 1]
     * // > [1, 1, 0]
     * // > [1, 0, 1]
     * // > [1, 0, 0]
     * // > [0, 1, 1]
     * // > [0, 1, 0]
     * // > [0, 0, 1]
     * // > [0, 0, 0]
     * }
     *
     * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
     * @since 3.0
     * @version 3.0
     */
    public static final class LowMajorBackward extends IndexIterator {

        /**
         * Create a new index iterator for the given range.
         *
         * @param range the index range the iterator is iterating
         */
        public LowMajorBackward(Range range) {
            super(
                range.start().toArray(),
                dec(range.end().toArray()),
                dec(range.end().toArray())
            );
        }

        @Override
        public boolean hasNext() {
            return cursor[0] >= start[0];
        }

        @Override
        public int[] next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            final var next = cursor.clone();

            for (int i = start.length; --i >= 0;) {
                cursor[i] = next[i] - 1;

                if (cursor[i] < start[i] && i > 0) {
                    for (int j = start.length; --j >= i;) {
                        cursor[j] = end[i];
                    }
                } else {
                    break;
                }
            }

            return next;
        }
    }

    /**
     * Forward iterator, where the higher indexes are increased after the lower
     * indexes.
     * {@snippet lang=java:
     * final var range = Range.of(Extent.of(2, 2, 2));
     *
     * final var it = new IndexIterator.HighMajorForward(range);
     * while (it.hasNext()) {
     *     System.out.println(Arrays.toString(it.next()));
     * }
     *
     * // Produced output.
     * // > [0, 0, 0]
     * // > [1, 0, 0]
     * // > [0, 1, 0]
     * // > [1, 1, 0]
     * // > [0, 0, 1]
     * // > [1, 0, 1]
     * // > [0, 1, 1]
     * // > [1, 1, 1]
     * }
     *
     * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
     * @since 3.0
     * @version 3.0
     */
    public static final class HighMajorForward extends IndexIterator {

        /**
         * Create a new index iterator for the given range.
         *
         * @param range the index range the iterator is iterating
         */
        public HighMajorForward(Range range) {
            super(
                range.start().toArray(),
                range.end().toArray(),
                range.start().toArray()
            );
        }

        @Override
        public boolean hasNext() {
            return cursor[start.length - 1] < end[start.length - 1];
        }

        @Override
        public int[] next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            final var next = cursor.clone();

            for (int i = 0; i < start.length; ++i) {
                cursor[i] = next[i] + 1;

                if (cursor[i] >= end[i] && i < start.length - 1) {
                    for (int j = 0; j <= i; ++j) {
                        cursor[j] = start[i];
                    }
                } else {
                    break;
                }
            }

            return next;
        }
    }

    /**
     * Backward iterator, where the higher indexes are decreased after the lower
     * indexes.
     * {@snippet lang=java:
     * final var range = Range.of(Extent.of(2, 2, 2));
     *
     * final var it = new IndexIterator.HighMajorBackward(range);
     * while (it.hasNext()) {
     *     System.out.println(Arrays.toString(it.next()));
     * }
     *
     * // Produced output.
     * // > [1, 1, 1]
     * // > [0, 1, 1]
     * // > [1, 0, 1]
     * // > [0, 0, 1]
     * // > [1, 1, 0]
     * // > [0, 1, 0]
     * // > [1, 0, 0]
     * // > [0, 0, 0]
     * }
     *
     * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
     * @since 3.0
     * @version 3.0
     */
    public static final class HighMajorBackward extends IndexIterator {

        /**
         * Create a new index iterator for the given range.
         *
         * @param range the index range the iterator is iterating
         */
        public HighMajorBackward(Range range) {
            super(
                range.start().toArray(),
                dec(range.end().toArray()),
                dec(range.end().toArray())
            );
        }

        @Override
        public boolean hasNext() {
            return cursor[start.length - 1] >= start[start.length - 1];
        }

        @Override
        public int[] next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            final var next = cursor.clone();

            for (int i = 0; i < start.length; ++i) {
                cursor[i] = next[i] - 1;

                if (cursor[i] < start[i] && i < start.length - 1) {
                    for (int j = 0; j <= i; ++j) {
                        cursor[j] = end[i];
                    }
                } else {
                    break;
                }
            }

            return next;
        }
    }

    private static int[] dec(int[] array) {
        for (int i = 0; i < array.length; ++i) {
            --array[i];
        }
        return array;
    }

    /**
     * Iterator, which maps an index array to an arbitrary object.
     *
     * @param <T> the mapped type
     */
    static final class MappedIterator<T> implements Iterator<T> {
        private final Iterator<int[]> iterator;
        private final Function<? super int[], ? extends T> mapper;

        MappedIterator(
            Iterator<int[]> iterator,
            Function<? super int[], ? extends T> mapper
        ) {
            this.iterator = requireNonNull(iterator);
            this.mapper = requireNonNull(mapper);
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public T next() {
            return mapper.apply(iterator.next());
        }
    }

}
