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
 * Defines row- and columns strides.
 *
 * @param row the row stride value
 * @param col the column stride value
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record Stride2d(int row, int col) implements Stride {

    /**
     * Create a new 2-stride.
     *
     * @param row the row stride
     * @param col the column stride
     * @throws IndexOutOfBoundsException if the one of the strides is smaller
     *         than one
     */
    public Stride2d {
        if (row < 1 || col < 1) {
            throw new IndexOutOfBoundsException(
                "Stride must be positive: [%d, %d].".formatted(row, col)
            );
        }
    }

    /**
     * Return the number of dimensions; always 2.
     *
     * @return 2
     */
    @Override
    public int dimensionality() {
        return 2;
    }

    @Override
    public int at(int dimension) {
        return switch (dimension) {
            case 0 -> row;
            case 1 -> col;
            default -> throw new IndexOutOfBoundsException(
                "Dimension out of range [0..%d): %d."
                    .formatted(dimensionality(), dimension)
            );
        };
    }

}
