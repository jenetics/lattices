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

import io.jenetics.linealgebra.structure.Extent2d;
import io.jenetics.linealgebra.structure.StrideOrder2d;
import io.jenetics.linealgebra.structure.Structural2d;
import io.jenetics.linealgebra.structure.Structure2d;

/**
 * This interface defines the structure for 2-d matrices holding objects or
 * primitive data types.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
public interface Matrix2d<M extends Matrix2d<M>>
    extends Matrix<M>, Structural2d
{

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
        M newMatrix(final Structure2d structure);

        /**
         * Create a new matrix with the given {@code dimension} and default
         * <em>order</em>.
         *
         * @param dim the dimension of the created array
         * @return a new matrix with the given {@code dimension}
         */
        default M newMatrix(final Extent2d dim) {
            return newMatrix(new Structure2d(dim, new StrideOrder2d(dim)));
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
            return newMatrix(new Extent2d(rows, cols));
        }

    }

    /**
     * Return a new view of the underlying element array with the given
     * {@code structure}. The data are unchanged and not copied.
     *
     * @param structure the structure definition of the data array
     * @return a new view of the underlying element array
     */
    M view(final Structure2d structure);

    /**
     * Return a new minimal copy of the underlying element array with the given
     * {@code structure}.
     *
     * @param structure the structure definition of the data array
     * @return a new minimal copy of the underlying element array
     */
    M copy(final Structure2d structure);

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

}
