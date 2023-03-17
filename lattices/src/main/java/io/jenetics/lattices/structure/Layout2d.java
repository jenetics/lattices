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

/**
 * This class defines the layout of the 2-d data onto the 1-d array like data
 * structure. The layout is defined by the 2-d start index and the 2-d strides.
 *
 * @apiNote
 * Note, that the direct manipulation/creation of the <em>layout</em> object
 * usually doesn't lead to the expected result. It is expected that layouts
 * are created by the <em>structure</em> object; {@link Structure2d#Structure2d(Extent2d)}.
 *
 * @see Structure2d
 *
 * @param start the start index of the first element
 * @param stride the element strides
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record Layout2d(Index2d start, Stride2d stride) {

    public Layout2d {
        requireNonNull(start);
        requireNonNull(stride);
    }

    /**
     * Create a new (row-major) order for the given range.
     *
     * @param range the range of the order
     */
    public Layout2d(Range2d range) {
        this(range.start(), new Stride2d(range.extent().cols(), 1));
    }

    /**
     * Create a new row-major order object for the given dimension.
     *
     * @param extent the structure extent
     */
    public Layout2d(Extent2d extent) {
        this(new Range2d(extent));
    }

    /**
     * Return the position of the given coordinate within the (virtual or
     * non-virtual) internal 1-d array.
     *
     * @param row the row index
     * @param col the column index
     * @return the (linearized) index of the given {@code row} and {@code col}
     */
    public int offset(int row, int col) {
        return
            start.row() + row*stride.row() +
            start.col() + col*stride.col();
    }

    /**
     * Return the <em>array</em> index from the given <em>dimensional</em> index.
     *
     * @param index the dimensional index
     * @return the array index
     */
    public int offset(Index2d index) {
        return offset(index.row(), index.col());
    }

    /**
     * Calculates the index for the given {@code offset}. This is the
     * <em>inverse</em> operation of the {@link #offset(Index2d)} method.
     *
     * @param offset the offset for which to calculate the index
     * @return the index for the given {@code offset}
     */
    public Index2d index(int offset) {
        final int start = offset -
            this.start.row() -
            this.start.col();

        final int row = start/stride.row();
        final int col = start - row*stride.row();

        return new Index2d(row, col);
    }

}
