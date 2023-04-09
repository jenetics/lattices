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

import io.jenetics.lattices.structure.Extent1d;
import io.jenetics.lattices.structure.Range1d;

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
    void forEach(IntConsumer action);

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
    boolean anyMatch(IntPredicate predicate);

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
    boolean allMatch(IntPredicate predicate);

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
    boolean nonMatch(IntPredicate predicate);

    /**
     * Return a <em>default</em> loop implementation with the given {@code range}.
     *
     * @param range the loop range
     * @return a default loop for the given {@code range}
     */
    static Loop1d of(Range1d range) {
        requireNonNull(range);
        return new Loop1dForward(range);
    }

    /**
     * Return a <em>default</em> loop implementation with the given {@code extent}.
     *
     * @param extent the loop range
     * @return a default loop for the given {@code extent}
     */
    static Loop1d of(Extent1d extent) {
        return of(new Range1d(extent));
    }

}
