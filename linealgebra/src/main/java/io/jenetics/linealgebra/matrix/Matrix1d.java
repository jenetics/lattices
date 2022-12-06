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

/**
 * This interface defines the structure for 1-d matrices holding objects or
 * primitive data types.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
public interface Matrix1d<M extends Matrix1d<M>> extends Matrix<M> {

    /**
     * Defines the structure of a 1-d matrix, which is defined by the dimension
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
            this(dim, RowMajor.DEFAULT);
        }
    }

    /**
     * The dimension of the {@link Matrix1d} object.
     *
     * @param size the number of elements
     */
    record Dim(int size) {
        public Dim {
            if (size < 0) {
                throw new IllegalArgumentException(
                    "Size must greater or equal than zero: " + size
                );
            }
        }

        @Override
        public String toString() {
            return "[%s]".formatted(size());
        }
    }

    /**
     * Represents the order for accessing the linearly stored matrix data.
     */
    @FunctionalInterface
    interface Order {

        /**
         * Return the position of the element with the given relative
         * {@code rank} within the (virtual or non-virtual) internal 1-d array.
         *
         * @param rank the rank of the element.
         * @return the (linearized) index of the given {@code rank}
         */
        int index(final int rank);

    }

    /**
     * Represents the <em>row-major</em> order.
     *
     * @param zero the index of the first element
     * @param stride the number of indexes between any two elements
     */
    record RowMajor(int zero, int stride)
        implements Order
    {

        public static final RowMajor DEFAULT = new RowMajor(0, 1);

        @Override
        public int index(final int rank) {
            return zero + rank*stride;
        }
    }

    /**
     * Factory interface for creating 2-d matrices.
     *
     * @param <M> the matrix type created by the factory
     */
    @FunctionalInterface
    interface Factory<M extends Matrix1d<M>> {

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
            return newMatrix(new Structure(dim));
        }

        /**
         * Create a new matrix with the given {@code size}.
         *
         * @param size the number of matrix elements
         * @return a new matrix with the given size
         */
        default M newMatrix(final int size) {
            return newMatrix(new Dim(size));
        }
    }

    /**
     * Return the structure of {@code this} 1-d matrix.
     *
     * @return the structure of {@code this} 1-d matrix
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
     * Return the dimension of {@code this} 1-d matrix.
     *
     * @return the dimension of {@code this} 1-d matrix
     */
    default Dim dim() {
        return structure().dim();
    }

    /**
     * Return the defined order of {@code this} 1-d matrix.
     *
     * @return the defined order of {@code this} 1-d matrix
     */
    default Order order() {
        return structure().order();
    }

    @Override
    default int size() {
        return dim().size();
    }

}
