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
package io.jenetics.lattices.matrix;

import io.jenetics.lattices.array.Array;
import io.jenetics.lattices.grid.Factory2d;
import io.jenetics.lattices.grid.Grid2d;
import io.jenetics.lattices.structure.Extent2d;
import io.jenetics.lattices.structure.Range2d;
import io.jenetics.lattices.structure.Stride2d;
import io.jenetics.lattices.structure.Structure2d;

/**
 * This interface defines the structure for 2-d matrices holding objects or
 * primitive data types.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public interface Matrix2d<A extends Array<A>, M extends Matrix2d<A, M>>
    extends Matrix<M>, Grid2d<A, M>
{

    /**
     * Return a matrix factory which is able to creates matrices from the same
     * kind.
     *
     * @return a matrix factory which is able to creates matrices from the same
     *        kind
     */
    Factory2d<M> factory();

    /**
     * Return a new matrix which is like this one, but with the given
     * {@code structure}.
     *
     * @param structure the structure of the created matrix
     * @return a new matrix which is like this one
     */
    default M like(final Structure2d structure) {
        return factory().create(structure);
    }

    /**
     * Return a new matrix which is like this one, but with the given
     * {@code extent}.
     *
     * @param extent the extent of the created matrix
     * @return a new matrix which is like this one
     */
    default M like(final Extent2d extent) {
        return factory().create(extent);
    }

    /**
     * Return a new matrix which is like this one, but with the given
     * {@code extent}.
     *
     * @param rows the number of rows of the created matrix
     * @param cols the number of columns of the created matrix
     * @return a new matrix which is like this one
     */
    default M like(final int rows, final int cols) {
        return factory().create(rows, cols);
    }

    @Override
    default M like() {
        return like(structure().like());
    }

    /**
     * Return a new view of this matrix for the given {@code range}.
     *
     * @param range the range of the returned view
     * @return a new view of the underlying element array
     */
    default M view(final Range2d range) {
        return view(structure().view(range));
    }

    /**
     * Return a new view of this matrix for the given {@code extent}.
     *
     * @param extent the extent of the returned view
     * @return a new view of the underlying element array
     */
    default M view(final Extent2d extent) {
        return view(new Range2d(extent));
    }

    /**
     * Return a new view of this matrix for the given {@code stride}.
     *
     * @param stride the range of the returned view
     * @return a new view of the underlying element array
     */
    default M view(final Stride2d stride) {
        return view(structure().view(stride));
    }

    /**
     * Return a new minimal copy of the underlying element array with the given
     * {@code range}.
     *
     * @param range the range definition of the data array
     * @return a new minimal copy of the underlying element array
     */
    M copy(final Range2d range);

    @Override
    default M copy() {
        return copy(new Range2d(extent()));
    }

    /**
     * Return a transposed view of this matrix.
     *
     * @return the transposed view of this matrix.
     */
    M transpose();

}
