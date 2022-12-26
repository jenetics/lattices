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
public record StrideOrder2d(Index2d start, Stride2d stride) {

    /**
     * Create a new row-major order object for the given dimension.
     *
     * @param extent the structure extent
     */
    public StrideOrder2d(final Extent2d extent) {
        this(Index2d.ZERO, new Stride2d(extent.cols(), 1));
    }

    public StrideOrder2d(final Range2d range) {
        this(range.start(), new Stride2d(range.extent().cols(), 1));
    }

    public int index(final int row, final int col) {
        return start.row() + row*stride.row() + start.col() + col*stride.col();
    }

    public StrideOrder2d transpose() {
        return new StrideOrder2d(start, new Stride2d(stride.col(), stride.row()));
    }

}
