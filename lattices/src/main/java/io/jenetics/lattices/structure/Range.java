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
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import io.jenetics.lattices.structure.IndexIterator.LowMajor;

/**
 * Defines a range by its start index and extent.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public interface Range extends Dimensional {

    /**
     * The start index of the range.
     *
     * @return the start index of the range
     */
    Index start();

    /**
     * Return the end index of the range (exclusively).
     *
     * @return the end index of the range (exclusively)
     */
    default Index end() {
        final var end = start().toArray();
        for (int i = 0; i < end.length; ++i) {
            end[i] += extent().at(i);
        }
        return Index.of(end);
    }

    /**
     * The extent of the range.
     *
     * @return the extent of the range
     */
    Extent extent();

    default List<Range> partition(int... parts) {
        return List.of();
    }

    /**
     * Return an index iterator for the given {@code range}.
     *
     * @param range the range used for creating the iterator
     * @return an index iterator for the given {@code range}
     */
    static Iterator<Index> iterator(Range range) {
        return LowMajor.forward(range);
    }

    /**
     * Return an index iterable for the given {@code range}.
     *
     * @param range the range used for creating the iterable
     * @return an index iterable for the given {@code range}
     */
    static Iterable<Index> iterable(Range range) {
        requireNonNull(range);
        return () -> iterator(range);
    }

    /**
     * Return an index stream for the given {@code range}.
     *
     * @param range the range used for creating the stream
     * @return an index stream for the given {@code range}
     */
    static Stream<Index> indexes(Range range) {
        return StreamSupport.stream(iterable(range).spliterator(), false);
    }

    /**
     * Create a new range, defined by the start index and the extent.
     *
     * @param start the start index of the range
     * @param extent the extent of the range
     * @return a new range, defined by the start index and the extent
     */
    static Range of(Index start, Extent extent) {
        record RangeNd(Index start, Extent extent) implements Range {
            RangeNd {
                if (start.dimensionality() != extent.dimensionality()) {
                    throw new IllegalArgumentException(
                        "start.dimensionality != extent.dimensionality: %d != %d."
                            .formatted(start.dimensionality(), extent.dimensionality())
                    );
                }
            }
            @Override
            public int dimensionality() {
                return start.dimensionality();
            }
        }

        if (start instanceof Index1d str && extent instanceof Extent1d ext) {
            return new Range1d(str, ext);
        }
        if (start instanceof Index2d str && extent instanceof Extent2d ext) {
            return new Range2d(str, ext);
        }
        if (start instanceof Index3d str && extent instanceof Extent3d ext) {
            return new Range3d(str, ext);
        }

        return new RangeNd(start, extent);
    }

    /**
     * Create a new range, defined by the extent.
     *
     * @param extent the extent of the range
     * @return a new range, defined by the extent
     */
    static Range of(Extent extent) {
        return Range.of(Index.of(new int[extent.dimensionality()]), extent);
    }

}
