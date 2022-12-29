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
 * This interface defines a projection from 3-d to 2-d.
 */
@FunctionalInterface
public interface Projection3d {

    /**
     * Performs the projection the given {@code structure}.
     *
     * @param structure the structure to apply this projection
     * @return the projected 2-d structure
     */
    Structure2d apply(final Structure3d structure);


    /**
     * Create a <em>slice</em>-projection for the slice with the given
     * {@code index}.
     *
     * @param index the column index
     * @return a new <em>slice</em>-projection
     * @throws IllegalArgumentException if the given {@code index} is negative
     */
    static Projection3d slice(final int index) {
        if (index < 0) {
            throw new IllegalArgumentException(
                "Projection index must be greater then zero: " + index
            );
        }

        return structure -> {
            if (index >= structure.extent().slices()) {
                throw new IndexOutOfBoundsException(
                    "Attempted to access " + structure.extent() + " at column=" +
                        index
                );
            }

            return new Structure2d(
                new Extent2d(
                    structure.extent().rows(),
                    structure.extent().cols()
                ),
                new Order2d(
                    new Index2d(
                        structure.order().start().row(),
                        structure.order().index(index, 0, 0)
                    ),
                    new Stride2d(
                        structure.order().stride().row(),
                        structure.order().stride().col()
                    )
                )
            );
        };
    }

    /**
     * Create a <em>row</em>-projection for the row with the given {@code index}.
     *
     * @param index the row index
     * @return a new <em>row</em>-projection
     * @throws IllegalArgumentException if the given {@code index} is negative
     */
    static Projection3d row(final int index) {
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

            return new Structure2d(
                new Extent2d(
                    structure.extent().slices(),
                    structure.extent().cols()
                ),
                new Order2d(
                    new Index2d(
                        structure.order().start().slice(),
                        structure.order().index(0, index, 0)
                    ),
                    new Stride2d(
                        structure.order().stride().slice(),
                        structure.order().stride().col()
                    )
                )
            );
        };
    }

    /**
     * Create a <em>column</em>-projection for the column with the given
     * {@code index}.
     *
     * @param index the column index
     * @return a new <em>column</em>-projection
     * @throws IllegalArgumentException if the given {@code index} is negative
     */
    static Projection3d col(final int index) {
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

            return new Structure2d(
                new Extent2d(
                    structure.extent().slices(),
                    structure.extent().rows()
                ),
                new Order2d(
                    new Index2d(
                        structure.order().start().slice(),
                        structure.order().index(0, 0, index)
                    ),
                    new Stride2d(
                        structure.order().stride().slice(),
                        structure.order().stride().row()
                    )
                )
            );
        };
    }

}
