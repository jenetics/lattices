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
 * Defines a 2-d structure, which is defined by the extent of the structure and
 * the layout of the underlying 1-d structure. This is the main class for working
 * with 2-d structures (latices/grids). The {@link View2d} and {@link Projection2d}
 * functions are used for <em>manipulating</em> this structure object.
 *
 * <pre>{@code
 * // Creating a new structure with the given extent.
 * final var structure = new Structure2d(500, 1000);
 * }</pre>
 *
 * @param extent the extent of the structure
 * @param layout the element order
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record Structure2d(Extent2d extent, Layout2d layout)
    implements Mapper2d
{

    public Structure2d {
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
    public Structure2d(Extent2d extent) {
        this(
            extent,
            new Layout2d(
                Index2d.ZERO,
                new Stride2d(
                    extent.cols()*extent.bands(),
                    extent.bands()
                ),
                Band.ZERO
            )
        );
    }

    /**
     * Create a new matrix structure with the given dimension and the default
     * element order. This is the usual way of creating instances of structure
     * objects.
     *
     * @param rows the number of rows
     * @param cols the number of columns
     */
    public Structure2d(int rows, int cols) {
        this(new Extent2d(rows, cols));
    }

    /**
     * Return the position of the given coordinate within the (virtual or
     * non-virtual) internal 1-d array.
     *
     * @param row the row index
     * @param col the column index
     * @return the (linearized) index of the given {@code row} and {@code col}
     * @throws IndexOutOfBoundsException if the given index values are out of
     *         bounds
     */
    @Override
    public int offset(int row, int col) {
        Objects.checkIndex(row, extent.rows());
        Objects.checkIndex(col, extent.cols());

        return layout.offset(row, col);
    }

    /**
     * Return the <em>array</em> index from the given <em>dimensional</em> index.
     *
     * @param index the dimensional index
     * @return the array index
     * @throws IndexOutOfBoundsException if the given index value is out of
     *         bounds
     */
    @Override
    public int offset(Index2d index) {
        return offset(index.row(), index.col());
    }

    /**
     * Calculates the index for the given {@code offset}. This is the
     * <em>inverse</em> operation of the {@link #offset(Index2d)} method.
     *
     * @param offset the offset for which to calculate the index
     * @return the index for the given {@code offset}
     * @throws IndexOutOfBoundsException if the index, represented by the offset,
     *         is out of bounds
     */
    @Override
    public Index2d index(int offset) {
        final var index = layout.index(offset);
        Objects.checkIndex(index.row(), extent.rows());
        Objects.checkIndex(index.col(), extent.cols());

        return index;
    }

}
