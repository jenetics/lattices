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

/**
 * This interface performs a projection from 2-d to 1-d.
 */
@FunctionalInterface
public interface Projection2d {

    /**
     * Performs the projection the given {@code structure}.
     *
     * @param structure the structure to apply this projection
     * @return the projected 1-d structure
     */
    Structure1d apply(final Structure2d structure);

    /**
     * Create a <em>row</em>-projection for the row with the given {@code index}.
     *
     * @param index the row index
     * @return a new <em>row</em>-projection
     * @throws IllegalArgumentException if the given {@code index} is negative
     */
    static Projection2d row(final int index) {
        if (index < 0) {
            throw new IllegalArgumentException(
                "Projection index must be greater then zero: " + index
            );
        }

        return structure -> {
            if (index >= structure.extent().rows()) {
                throw new IndexOutOfBoundsException(
                    "Attempted to access " + structure.extent() + " at row=" + index
                );
            }

            return new Structure1d(
                new Extent1d(structure.extent().cols()),
                new Order1d(
                    structure.order().index(index, 0),
                    structure.order().stride().col()
                )
            );
        };
    }

    /**
     * Create a <em>column</em>-projection for the row with the given
     * {@code index}.
     *
     * @param index the column index
     * @return a new <em>column</em>-projection
     * @throws IllegalArgumentException if the given {@code index} is negative
     */
    static Projection2d col(final int index) {
        if (index < 0) {
            throw new IllegalArgumentException(
                "Projection index must be greater then zero: " + index
            );
        }

        return structure -> {
            if (index >= structure.extent().cols()) {
                throw new IndexOutOfBoundsException(
                    "Attempted to access " + structure.extent() + " at column=" +
                        index
                );
            }

            return new Structure1d(
                new Extent1d(structure.extent().rows()),
                new Order1d(
                    structure.order().index(0, index),
                    structure.order().stride().row()
                )
            );
        };
    }

}
