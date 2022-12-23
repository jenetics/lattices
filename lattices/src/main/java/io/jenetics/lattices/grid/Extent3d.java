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
 * The extent of 3-d structures.
 *
 * @param rows the number of rows, must be greater or equal zero
 * @param cols the number of columns, must be greater or equal zero
 * @param slices the number of slices, must be greater or equal zero
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record Extent3d(int rows, int cols, int slices) {

    public Extent3d {
        if (rows < 0 || cols < 0 || slices < 0) {
            throw new IllegalArgumentException(
                "Extent must be greater or equal zero: [%d, %d, %d]."
                    .formatted(rows, cols, slices)
            );
        }
    }

    /**
     * The number of matrix elements (cells) a matrix with {@code this}
     * dimensions consists of.
     *
     * @return the number of cells for {@code this} matrix dimension
     */
    public int size() {
        return rows*cols*slices;
    }

    @Override
    public String toString() {
        return "[%d, %d, %d]".formatted(rows(), cols(), slices());
    }

}
