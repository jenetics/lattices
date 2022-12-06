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
package io.jenetics.linealgebra.matrix;

import static java.util.Objects.requireNonNull;

import io.jenetics.linealgebra.function.IntIntConsumer;
import io.jenetics.linealgebra.function.IntIntPredicate;

/**
 * This interface defines the structure for 2-d matrices holding objects or
 * primitive data types.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
public interface Matrix2d<M extends Matrix2d<M>> extends Matrix<M> {

    /**
     * Defines the structure of a 2-d matrix, which is defined by the dimension
     * of the matrix and the index order of the underlying element array.
     *
     * @param dim the dimension of the matrix
     * @param order the element order
     */
    record Structure(Dim dim, Order order) {
        public Structure {
            requireNonNull(dim);
            requireNonNull(order);
        }

        /**
         * Create a new matrix structure with the given dimension and the default
         * element order.
         *
         * @param dim the matrix dimension
         */
        public Structure(final Dim dim) {
            this(dim, RowMajor.of(dim));
        }

        /**
         * Transposes the matrix structure without copying any matrix elements.
         *
         * @return the transposed matrix structure
         */
        public Structure transpose() {
            return new Structure(dim.transpose(), order.transpose());
        }
    }

    /**
     * The dimension of the {@link Matrix2d} object.
     *
     * @param rows the number of rows
     * @param cols the number of columns
     */
    record Dim(int rows, int cols) {
        public Dim {
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

        /**
         * The number of matrix elements (cells) a matrix with {@code this}
         * dimensions consists of.
         *
         * @return the number of cells for {@code this} matrix dimension
         */
        public int size() {
            return rows*cols;
        }

        /**
         * Swaps the dimensions of rows and columns.
         *
         * @return a new transposed dimension object
         */
        public Dim transpose() {
            return new Dim(cols, rows);
        }

        /**
         * Performs an action for each position of {@code this} dimension.
         *
         * @param action an action to perform on the positions
         */
        public void forEach(final IntIntConsumer action) {
            requireNonNull(action);

            for (int i = 0; i < rows; ++i) {
                for (int j = 0; j < cols; ++j) {
                    action.accept(i, j);
                }
            }
        }

        /**
         * Returns whether any position of this dimension match the provided
         * predicate.  May not evaluate the predicate on all positions if not
         * necessary for determining the result. If the dimension is empty then
         * {@code false} is returned and the predicate is not evaluated.
         *
         * @param predicate predicate to apply to elements of this dimension
         * @return {@code true} if any position of the dimension match the
         *         provided predicate, otherwise {@code false}
         */
        public boolean anyMatch(final IntIntPredicate predicate) {
            requireNonNull(predicate);

            for (int i = 0; i < rows; ++i) {
                for (int j = 0; j < cols; ++j) {
                    if (predicate.test(i, j)) {
                        return true;
                    }
                }
            }

            return false;
        }

        /**
         * Returns whether all positions of {@code this} dimension match the
         * provided {@code predicate}. May not evaluate the predicate on all
         * positions if not necessary for determining the result. If the
         * dimension is empty then {@code true} is returned and the
         * {@code predicate} is not evaluated.
         *
         * @param predicate a non-interfering, stateless predicate to apply to
         *        positions of {@code this} dimension
         * @return {@code true} if either all positions of the dimension match
         *         the provided {@code predicate} or the dimension is empty,
         *         otherwise {@code false}
         */
        public boolean allMatch(final IntIntPredicate predicate) {
            requireNonNull(predicate);

            for (int i = 0; i < rows; ++i) {
                for (int j = 0; j < cols; ++j) {
                    if (!predicate.test(i, j)) {
                        return false;
                    }
                }
            }

            return true;
        }

        /**
         * Returns whether no position of this dimension match the provided
         * predicate. May not evaluate the predicate on all positions if not
         * necessary for determining the result.  If the dimension is empty then
         * {@code true} is returned and the predicate is not evaluated.
         *
         * @param predicate predicate to apply to positions of this dimension
         * @return {@code true} if either no position of the dimension match the
         *         provided predicate or the dimension is empty, otherwise
         *         {@code false}
         */
        public boolean nonMatch(final IntIntPredicate predicate) {
            requireNonNull(predicate);

            for (int i = 0; i < rows; ++i) {
                for (int j = 0; j < cols; ++j) {
                    if (predicate.test(i, j)) {
                        return false;
                    }
                }
            }

            return true;
        }

        @Override
        public String toString() {
            return "[%s x %s]".formatted(rows(), cols());
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

        /**
         * Create a new row-major {@link Order} object for the given matrix
         * dimension. This is the default implementation for the element order
         * of the matrix.
         *
         * @param dim the matrix dimension
         * @return a new row-major {@link Order} object
         */
        public static RowMajor of(final Dim dim) {
            return new RowMajor(0, 0, dim.cols(), 1);
        }
    }

    /**
     * Factory interface for creating 2-d matrices.
     *
     * @param <M> the matrix type created by the factory
     */
    @FunctionalInterface
    interface Factory<M extends Matrix2d<M>> {

        /**
         * Create a new matrix with the given {@code structure}.
         *
         * @param structure the structure of the new matrix
         * @return a new matrix with the given {@code structure}
         */
        M newMatrix(final Structure structure);

        /**
         * Create a new matrix with the given {@code dimension} and default
         * <em>order</em>.
         *
         * @param dim the dimension of the created array
         * @return a new matrix with the given {@code dimension}
         */
        default M newMatrix(final Dim dim) {
            return newMatrix(new Structure(dim, RowMajor.of(dim)));
        }

        /**
         * Create a new matrix with the given number of {@code rows} and
         * {@code cols}.
         *
         * @param rows the number of rows of the created matrix
         * @param cols the number of columns of the created matrix
         * @return a new matrix with the given size
         */
        default M newMatrix(final int rows, final int cols) {
            return newMatrix(new Dim(rows, cols));
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
     * Return a matrix factory which is able to creates matrices from the same
     * kind.
     *
     * @return a matrix factory which is able to creates matrices from the same
     *        kind
     */
    Factory<M> factory();


    /**
     * Return the dimension of {@code this} 2-d matrix.
     *
     * @return the dimension of {@code this} 2-d matrix
     */
    default Dim dim() {
        return structure().dim();
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
        return dim().size();
    }

    /**
     * Return the number of rows of {@code this} matrix.
     *
     * @return the number of rows of {@code this} matrix
     */
    default int rows() {
        return dim().rows();
    }

    /**
     * Return the number of columns of {@code this} matrix.
     *
     * @return the number of columns of {@code this} matrix
     */
    default int cols() {
        return dim().cols();
    }

}
