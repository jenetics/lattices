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
 * @param band the band number of this structure, zero based
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record Layout2d(Index2d start, Stride2d stride, Band band)
    implements Layout, Mapper2d
{

    public Layout2d {
        requireNonNull(start);
        requireNonNull(stride);
        requireNonNull(band);
    }

    /**
     * Return the number of dimensions; always 2.
     *
     * @return 2
     */
    @Override
    public int dimensionality() {
        return 2;
    }

    @Override
    public int offset(int row, int col) {
        return
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
        return offset(index[0], index[1]);
    }

    @Override
    public Index2d index(int offset) {
        int start = offset -
            this.start.row() -
            this.start.col() -
            band.value();

        final int row = start/stride.row();
        start = start - row*stride.row();

        final int col = start/stride.col();

        return new Index2d(row, col);
    }

}
