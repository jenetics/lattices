/*
 * Java Linear Algebra Library (@__identifier__@).
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
package io.jenetics.linealgebra.grid;

import static java.util.Objects.requireNonNull;

import io.jenetics.linealgebra.function.IntIntConsumer;
import io.jenetics.linealgebra.function.IntIntPredicate;

/**
 * Looping strategies for 2-d structures.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
public interface Loop2d {

    /**
     * Performs an action for each position of {@code this} dimension.
     *
     * @param action an action to perform on the positions
     */
    void forEach(final IntIntConsumer action);

    /**
     * Returns whether any position of this dimension match the provided
     * predicate.  May not evaluate the predicate on all positions if not
     * necessary for determining the result. If the dimension is empty then
     * {@code false} is returned and the predicate is not evaluated.
     *
     * @param predicate predicate to apply to elements of this dimension
     * @return {@code true} if any position of the dimension match the
     *         provided predicate, otherwise {@code false}
     */
    boolean anyMatch(final IntIntPredicate predicate);

    /**
     * Returns whether all positions of {@code this} dimension match the
     * provided {@code predicate}. May not evaluate the predicate on all
     * positions if not necessary for determining the result. If the
     * dimension is empty then {@code true} is returned and the
     * {@code predicate} is not evaluated.
     *
     * @param predicate a non-interfering, stateless predicate to apply to
     *        positions of {@code this} dimension
     * @return {@code true} if either all positions of the dimension match
     *         the provided {@code predicate} or the dimension is empty,
     *         otherwise {@code false}
     */
    boolean allMatch(final IntIntPredicate predicate);

    /**
     * Returns whether no position of this dimension match the provided
     * predicate. May not evaluate the predicate on all positions if not
     * necessary for determining the result.  If the dimension is empty then
     * {@code true} is returned and the predicate is not evaluated.
     *
     * @param predicate predicate to apply to positions of this dimension
     * @return {@code true} if either no position of the dimension match the
     *         provided predicate or the dimension is empty, otherwise
     *         {@code false}
     */
    boolean nonMatch(final IntIntPredicate predicate);

    /**
     * Row-major loop implementation. The rows and columns are iterated forward.
     *
     * @param range the range which defines the boundaries of the loop
     */
    record RowMajor(Range2d range) implements Loop2d {

        /**
         * Row-major implementation of the loop strategy
         *
         * @param extent the extent which defines the boundaries of the loop
         */
        public RowMajor(final Extent2d extent) {
            this(new Range2d(extent));
        }

        @Override
        public void forEach(final IntIntConsumer action) {
            requireNonNull(action);

            for (int r = range.row(); r < range.height(); ++r) {
                for (int c = range.col(); c < range.width(); ++c) {
                    action.accept(r, c);
                }
            }
        }

        @Override
        public boolean anyMatch(final IntIntPredicate predicate) {
            requireNonNull(predicate);

            for (int r = range.row(); r < range.height(); ++r) {
                for (int c = range.col(); c < range.width(); ++c) {
                    if (predicate.test(r, c)) {
                        return true;
                    }
                }
            }

            return false;
        }

        @Override
        public boolean allMatch(final IntIntPredicate predicate) {
            requireNonNull(predicate);

            for (int r = range.row(); r < range.height(); ++r) {
                for (int c = range.col(); c < range.width(); ++c) {
                    if (!predicate.test(r, c)) {
                        return false;
                    }
                }
            }

            return true;
        }

        @Override
        public boolean nonMatch(final IntIntPredicate predicate) {
            requireNonNull(predicate);

            for (int r = range.row(); r < range.height(); ++r) {
                for (int c = range.col(); c < range.width(); ++c) {
                    if (predicate.test(r, c)) {
                        return false;
                    }
                }
            }

            return true;
        }
    }

    /**
     * Column-major loop implementation. he rows and columns are iterated
     * forward.
     *
     * @param range the range which defines the boundaries of the loop
     */
    record ColMajor(Range2d range) implements Loop2d {

        /**
         * Column-major implementation of the loop strategy
         *
         * @param extent the extent which defines the boundaries of the loop
         */
        public ColMajor(final Extent2d extent) {
            this(new Range2d(extent));
        }

        @Override
        public void forEach(IntIntConsumer action) {
            requireNonNull(action);

            for (int c = range.col(); c < range.width(); ++c) {
                for (int r = range.row(); r < range.height(); ++r) {
                    action.accept(r, c);
                }
            }
        }

        @Override
        public boolean anyMatch(final IntIntPredicate predicate) {
            requireNonNull(predicate);

            for (int c = range.col(); c < range.width(); ++c) {
                for (int r = range.row(); r < range.height(); ++r) {
                    if (predicate.test(r, c)) {
                        return true;
                    }
                }
            }

            return false;
        }

        @Override
        public boolean allMatch(final IntIntPredicate predicate) {
            requireNonNull(predicate);

            for (int c = range.col(); c < range.width(); ++c) {
                for (int r = range.row(); r < range.height(); ++r) {
                    if (!predicate.test(r, c)) {
                        return false;
                    }
                }
            }

            return true;
        }

        @Override
        public boolean nonMatch(final IntIntPredicate predicate) {
            requireNonNull(predicate);

            for (int c = range.col(); c < range.width(); ++c) {
                for (int r = range.row(); r < range.height(); ++r) {
                    if (predicate.test(r, c)) {
                        return false;
                    }
                }
            }

            return true;
        }
    }

}
