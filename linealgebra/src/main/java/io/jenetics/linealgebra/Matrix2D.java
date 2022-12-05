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
package io.jenetics.linealgebra;

import static java.util.Objects.requireNonNull;

/**
 * This interface defines the structure for 2-d matrices holding objects or
 * primitive data types.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
public interface Matrix2D<ARRAY_TYPE, M extends Matrix2D<ARRAY_TYPE, M>>
    extends Matrix<ARRAY_TYPE>
{

    /**
     * The dimension of the {@link Matrix2D} object.
     *
     * @param rows the number of rows
     * @param cols the number of columns
     */
    record Dimension(int rows, int cols) {
        public Dimension {
            if (rows < 0) {
                throw new IllegalArgumentException(
                    "Number of rows must greater or equal than zero: " + rows
                );
            }
            if (cols < 0) {
                throw new IllegalArgumentException(
                    "Number of columns must greater or equal than zero: " + cols
                );
            }
        }

        public int size() {
            return rows*cols;
        }

        public Dimension transpose() {
            return new Dimension(cols, rows);
        }
    }

    /**
     * Represents the order for accessing the linearly stored matrix data.
     */
    @FunctionalInterface
    interface Order {

        /**
         * Return the position of the given coordinate within the (virtual or
         * non-virtual) internal 1-dimensional array.
         *
         * @param row the row index
         * @param col the column index
         * @return the (linearized) index of the given {@code row} and {@code col}
         */
        int index(final int row, final int col);

        /**
         * Return a new order function which swaps row index with column index.
         *
         * @return a new transposed order function
         */
        default Order transpose() {
            return (row, col) -> index(col, row);
        }
    }

    /**
     * Represents the <em>row-major</em> order.
     *
     * @param rowZero the index of the first row element
     * @param colZero the index of the first column element
     * @param rowStride the number of elements between two rows
     * @param colStride the number of elements between two columns
     */
    record RowMajor(
        int rowZero,
        int colZero,
        int rowStride,
        int colStride
    )
        implements Order
    {
        @Override
        public int index(final int row, final int col) {
            return rowZero + row*rowStride + colZero + col*colStride;
        }

        @Override
        public RowMajor transpose() {
            return new RowMajor(colZero, rowZero, colStride, rowStride);
        }

        public static RowMajor of(final Dimension dimension) {
            return new RowMajor(0, 0, dimension.cols(), 1);
        }
    }

    /**
     * Defines the structure of a 2-d matrix, which is defined by the dimension
     * of the matrix and the index order of the underlying element array.
     *
     * @param dimension the dimension of the matrix
     * @param order the element order
     */
    record Structure(Dimension dimension, Order order) {
        public Structure {
            requireNonNull(dimension);
            requireNonNull(order);
        }

        public Structure transpose() {
            return new Structure(dimension.transpose(), order.transpose());
        }
    }

    /**
     * Return the structure of {@code this} 2-d matrix.
     *
     * @return the structure of {@code this} 2-d matrix
     */
    Structure structure();

    /**
     * Return a new view of the underlying element array with the given
     * {@code structure}. The data are unchanged and not copied.
     *
     * @param structure the structure definition of the data array
     * @return a new view of the underlying element array
     */
    M view(final Structure structure);

    /**
     * Return a new minimal copy of the underlying element array with the given
     * {@code structure}.
     *
     * @param structure the structure definition of the data array
     * @return a new minimal copy of the underlying element array
     */
    M copy(final Structure structure);

    /**
     * Return a new minimal copy of the underlying element array.
     *
     * @return a new minimal copy of the underlying element array
     */
    default M copy() {
        return copy(structure());
    }

    /**
     * Return the dimension of {@code this} 2-d matrix.
     *
     * @return the dimension of {@code this} 2-d matrix
     */
    default Dimension dimension() {
        return structure().dimension();
    }

    /**
     * Return the defined order of {@code this} 2-d matrix.
     *
     * @return the defined order of {@code this} 2-d matrix
     */
    default Order order() {
        return structure().order();
    }

    @Override
    default int size() {
        return dimension().size();
    }

    default int rows() {
        return dimension().rows();
    }

    default int cols() {
        return dimension().cols();
    }

}
