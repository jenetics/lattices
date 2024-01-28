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
 * Represents a 3-d index.
 *
 * @param row the row index
 * @param col the column index
 * @param slice the slice index
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record Index3d(int slice, int row, int col) implements Index {

    /**
     * Index where slice, row and column are zero.
     */
    public static final Index3d ZERO = new Index3d(0, 0, 0);

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
