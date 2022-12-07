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

import io.jenetics.linealgebra.matrix.Matrix1d;
import io.jenetics.linealgebra.matrix.Matrix2d;

import static java.util.Objects.requireNonNull;

/**
 * Defines the structure of a 2-d matrix, which is defined by the dimension
 * of the matrix and the index order of the underlying element array.
 *
 * @param extent the dimension of the matrix
 * @param order the element order
 */
public record Structure2d(Extent2d extent, Order2d order) {

    public Structure2d {
        requireNonNull(extent);
        requireNonNull(order);
    }

    /**
     * Create a new matrix structure with the given dimension and the default
     * element order.
     *
     * @param dim the matrix dimension
     */
    public Structure2d(final Extent2d dim) {
        this(dim, new MajorOrder2d(dim));
    }

    /**
     * Transposes the matrix structure without copying any matrix elements.
     *
     * @return the transposed matrix structure
     */
    public Structure2d transpose() {
        return new Structure2d(extent.transpose(), order.transpose());
    }

    /**
     * Create a new {@link Matrix1d.Structure} object which can be used to
     * create a column view {@link Matrix1d}.
     *
     * @param index the column index
     * @return a new {@link Matrix1d.Structure} object
     * @throws IndexOutOfBoundsException if {@code index < 0 || index >= cols()}
     * @throws UnsupportedOperationException if the {@link #order()} function
     *         is not an instance of {@link MajorOrder2d}
     */
    public Matrix1d.Structure col(final int index) {
        if (index < 0 || index >= extent().cols()) {
            throw new IndexOutOfBoundsException(
                "Attempted to access " + extent() + " at column=" + index
            );
        }

        if (order instanceof MajorOrder2d mo) {
            return new Matrix1d.Structure(
                new Matrix1d.Dim(extent().rows()),
                new Matrix1d.MajorOrder(
                    mo.index(0, index),
                    mo.rowStride()
                )
            );
        } else {
            throw new UnsupportedOperationException(
                "Column view structure not supported by " + order
            );
        }
    }

    /**
     * Create a new {@link Matrix1d.Structure} object which can be used to
     * create a row view {@link Matrix1d}.
     *
     * @param index the row index
     * @return a new {@link Matrix1d.Structure} object
     * @throws IndexOutOfBoundsException if {@code index < 0 || index >= rows()}
     * @throws UnsupportedOperationException if the {@link #order()} function
     *         is not an instance of {@link MajorOrder2d}
     */
    public Matrix1d.Structure row(final int index) {
        if (index < 0 || index >= extent().rows()) {
            throw new IndexOutOfBoundsException(
                "Attempted to access " + extent() + " at row=" + index
            );
        }

        if (order instanceof MajorOrder2d mo) {
            return new Matrix1d.Structure(
                new Matrix1d.Dim(extent().cols()),
                new Matrix1d.MajorOrder(
                    mo.index(index, 0),
                    mo.colStride()
                )
            );
        } else {
            throw new UnsupportedOperationException(
                "Row view structure not supported by " + order
            );
        }
    }

}
