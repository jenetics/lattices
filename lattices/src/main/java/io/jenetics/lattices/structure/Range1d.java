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

import java.util.Iterator;

/**
 * Represents a <em>grid</em> range with the given parameters.
 *
 * @param start the start index of the range
 * @param extent the size of the range
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record Range1d(Index1d start, Extent1d extent)
    implements Iterable<Index1d>
{

    public Range1d {
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
    public Range1d(Index1d start, Index1d end) {
        this(start, new Extent1d(end.value() - start.value()));
    }

    /**
     * Create a new range object with the given {@code start} and {@code end}
     * index.
     *
     * @param start the start index, inclusively
     * @param end the end index, exclusively
     * @throws IllegalArgumentException if {@code start >= end}
     */
    public Range1d(int start, int end) {
        this(new Index1d(start), new Index1d(end));
    }

    /**
     * Create a new range from the given extent. The start indices ({@link #start}
     * is set to zero.
     *
     * @param extent the extent of the new range
     */
    public Range1d(Extent1d extent) {
        this(Index1d.ZERO, extent);
    }

    @Override
    public Iterator<Index1d> iterator() {
        return new Index1dIterator(this, Stride1d.ONE);
    }

    @Override
    public String toString() {
        return "[%d..%d]".formatted(
            start.value(),
            start.value() + extent.elements()
        );
    }

}
