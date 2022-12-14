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

import java.util.function.IntConsumer;
import java.util.function.IntPredicate;

/**
 * 1-d structural mixin interface.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
public interface Grid1d extends Loop1d {

    /**
     * Return the structure for 2-d structures.
     *
     * @return the structure for 2-d structures
     */
    Structure1d structure();

    /**
     * Return the dimension of {@code this} 2-d structures.
     *
     * @return the dimension of {@code this} 2-d structures
     */
    default Extent1d extent() {
        return structure().extent();
    }

    /**
     * Return the defined order of {@code this} 2-d structures.
     *
     * @return the defined order of {@code this} 2-d structures
     */
    default Order1d order() {
        return structure().order();
    }

    /**
     * Return the number of cells of this {@code this} 2-d structures.
     *
     * @return the number of cells of this {@code this} 2-d structures
     */
    default int size() {
        return extent().size();
    }

    /**
     * Return the default looping strategy of this structural, which can be
     * overridden by the implementation, if desired.
     *
     * @return the looping strategy of this structural
     */
    default Loop1d loop() {
        return new Loop1d.Forward(extent());
    }

    @Override
    default void forEach(final IntConsumer action) {
        loop().forEach(action);
    }

    @Override
    default boolean anyMatch(final IntPredicate predicate) {
        return loop().anyMatch(predicate);
    }

    @Override
    default boolean allMatch(final IntPredicate predicate) {
        return loop().allMatch(predicate);
    }

    @Override
    default boolean nonMatch(final IntPredicate predicate) {
        return loop().nonMatch(predicate);
    }

}
