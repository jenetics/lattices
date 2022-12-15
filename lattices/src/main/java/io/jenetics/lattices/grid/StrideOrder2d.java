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
 * Implements a structure order by defining start indexes and strides.
 *
 * @param rowStart the index of the first row element
 * @param colStart the index of the first column element
 * @param rowStride the number of elements between two rows
 * @param colStride the number of elements between two columns
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record StrideOrder2d(
    int rowStart,
    int colStart,
    int rowStride,
    int colStride
)
    implements Order2d
{

    /**
     * Create a new row-major {@link Order2d} object for the given matrix
     * dimension. This is the default implementation for the element order
     * of the matrix.
     *
     * @param extent the structure extent
     */
    public StrideOrder2d(final Extent2d extent) {
        this(0, 0, extent.cols(), 1);
    }

    public StrideOrder2d(
        final int rowStart,
        final int colStart,
        final Extent2d extent
    ) {
        this(rowStart, colStart, extent.cols(), 1);
    }

    @Override
    public int index(final int row, final int col) {
        return rowStart + row*rowStride + colStart + col*colStride;
    }

    @Override
    public StrideOrder2d transpose() {
        return new StrideOrder2d(colStart, rowStart, colStride, rowStride);
    }

}
