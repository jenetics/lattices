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
 * are created by the <em>structure</em> object; {@link Structure3d#Structure3d(Extent3d)}.
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
public record Layout3d(Index3d start, Stride3d stride, Band band)
    implements Layout, Mapper3d
{

    public Layout3d {
        requireNonNull(start);
        requireNonNull(stride);
        requireNonNull(band);
    }

    /**
     * Return the number of dimensions; always 3.
     *
     * @return 3
     */
    @Override
    public int dimensionality() {
        return 3;
    }

    @Override
    public int offset(int slice, int row, int col) {
        return
            start.slice() + slice*stride.slice() +
            start.row() + row*stride.row() +
            start.col() + col*stride.col() +
            band.value();
    }

    @Override
    public int offset(int... index) {
        if (index.length != dimensionality()) {
            throw new IllegalArgumentException(
                "Index elements must match dimensionality: %d != %d."
                    .formatted(index.length, dimensionality())
            );
        }
        return offset(index[0], index[1], index[2]);
    }

    @Override
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
