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
package io.jenetics.lattices.grid;

import static java.util.Objects.requireNonNull;

import java.util.function.IntConsumer;
import java.util.function.IntPredicate;

/**
 * Looping strategies for 1-d structures.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public interface Loop1d {

    /**
     * Performs an action for each position of {@code this} dimension.
     *
     * @param action an action to perform on the positions
     */
    void forEach(final IntConsumer action);

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
    boolean anyMatch(final IntPredicate predicate);

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
    boolean allMatch(final IntPredicate predicate);

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
    boolean nonMatch(final IntPredicate predicate);

    /**
     * Implements a forward loop iteration.
     *
     * @param range the range which defines the boundaries of the loop
     */
    record Forward(Range1d range) implements Loop1d {

        /**
         * Implements a forward loop iteration.
         *
         * @param extent the extent which defines the boundaries of the loop
         */
        public Forward(final Extent1d extent) {
            this(new Range1d(extent));
        }

        @Override
        public void forEach(final IntConsumer action) {
            requireNonNull(action);

            for (int i = range.start().value(),
                 n = range.start().value() + range.extent().size();
                 i < n; ++i)
            {
                action.accept(i);
            }
        }

        @Override
        public boolean anyMatch(final IntPredicate predicate) {
            requireNonNull(predicate);

            for (int i = range.start().value(),
                 n = range.start().value() + range.extent().size();
                 i < n; ++i)
            {
                if (predicate.test(i)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean allMatch(final IntPredicate predicate) {
            requireNonNull(predicate);

            for (int i = range.start().value(),
                 n = range.start().value() + range.extent().size();
                 i < n; ++i)
            {
                if (!predicate.test(i)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public boolean nonMatch(final IntPredicate predicate) {
            requireNonNull(predicate);

            for (int i = range.start().value(),
                 n = range.start().value() + range.extent().size();
                 i < n; ++i)
            {
                if (predicate.test(i)) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Implements a backward loop iteration.
     *
     * @param range the range which defines the boundaries of the loop
     */
    record Backward(Range1d range) implements Loop1d {

        /**
         * Implements a backward loop iteration.
         *
         * @param extent the extent which defines the boundaries of the loop
         */
        public Backward(final Extent1d extent) {
            this(new Range1d(extent));
        }

        @Override
        public void forEach(final IntConsumer action) {
            requireNonNull(action);

            for (int i = range.start().value() + range.extent().size(),
                 s = range.start().value();
                 --i >= s;)
            {
                action.accept(i);
            }
        }

        @Override
        public boolean anyMatch(final IntPredicate predicate) {
            requireNonNull(predicate);

            for (int i = range.start().value() + range.extent().size(),
                 s = range.start().value();
                 --i >= s;)
            {
                if (predicate.test(i)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean allMatch(final IntPredicate predicate) {
            requireNonNull(predicate);

            for (int i = range.start().value() + range.extent().size(),
                 s = range.start().value();
                 --i >= s;)
            {
                if (!predicate.test(i)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public boolean nonMatch(final IntPredicate predicate) {
            requireNonNull(predicate);

            for (int i = range.start().value() + range.extent().size(),
                 s = range.start().value();
                 --i >= s;)
            {
                if (predicate.test(i)) {
                    return false;
                }
            }
            return true;
        }
    }

}
