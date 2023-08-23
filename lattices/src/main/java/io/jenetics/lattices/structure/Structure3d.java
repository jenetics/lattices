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
 * Defines a 3-d structure, which is defined by the extent of the structure and
 * the index oder of the underlying 1-d structure. The {@link View3d} and
 * {@link Projection3d} functions are used for <em>manipulating</em> this
 * structure object.
 *
 * <pre>{@code
 * // Creating a new structure with the given extent.
 * final var structure = new Structure3d(500, 1000, 50);
 * }</pre>
 *
 * @param extent the extent of the structure
 * @param layout the element order
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record Structure3d(Extent3d extent, Layout3d layout)
    implements OffsetMapper3d
{

    public Structure3d {
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
    public Structure3d(Extent3d extent) {
        this(
            extent,
            new Layout3d(
                Index3d.ZERO,
                new Stride3d(
                    extent.rows()*extent.cols()*extent.bands(),
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
     * @param slices the number of slices
     * @param rows the number of rows
     * @param cols the number of columns
     */
    public Structure3d(int slices, int rows, int cols) {
        this(new Extent3d(slices, rows, cols));
    }

    /**
     * Return the position of the given coordinate within the (virtual or
     * non-virtual) internal 1-d array.
     *
     * @param slice the slice index
     * @param row the row index
     * @param col the column index
     * @return the (linearized) index of the given {@code slice}, {@code row}
     *         and {@code col}
     * @throws IndexOutOfBoundsException if the given index values are out of
     *         bounds
     */
    @Override
    public int offset(int slice, int row, int col) {
        Objects.checkIndex(slice, extent.slices());
        Objects.checkIndex(row, extent.rows());
        Objects.checkIndex(col, extent.cols());

        return layout.offset(slice, row, col);
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
    public int offset(Index3d index) {
        return offset(index.slice(), index.row(), index.col());
    }

    /**
     * Calculates the index for the given {@code offset}. This is the
     * <em>inverse</em> operation of the {@link #offset(Index3d)} method.
     *
     * @param offset the offset for which to calculate the index
     * @return the index for the given {@code offset}
     * @throws IndexOutOfBoundsException if the index, represented by the offset,
     *         is out of bounds
     */
    @Override
    public Index3d index(int offset) {
        final var index = layout.index(offset);
        Objects.checkIndex(index.slice(), extent.slices());
        Objects.checkIndex(index.row(), extent.rows());
        Objects.checkIndex(index.col(), extent.cols());

        return index;
    }

}
