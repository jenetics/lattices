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
package io.jenetics.lattices.structure;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

/**
 * Defines the structure of a 1-d matrix, which is defined by the dimension of
 * the matrix and the index order of the underlying element array. The
 * {@link View1d} function1 is used for <em>manipulating</em> this structure
 * object.
 *
 * <pre>{@code
 * // Creating a new structure with the given extent.
 * final var structure = new Structure1d(1000);
 * }</pre>
 *
 * @param extent the extent of the structure
 * @param layout the element layout
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record Structure1d(Extent1d extent, Layout1d layout)
    implements Mapper1d
{

    public Structure1d {
        requireNonNull(extent);
        requireNonNull(layout);
    }

    /**
     * Create a new matrix structure with the given dimension and the default
     * element order. This is the usual way of creating instances of structure
     * objects.
     *
     * @param extent the extent of the structure
     */
    public Structure1d(Extent1d extent) {
        this(
            extent,
            new Layout1d(
                Index1d.ZERO,
                new Stride1d(extent.bands()),
                Band.ZERO
            )
        );
    }

    /**
     * Create a new matrix structure with the given dimension and the default
     * element order. This is the usual way of creating instances of structure
     * objects.
     *
     * @param extent the extent of the structure
     */
    public Structure1d(int extent) {
        this(new Extent1d(extent));
    }

    /**
     * Return the position of the element with the given relative {@code rank}
     * within the (virtual or non-virtual) internal 1-d array.
     *
     * @param index the index of the element.
     * @return the (linearized) index of the given {@code index}
     * @throws IndexOutOfBoundsException if the given index value is out of
     *         bounds
     */
    @Override
    public int offset(int index) {
        Checks.checkIndex(index, extent);
        return layout.offset(index);
    }

    /**
     * Calculates the index for the given {@code offset}. This is the
     * <em>inverse</em> operation of the {@link #offset(Index1d)} method.
     *
     * @param offset the offset for which to calculate the index
     * @return the index for the given {@code offset}
     * @throws IndexOutOfBoundsException if the index, represented by the offset,
     *         is out of bounds
     */
    @Override
    public Index1d index(int offset) {
        final var index = layout.index(offset);
        Checks.checkIndex(index.value(), extent);
        return index;
    }

}
