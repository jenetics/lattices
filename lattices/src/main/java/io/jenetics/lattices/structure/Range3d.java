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
 * Represents a <em>grid</em> range with the given parameters.
 *
 * @param start the range start
 * @param extent the extent of the range
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record Range3d(Index3d start, Extent3d extent) {

    public Range3d {
        requireNonNull(start);
        requireNonNull(extent);
    }

    /**
     * Create a new range object with the given {@code start} and {@code end}
     * index.
     *
     * @param start the start index, inclusively
     * @param end the end index, exclusively
     * @throws IllegalArgumentException if {@code start >= end}
     */
    public Range3d(final Index3d start, final Index3d end) {
        this(
            start,
            new Extent3d(
                end.slice() - start.slice(),
                end.row() - start.row(),
                end.col() - start.col())
        );
    }

    /**
     * Create a new range from the given extent.
     *
     * @param extent the extent of the new range
     */
    public Range3d(final Extent3d extent) {
        this(Index3d.ZERO, extent);
    }

    /**
     * Return the number of elements of this range.
     *
     * @return the number of elements of this range
     */
    public int size() {
        return extent.size();
    }

    @Override
    public String toString() {
        return "[%d..%d, %d..%d, %d..%d]".formatted(
            start.row(), start.row() + extent.rows(),
            start.col(), start.col() + extent.cols(),
            start.slice(), start.slice() + extent.slices()
        );
    }

}
