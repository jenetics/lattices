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

/**
 * This interface defines a projection from 2-d to 1-d.
 */
@FunctionalInterface
public interface Projection2d {

    record Row(int index) implements Projection2d {
        public Row {
            if (index < 0) {
                throw new IllegalArgumentException(
                    "Projection index must be greater then zero: " + index
                );
            }
        }

        @Override
        public Structure1d apply(final Structure2d structure) {
            if (index >= structure.extent().rows()) {
                throw new IndexOutOfBoundsException(
                    "Attempted to access " + structure.extent() + " at row=" + index
                );
            }

            if (structure.order() instanceof StrideOrder2d ord) {
                return new Structure1d(
                    new Extent1d(structure.extent().cols()),
                    new StrideOrder1d(
                        ord.index(index, 0),
                        ord.stride().col()
                    )
                );
            } else {
                throw new UnsupportedOperationException(
                    "Row view structure not supported by " + structure.order()
                );
            }
        }
    }

    record Col(int index) implements Projection2d {
        public Col {
            if (index < 0) {
                throw new IllegalArgumentException(
                    "Projection index must be greater then zero: " + index
                );
            }
        }

        @Override
        public Structure1d apply(final Structure2d structure) {
            if (index >= structure.extent().cols()) {
                throw new IndexOutOfBoundsException(
                    "Attempted to access " + structure.extent() + " at column=" +
                        index()
                );
            }

            if (structure.order() instanceof StrideOrder2d ord) {
                return new Structure1d(
                    new Extent1d(structure.extent().rows()),
                    new StrideOrder1d(
                        ord.index(0, index()),
                        ord.stride().row()
                    )
                );
            } else {
                throw new UnsupportedOperationException(
                    "Column view structure not supported by " + structure.order()
                );
            }
        }

    }

    /**
     * Performs the projection the given {@code structure}.
     *
     * @param structure the structure to apply this projection
     * @return the projected 1-d structure
     */
    Structure1d apply(final Structure2d structure);


    static Projection2d row(final int index) {
        return structure -> {
            if (index >= structure.extent().rows()) {
                throw new IndexOutOfBoundsException(
                    "Attempted to access " + structure.extent() + " at row=" + index
                );
            }

            if (structure.order() instanceof StrideOrder2d ord) {
                return new Structure1d(
                    new Extent1d(structure.extent().cols()),
                    new StrideOrder1d(
                        ord.index(index, 0),
                        ord.stride().col()
                    )
                );
            } else {
                throw new UnsupportedOperationException(
                    "Row view structure not supported by " + structure.order()
                );
            }
        };
    }

}
