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

import io.jenetics.lattices.function.IntIntIntConsumer;
import io.jenetics.lattices.function.IntIntIntPredicate;
import io.jenetics.lattices.structure.Extent3d;
import io.jenetics.lattices.structure.Range3d;

import static java.util.Objects.requireNonNull;

/**
 * Looping strategies for 3-d structures.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public interface Loop3d {

    /**
     * Performs an action for each position of {@code this} dimension.
     *
     * @param action an action to perform on the positions
     */
    void forEach(final IntIntIntConsumer action);

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
    boolean anyMatch(final IntIntIntPredicate predicate);

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
    boolean allMatch(final IntIntIntPredicate predicate);

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
    boolean nonMatch(final IntIntIntPredicate predicate);


    record SliceFirst(Range3d range) implements Loop3d {

        public SliceFirst(final Extent3d extent) {
            this(new Range3d(extent));
        }

        @Override
        public void forEach(final IntIntIntConsumer action) {
            requireNonNull(action);

            for (int s = range.start().slice(),
                 d = range.start().row() + range.extent().rows();
                 s < d; ++s)
            {
                for (int r = range.start().row(),
                     h = range.start().row() + range.extent().rows();
                     r < h; ++r)
                {
                    for (int c = range.start().col(),
                         w = range.start().col() + range.extent().cols();
                         c < w; ++c)
                    {
                        action.accept(s, r, c);
                    }
                }
            }
        }

        @Override
        public boolean anyMatch(final IntIntIntPredicate predicate) {
            requireNonNull(predicate);

            for (int s = range.start().slice(),
                 d = range.start().row() + range.extent().rows();
                 s < d; ++s)
            {
                for (int r = range.start().row(),
                     h = range.start().row() + range.extent().rows();
                     r < h; ++r)
                {
                    for (int c = range.start().col(),
                         w = range.start().col() + range.extent().cols();
                         c < w; ++c)
                    {
                        if (predicate.test(s, r, c)) {
                            return true;
                        }
                    }
                }
            }

            return false;
        }

        @Override
        public boolean allMatch(final IntIntIntPredicate predicate) {
            requireNonNull(predicate);

            for (int s = range.start().slice(),
                 d = range.start().row() + range.extent().rows();
                 s < d; ++s)
            {
                for (int r = range.start().row(),
                     h = range.start().row() + range.extent().rows();
                     r < h; ++r)
                {
                    for (int c = range.start().col(),
                         w = range.start().col() + range.extent().cols();
                         c < w; ++c)
                    {
                        if (!predicate.test(s, r, c)) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }

        @Override
        public boolean nonMatch(final IntIntIntPredicate predicate) {
            requireNonNull(predicate);

            for (int s = range.start().slice(),
                 d = range.start().row() + range.extent().rows();
                 s < d; ++s)
            {
                for (int r = range.start().row(),
                     h = range.start().row() + range.extent().rows();
                     r < h; ++r)
                {
                    for (int c = range.start().col(),
                         w = range.start().col() + range.extent().cols();
                         c < w; ++c)
                    {
                        if (predicate.test(s, r, c)) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }

}
