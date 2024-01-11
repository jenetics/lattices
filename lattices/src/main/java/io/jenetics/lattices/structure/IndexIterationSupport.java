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

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Low-level functionality for index iteration.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public final class IndexIterationSupport {
    private IndexIterationSupport() {
    }

    public static Iterable<int[]> forward(Range range, Precedence precedence) {
        return () -> new Forward(
            range.start().toArray(),
            range.end().toArray(),
            precedence.order(),
            range.start().toArray()
        );
    }

    public static Iterable<int[]> forward(Range range) {
        return forward(range, Precedence.natural(range.dimensionality()));
    }

    public static Iterable<int[]> backward(Range range, Precedence precedence) {
        return () -> new Backward(
            range.start().toArray(),
            dec(range.end().toArray()),
            precedence.order(),
            dec(range.end().toArray())
        );
    }

    public static Iterable<int[]> backward(Range range) {
        return backward(range, Precedence.natural(range.dimensionality()));
    }


    /* *************************************************************************
     * Iterator helper classes.
     * ************************************************************************/

    private static abstract class IndexIterator implements Iterator<int[]> {
        final int[] start;
        final int[] end;
        final int[] order;
        final int[] cursor;

        final int[] next;

        private IndexIterator(int[] start, int[] end, int[] order, int[] cursor) {
            if (start.length != end.length ||
                start.length != order.length ||
                start.length != cursor.length)
            {
                throw new IllegalArgumentException();
            }

            this.start = start;
            this.end = end;
            this.order = order;
            this.cursor = cursor;

            this.next = new int[start.length];
        }
    }

    private static final class Forward extends IndexIterator {
        private Forward(int[] start, int[] end, int[] order, int[] cursor) {
            super(start, end, order, cursor);
        }

        @Override
        public boolean hasNext() {
            return cursor[order[order.length - 1]] < end[order[order.length - 1]];
        }

        @Override
        public int[] next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            arraycopy(cursor, 0, next, 0, cursor.length);

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

            return next;
        }
    }

    private static final class Backward extends IndexIterator {
        private Backward(int[] start, int[] end, int[] order, int[] cursor) {
            super(start, end, order, cursor);
        }

        @Override
        public boolean hasNext() {
            return cursor[order[order.length - 1]] >= start[order[order.length - 1]];
        }

        @Override
        public int[] next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            arraycopy(cursor, 0, next, 0, cursor.length);

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

            return next;
        }
    }

    private static int[] dec(int[] array) {
        for (int i = 0; i < array.length; ++i) {
            --array[i];
        }
        return array;
    }

}
