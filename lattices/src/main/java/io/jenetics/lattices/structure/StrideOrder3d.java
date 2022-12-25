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
 * Implements a structure order by defining start indexes and strides.
 *
 * @param start the start index of the first element
 * @param stride the element strides
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record StrideOrder3d(Index3d start, Stride3d stride) implements Order3d {

    public StrideOrder3d {
        requireNonNull(start);
        requireNonNull(stride);
    }

    public StrideOrder3d(final Index3d start, final Extent3d extent) {
        this(
            start,
            new Stride3d(extent.rows()*extent.cols(), extent.cols(),  1)
        );
    }

    /**
     * Create a new stride-order object,
     *
     * @param extent the structure extent
     */
    public StrideOrder3d(final Extent3d extent) {
        this(Index3d.ZERO, extent);
    }

    @Override
    public int index(final int slice, final int row, final int col) {
        return
            start.slice() + slice*stride.slice() +
            start.row() + row*stride.row() +
            start.col() + col*stride.col();
    }

}
