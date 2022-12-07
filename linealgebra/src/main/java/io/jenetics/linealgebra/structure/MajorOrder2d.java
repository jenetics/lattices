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
 * Represents the <em>row-major</em> order.
 *
 * @param rowZero the index of the first row element
 * @param colZero the index of the first column element
 * @param rowStride the number of elements between two rows
 * @param colStride the number of elements between two columns
 */
public record MajorOrder2d(
    int rowZero, int colZero,
    int rowStride, int colStride
)
    implements Order2d
{

    /**
     * Create a new row-major {@link Order2d} object for the given matrix
     * dimension. This is the default implementation for the element order
     * of the matrix.
     *
     * @param dim the matrix dimension
     */
    public MajorOrder2d(final Dim2d dim) {
        this(0, 0, dim.cols(), 1);
    }

    @Override
    public int index(final int row, final int col) {
        return rowZero + row*rowStride + colZero + col*colStride;
    }

    @Override
    public MajorOrder2d transpose() {
        return new MajorOrder2d(colZero, rowZero, colStride, rowStride);
    }

}
