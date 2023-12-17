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
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
public record Range2d(Index2d start, Extent2d extent)
    implements Range, Iterable<Index2d>
{

    public Range2d {
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
    public Range2d(Index2d start, Index2d end) {
        this(
            start,
            new Extent2d(
                end.row() - start.row(),
                end.col() - start.col()
            )
        );
    }

    /**
     * Create a new range from the given extent.
     *
     * @param extent the extent of the new range
     */
    public Range2d(final Extent2d extent) {
        this(Index2d.ZERO, extent);
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
    public Iterator<Index2d> iterator() {
        @SuppressWarnings("unchecked")
        final var it = (Iterator<Index2d>)(Object)Range.iterator(this);
        return it;
        /*
        return new Iterator<>() {
            private final int limit = start.row() + extent.rows();
            private int rowCursor = start.row();
            private int colCursor = start.col();

            @Override
            public boolean hasNext() {
                return rowCursor < limit;
            }

            @Override
            public Index2d next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                final int nextRow = rowCursor;
                final int nextCol = colCursor;

                colCursor = nextCol + 1;
                if (colCursor >= start.col() + extent.cols()) {
                    colCursor = start.col();
                    rowCursor = nextRow + 1;
                }

                return new Index2d(nextRow, nextCol);
            }
        };
         */
    }

    /**
     * Return a new index stream from {@code this} range.
     *
     * @return a new index stream from {@code this} range
     */
    public Stream<Index2d> indexes() {
        return StreamSupport.stream(spliterator(), false);
    }

    @Override
    public String toString() {
        return "[%d..%d, %d..%d]".formatted(
            start.row(), start.row() + extent.rows(),
            start.col(), start.col() + extent.cols());
    }

}
