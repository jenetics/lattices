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
 * Defines slice, row and column strides.
 *
 * @param slice the slice stride value
 * @param row the row stride value
 * @param col the column stride value
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record Stride3d(int slice, int row, int col) implements Stride {

    /**
     * Create a new 3-d stride.
     *
     * @param slice the slice stride
     * @param row the row stride
     * @param col the column stride
     * @throws IndexOutOfBoundsException if the one of the strides is smaller
     *         than one
     */
    public Stride3d {
        if (slice < 1 || row < 1 || col < 1) {
            throw new IndexOutOfBoundsException(
                "Stride must be positive: [%d, %d, %d]."
                    .formatted(slice, row, col)
            );
        }
    }

    /**
     * Return the number of dimensions; always 3.
     *
     * @return 3
     */
    @Override
    public int dimensionality() {
        return 3;
    }

    @Override
    public int at(int dimension) {
        return switch (dimension) {
            case 0 -> slice;
            case 1 -> row;
            case 2 -> col;
            default -> throw new IndexOutOfBoundsException(
                "Dimension out of range [0..%d): %d."
                    .formatted(dimensionality(), dimension)
            );
        };
    }

}
