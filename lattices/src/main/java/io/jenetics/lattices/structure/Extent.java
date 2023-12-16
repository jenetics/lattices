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

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Base interface for extents.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public interface Extent extends Spatial {

    /**
     * Return the number of elements this extent contains.
     *
     * @return the number of elements
     */
    default int elements() {
        int elements = 1;
        for (int i = dimensionality(); --i >= 0;) {
            elements *= at(i);
        }
        return elements;
    }

    /**
     * Return the number of bands
     *
     * @return the number of bands
     */
    int bands();

    /**
     * Return the length of the array, needed for storing all cells:
     * {@code elements()*bands()}.
     *
     * @return the array length needed for storing all cells
     */
    default int cells() {
        return elements()*bands();
    }

    /**
     * Return an index iterator for the given {@code extent}.
     *
     * @param extent the extent used for creating the iterator
     * @return an index iterator for the given {@code extent}
     */
    static Iterator<Index> iterator(Extent extent) {
        return Range.iterator(Range.of(extent));
    }

    /**
     * Return an index stream for the given {@code extent}.
     *
     * @param extent the extent used for creating the stream
     * @return an index stream for the given {@code extent}
     */
    static Stream<Index> indexes(Extent extent) {
        return Range.indexes(Range.of(extent));
    }

    /**
     * Create a new extent from the given extent values and bands.
     *
     * @param extents the extent values
     * @param bands the number of bands
     * @return a new extent object
     * @throws IllegalArgumentException if one of the arguments is smaller than
     *         zero or {@code mult(extents)*bands > Integer.MAX_VALUE}
     */
    static Extent of(int[] extents, int bands) {
        record ExtentNd(int[] extents, int bands) implements Extent {
            ExtentNd {
                if (extents.length == 0) {
                    throw new IllegalArgumentException(
                        "Dimensionality must not be zero."
                    );
                }
                if (IntStream.of(extents).anyMatch(i -> i < 0) ||
                    bands < 1 ||
                    Checks.multNotSave(bands, extents))
                {
                    throw new IllegalArgumentException(
                        "Extent is out of bounds: [%s, bands=%d]."
                            .formatted(Arrays.toString(extents), bands)
                    );
                }
            }
            @Override
            public int dimensionality() {
                return extents.length;
            }
            @Override
            public int at(int dimension) {
                return extents[dimension];
            }
            @Override
            public int[] toArray() {
                return extents.clone();
            }
            @Override
            public int hashCode() {
                return Objects.hash(Arrays.hashCode(extents), bands);
            }
            @Override
            public boolean equals(Object obj) {
                return obj instanceof ExtentNd ext &&
                    bands == ext.bands &&
                    Arrays.equals(extents, ext.extents);
            }
            @Override
            public String toString() {
                return Arrays.toString(extents);
            }
        }

        return switch (extents.length) {
            case 1 -> new Extent1d(extents[0], bands);
            case 2 -> new Extent2d(extents[0], extents[1], bands);
            case 3 -> new Extent3d(extents[0], extents[1], extents[2], bands);
            default -> new ExtentNd(extents, bands);
        };
    }

    /**
     * Create a new extent from the given extent values and bands.
     *
     * @param extents the extent values
     * @return a new extent object
     * @throws IllegalArgumentException if one of the arguments is smaller than
     *         zero or {@code mult(extents) > Integer.MAX_VALUE}
     */
    static Extent of(int... extents) {
        return of(extents, 1);
    }

}
