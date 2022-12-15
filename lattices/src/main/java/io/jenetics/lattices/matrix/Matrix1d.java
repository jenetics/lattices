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

import io.jenetics.lattices.grid.Extent1d;
import io.jenetics.lattices.grid.Factory1d;
import io.jenetics.lattices.grid.Grid1d;
import io.jenetics.lattices.grid.Range1d;
import io.jenetics.lattices.grid.Stride1d;
import io.jenetics.lattices.grid.Structure1d;

/**
 * This interface defines the structure for 1-d matrices holding objects or
 * primitive data types.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public interface Matrix1d<M extends Matrix1d<M>>
    extends Matrix<M>, Grid1d
{

    /**
     * Return a matrix factory which is able to creates matrices from the same
     * kind.
     *
     * @return a matrix factory which is able to creates matrices from the same
     *        kind
     */
    Factory1d<M> factory();

    /**
     * Return a new matrix which is like this one, but with the given new
     * {@code structure}.
     *
     * @param structure the structure of the new matrix
     * @return a new matrix which is like this one
     */
    default M like(final Structure1d structure) {
        return factory().create(structure);
    }

    /**
     * Return a new matrix which is like this one, but with the given
     * {@code extent}.
     *
     * @param extent the extent of the created matrix
     * @return a new matrix which is like this one
     */
    default M like(final Extent1d extent) {
        return factory().create(extent);
    }

    /**
     * Return a new matrix which is like this one, but with the given
     * {@code extent}.
     *
     * @param size the number of elements
     * @return a new matrix which is like this one
     */
    default M like(final int size) {
        return factory().create(size);
    }

    @Override
    default M like() {
        return like(structure().like());
    }

    /**
     * Return a new view of the underlying element array with the given
     * {@code structure}. The data are unchanged and not copied.
     *
     * @param structure the structure definition of the data array
     * @return a new view of the underlying element array
     */
    M view(final Structure1d structure);

    /**
     * Return a new view of this matrix for the given {@code range}.
     *
     * @param range the range of the returned view
     * @return a new view of the underlying element array
     */
    default M view(final Range1d range) {
        return view(structure().view(range));
    }

    /**
     * Return a new view of this matrix for the given {@code extent}.
     *
     * @param extent the extent of the returned view
     * @return a new view of the underlying element array
     */
    default M view(final Extent1d extent) {
        return view(new Range1d(extent));
    }

    /**
     * Return a new view of this matrix for the given {@code stride}.
     *
     * @param stride the range of the returned view
     * @return a new view of the underlying element array
     */
    default M view(final Stride1d stride) {
        return view(structure().view(stride));
    }

    /**
     * Return a new minimal copy of the underlying element array with the given
     * {@code range}.
     *
     * @param range the range definition of the data array
     * @return a new minimal copy of the underlying element array
     */
    M copy(final Range1d range);

    @Override
    default M copy() {
        return copy(new Range1d(extent()));
    }



}
