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
package io.jenetics.linealgebra.structure;

/**
 * 2-d structural mixin interface.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
public interface Structural2d {

    /**
     * Return the structure for 2-d grid.
     *
     * @return the structure for 2-d grid
     */
    Structure2d structure();

    /**
     * Checks whether the extent of this structural object is the same as the
     * given {@code other} extent.
     *
     * @param other the other extent to check
     * @throws IllegalArgumentException if the given {@code other} extent doesn't
     *         match
     */
    default void requireSameExtent(final Extent2d other) {
        if (!extent().equals(other)) {
            throw new IllegalArgumentException(
                "Incompatible extent: %s != %s.".formatted(extent(), extent())
            );
        }
    }

    /**
     * Return the dimension of {@code this} 2-d structures.
     *
     * @return the dimension of {@code this} 2-d structures
     */
    default Extent2d extent() {
        return structure().extent();
    }

    /**
     * Return the defined order of {@code this} 2-d structures.
     *
     * @return the defined order of {@code this} 2-d structures
     */
    default Order2d order() {
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
     * Return the number of rows of {@code this} structures.
     *
     * @return the number of rows of {@code this} structures
     */
    default int rows() {
        return extent().rows();
    }

    /**
     * Return the number of columns of {@code this} structures.
     *
     * @return the number of columns of {@code this} structures
     */
    default int cols() {
        return extent().cols();
    }

}
