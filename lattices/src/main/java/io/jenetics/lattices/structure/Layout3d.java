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
 * This class defines the layout of the 3-d data onto the 1-d array like data
 * structure. The layout is defined by the 3-d start index and the 3-d strides.
 *
 * @apiNote
 * Note, that the direct manipulation/creation of the <em>layout</em> object
 * usually doesn't lead to the expected result. It is expected that layouts
 * are created by the <em>structure</em> object; {@link Structure3d#of(Extent3d)}.
 *
 * @see Structure3d
 *
 * @param start the start index of the first element
 * @param stride the element strides
 * @param band the band number of this structure, zero based
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record Layout3d(Index3d start, Stride3d stride, Band band) {

    public Layout3d {
        requireNonNull(start);
        requireNonNull(stride);
        requireNonNull(band);
    }

    public Layout3d(Index3d start, Stride3d stride) {
        this(start, stride, Band.ZERO);
    }

    /**
     * Return the position of the given coordinate within the (virtual or
     * non-virtual) internal 1-d array.
     * <em>This method doesn't do any range checks.</em>
     *
     * @param slice the slice index
     * @param row the row index
     * @param col the column index
     * @return the (linearized) index of the given {@code slice}, {@code row}
     *         and {@code col}
     */
    public int offset(int slice, int row, int col) {
        return
            start.slice() + slice*stride.slice() +
            start.row() + row*stride.row() +
            start.col() + col*stride.col() +
            band.value();
    }

    /**
     * Return the <em>array</em> index from the given <em>dimensional</em> index.
     * <em>This method doesn't do any range checks.</em>
     *
     * @param index the dimensional index
     * @return the array index
     */
    public int offset(Index3d index) {
        return offset(index.slice(), index.row(), index.col());
    }

    /**
     * Calculates the index for the given {@code offset}. This is the
     * <em>inverse</em> operation of the {@link #offset(Index3d)} method.
     * <em>This method doesn't do any range checks.</em>
     *
     * @param offset the offset for which to calculate the index
     * @return the index for the given {@code offset}
     */
    public Index3d index(int offset) {
        int start = offset -
            this.start.slice() -
            this.start.row() -
            this.start.col() -
            band.value();

        final int slice = start/stride.slice();
        start = start - slice*stride.slice();

        final int row = start/stride.row();
        start = start - row*stride.row();

        final int col = start/stride.col();

        return new Index3d(slice, row, col);
    }

}
