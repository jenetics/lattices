package io.jenetics.lattices.structure;

import static java.lang.System.arraycopy;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Index iterator which iterates n-dimensional index in lexicographical order.
 * The iteration could be performed forward or backward. Additionally, the
 * index precedence, which defines the order of the indexes, can be defined
 * with the Order interface.
 */
sealed abstract class IndexCursor implements Iterator<int[]> {

    /**
     * Defines the order of the indexes in which they are incremented or
     * decremented first and which one later. The order must be a permutation of [0, n). It's a readonly
     * view an int[] array.
     */
    interface Order extends Comparator<int[]> {
        int at(int index);
        int length();

        static Order lowMajor(int length) {
            return new Order() {
                @Override
                public int at(int index) {
                    return length - index - 1;
                }
                @Override
                public int length() {
                    return length;
                }
            };
        }

        static Order highMajor(int length) {
            return new Order() {
                @Override
                public int at(int index) {
                    return index;
                }
                @Override
                public int length() {
                    return length;
                }
            };
        }

        @Override
        default int compare(int[] a, int[] b) {
            if (a.length != length() || b.length != length()) {
                throw new IllegalArgumentException();
            }

            for (int i = length(); --i >= 0;) {
                final int cmp = Integer.compare(a[at(i)], b[at(i)]);
                if (cmp != 0) {
                    return cmp;
                }
            }

            return 0;
        }
    }

    final int[] start;
    final int[] end;
    final int[] order;
    final int[] cursor;

    final int[] next;

    IndexCursor(int[] start, int[] end, int[] order, int[] cursor) {
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

    final static class Forward extends IndexCursor {
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

        static Forward of(Range range) {
            final var order = new int[range.dimensionality()];
            for (int i = 0; i < order.length; ++i) {
                order[order.length - i - 1] = i;
            }

            return new Forward(
                range.start().toArray(),
                range.end().toArray(),
                order,
                range.start().toArray()
            );
        }
    }

    final static class Backward extends IndexCursor {
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

        static Backward of(Range range) {
            final var order = new int[range.dimensionality()];
            for (int i = 0; i < order.length; ++i) {
                order[order.length - i - 1] = i;
            }

            return new Backward(
                range.start().toArray(),
                dec(range.end().toArray()),
                order,
                dec(range.end().toArray())
            );
        }
    }

    private static int[] dec(int[] array) {
        for (int i = 0; i < array.length; ++i) {
            --array[i];
        }
        return array;
    }

}
