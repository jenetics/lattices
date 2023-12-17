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

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Index iterator interface. It allows iterating indexes in different orders. The
 * following example shows how to do this.
 * {@snippet lang=java:
 * final var range = Range.of(Extent.of(2, 2, 2));
 * final var it = IndexIterator.LowMajor.forward(range);
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
 * Implementations of this interface allow to iterate <em>forward</em> and
 * <em>backward</em> ond in <em>low-major</em> and <em>high-major</em> order.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public sealed interface IdxIterator extends Iterator<int[]> {

    /**
     * Forward iterators starts iterating with the start indexes (inclusively)
     * and increases the index values.
     *
     * <pre>
     * [0, 0, 0]
     * [0, 0, 1]
     * [0, 1, 0]
     * [0, 1, 1]
     * [1, 0, 0]
     * [1, 0, 1]
     * [1, 1, 0]
     * [1, 1, 1]
     * </pre>
     *
     * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
     * @since 3.0
     * @version 3.0
     */
    sealed interface Forward extends IdxIterator {
    }

    /**
     * Backward iterators starts iterating with the end indexes (exclusively)
     * and decreases the index values.
     *
     * <pre>
     * [1, 1, 1]
     * [1, 1, 0]
     * [1, 0, 1]
     * [1, 0, 0]
     * [0, 1, 1]
     * [0, 1, 0]
     * [0, 0, 1]
     * [0, 0, 0]
     * </pre>
     *
     * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
     * @since 3.0
     * @version 3.0
     */
    sealed interface Backward extends IdxIterator {
    }

    /**
     * Low-major iterators increases (or decreases) the higher indexes before
     * the lower indexes.
     *
     * <pre>
     * [0, 0, 0]
     * [0, 0, 1]
     * [0, 1, 0]
     * [0, 1, 1]
     * [1, 0, 0]
     * [1, 0, 1]
     * [1, 1, 0]
     * [1, 1, 1]
     * </pre>
     *
     * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
     * @since 3.0
     * @version 3.0
     */
    abstract sealed class LowMajor implements IdxIterator {

        record IndexState(int[] start, int[] end, int[] cursor) {
        }

        private LowMajor() {
        }

        private static final class LowMajorForward extends LowMajor implements Forward {
            private final IndexState state;

            private LowMajorForward(IndexState state) {
                this.state = state;
            }

            @Override
            public boolean hasNext() {
                return state.cursor[0] < state.end[0];
            }

            @Override
            public int[] next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                final var next = state.cursor.clone();

                for (int i = state.start.length; --i >= 0;) {
                    state.cursor[i] = next[i] + 1;

                    if (state.cursor[i] >= state.end[i] && i > 0) {
                        for (int j = state.start.length; --j >= i;) {
                            state.cursor[j] = state.start[i];
                        }
                    } else {
                        break;
                    }
                }

                return next;
            }
        }

        private static final class LowMajorBackward extends LowMajor implements Backward {
            private final IndexState state;

            private LowMajorBackward(IndexState state) {
                this.state = state;
            }

            @Override
            public boolean hasNext() {
                return state.cursor[0] >= state.start[0];
            }

            @Override
            public int[] next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                final var next = state.cursor.clone();

                for (int i = state.start.length; --i >= 0;) {
                    state.cursor[i] = next[i] - 1;

                    if (state.cursor[i] < state.start[i] && i > 0) {
                        for (int j = state.start.length; --j >= i;) {
                            state.cursor[j] = state.end[i];
                        }
                    } else {
                        break;
                    }
                }

                return next;
            }
        }

        /**
         * Create a new low-major forward iterator from the given {@code range}.
         *
         * @param range the iteration range
         * @return a new low-major forward iterator from the given {@code range}
         * @param <I> the iterator <em>union</em> type
         */
        @SuppressWarnings("unchecked")
        public static <I extends LowMajor & Forward> I forward(Range range) {
            return (I)new LowMajorForward(new IndexState(
                range.start().toArray(),
                range.end().toArray(),
                range.start().toArray()
            ));
        }

        /**
         * Create a new low-major backward iterator from the given {@code range}.
         *
         * @param range the iteration range
         * @return a new low-major backward iterator from the given {@code range}
         * @param <I> the iterator <em>union</em> type
         */
        @SuppressWarnings("unchecked")
        public static <I extends LowMajor & Backward> I backward(Range range) {
            return (I)new LowMajorBackward(new IndexState(
                range.start().toArray(),
                dec(range.end().toArray()),
                dec(range.end().toArray())
            ));
        }
    }

    /**
     * High-major iterators increases (or decreases) the lower indexes before
     * the higher indexes.
     *
     * <pre>
     * [0, 0, 0]
     * [1, 0, 0]
     * [0, 1, 0]
     * [1, 1, 0]
     * [0, 0, 1]
     * [1, 0, 1]
     * [0, 1, 1]
     * [1, 1, 1]
     * </pre>
     *
     * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
     * @since 3.0
     * @version 3.0
     */
    abstract sealed class HighMajor implements IdxIterator {

        private HighMajor() {
        }

        private static final class HighMajorForward extends HighMajor implements Forward {
            private final LowMajor.IndexState state;

            private HighMajorForward(LowMajor.IndexState state) {
                this.state = state;
            }

            @Override
            public boolean hasNext() {
                return
                    state.cursor[state.start.length - 1] <
                    state.end[state.start.length - 1];
            }

            @Override
            public int[] next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                final var next = state.cursor.clone();

                for (int i = 0; i < state.start.length; ++i) {
                    state.cursor[i] = next[i] + 1;

                    if (state.cursor[i] >= state.end[i] && i < state.start.length - 1) {
                        for (int j = 0; j <= i; ++j) {
                            state.cursor[j] = state.start[i];
                        }
                    } else {
                        break;
                    }
                }

                return next;
            }
        }

        private static final class HighMajorBackward extends HighMajor implements Backward {
            private final LowMajor.IndexState state;

            private HighMajorBackward(LowMajor.IndexState state) {
                this.state = state;
            }

            @Override
            public boolean hasNext() {
                return
                    state.cursor[state.start.length - 1] >=
                    state.start[state.start.length - 1];
            }

            @Override
            public int[] next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                final var next = state.cursor.clone();

                for (int i = 0; i < state.start.length; ++i) {
                    state.cursor[i] = next[i] - 1;

                    if (state.cursor[i] < state.start[i] && i < state.start.length - 1) {
                        for (int j = 0; j <= i; ++j) {
                            state.cursor[j] = state.end[i];
                        }
                    } else {
                        break;
                    }
                }

                return next;
            }
        }

        /**
         * Create a new high-major forward iterator from the given {@code range}.
         *
         * @param range the iteration range
         * @return a new high-major forward iterator from the given {@code range}
         * @param <I> the iterator <em>union</em> type
         */
        @SuppressWarnings("unchecked")
        public static <I extends HighMajor & Forward> I forward(Range range) {
            return (I)new HighMajorForward(new LowMajor.IndexState(
                range.start().toArray(),
                range.end().toArray(),
                range.start().toArray()
            ));
        }

        /**
         * Create a new high-major backward iterator from the given {@code range}.
         *
         * @param range the iteration range
         * @return a new high-major backward iterator from the given {@code range}
         * @param <I> the iterator <em>union</em> type
         */
        @SuppressWarnings("unchecked")
        public static <I extends HighMajor & Backward> I backward(Range range) {
            return (I)new HighMajorBackward(new LowMajor.IndexState(
                range.start().toArray(),
                dec(range.end().toArray()),
                dec(range.end().toArray())
            ));
        }
    }

    private static int[] dec(int[] array) {
        for (int i = 0; i < array.length; ++i) {
            --array[i];
        }
        return array;
    }

}
