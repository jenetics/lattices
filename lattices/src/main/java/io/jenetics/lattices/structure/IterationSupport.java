package io.jenetics.lattices.structure;

import static java.lang.System.arraycopy;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Low-level functionality for index iteration.
 */
public final class IterationSupport {
    private IterationSupport() {
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
        return forward(range, Precedence.regular(range.dimensionality()));
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
        return backward(range, Precedence.regular(range.dimensionality()));
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

        IndexIterator(int[] start, int[] end, int[] order, int[] cursor) {
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
        Forward(int[] start, int[] end, int[] order, int[] cursor) {
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
        Backward(int[] start, int[] end, int[] order, int[] cursor) {
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
