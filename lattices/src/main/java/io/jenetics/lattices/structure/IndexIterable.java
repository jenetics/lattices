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

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import io.jenetics.lattices.function.IntsConsumer;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public interface IndexIterable extends Iterable<Index> {

    @Override
    IndexIterator iterator();

    /**
     * Performs an action for each position of {@code this} dimension.
     *
     * @param action an action to perform on the positions
     */
    default void forEach(Consumer<? super Index> action) {
        for (var next : this) {
            action.accept(next);
        }
    }

    /**
     * Returns whether any position of this dimension match the provided
     * predicate. May not evaluate the predicate on all positions if not
     * necessary for determining the result. If the dimension is empty then
     * {@code false} is returned and the predicate is not evaluated.
     *
     * @param predicate predicate to apply to elements of this dimension
     * @return {@code true} if any position of the dimension match the
     *         provided predicate, otherwise {@code false}
     */
    default boolean anyMatch(Predicate<? super Index> predicate) {
        for (var next : this) {
            if (predicate.test(next)) {
                return true;
            }
        }

        return false;
    }

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
    default boolean allMatch(Predicate<? super Index> predicate) {
        for (var next : this) {
            if (!predicate.test(next)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns whether no position of this dimension match the provided
     * predicate. May not evaluate the predicate on all positions if not
     * necessary for determining the result.  If the dimension is empty then
     * {@code true} is returned and the predicate is not evaluated.
     *
     * @param predicate predicate to apply to positions of this dimension
     * @return {@code true} if either no position of the dimension match the
     *         provided predicate, or the dimension is empty, otherwise
     *         {@code false}
     */
    default boolean nonMatch(Predicate<? super Index> predicate) {
        for (var next : this) {
            if (predicate.test(next)) {
                return false;
            }
        }

        return true;
    }

    static IndexIterable of(
        Range range,
        Function<? super Range, ? extends IndexIterator> iterator
    ) {
        return () -> iterator.apply(range);
    }

    public static void main(String[] args) {
        final var iterable = IndexIterable.of(
            Range.of(Extent.of(2, 3, 4)),
            IndexIterator.LowMajor::backward
        );
    }

}
