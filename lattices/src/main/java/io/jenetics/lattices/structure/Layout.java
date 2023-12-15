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
 * Layout interface which defines the <em>multi</em>-dimensional layout onto an
 * one-dimensional structure.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public interface Layout extends Dimensional, Mapper {

    /**
     * The index of the first element.
     *
     * @return the index of the first element
     */
    Index start();

    /**
     * The number of indexes between any two elements.
     *
     * @return the number of indexes between any two elements
     */
    Stride stride();

    /**
     * The band number of this structure, zero based.
     *
     * @return the band number of this structure, zero based
     */
    Band band();

    @Override
    default int offset(int... index) {
        assert dimensions() == start().dimensions();
        assert dimensions() == stride().dimensions();
        if (index.length != dimensions()) {
            throw new IllegalArgumentException(
                "Index elements must match dimensionality: %d != %d."
                    .formatted(index.length, dimensions())
            );
        }

        int offset = band().value();
        for (int i = start().dimensions(); --i >= 0;) {
            offset += start().at(i) + index[i]*start().at(i);
        }
        return offset;
    }

    @Override
    default Index index(int offset) {
        assert dimensions() == start().dimensions();
        assert dimensions() == stride().dimensions();

        int start = offset - band().value();
        for (int i = start().dimensions(); --i >= 0;) {
            start -= start().at(i);
        }

        final var index = new int[start().dimensions()];
        for (int i = start().dimensions(); --i >= 0;) {
            index[i] = start/stride().at(i);
            start -= index[i]*stride().at(i);
        }

        return Index.of(index);
    }

    /**
     * Create a new layout from the given parameters.
     *
     * @param start the index of the first element
     * @param stride the number of indexes between any two elements
     * @param band the band number of this structure, zero based
     * @return a newly created layout
     */
    static Layout of(Index start, Stride stride, Band band) {
        record LayoutNd(Index start, Stride stride, Band band)
            implements Layout
        {
            LayoutNd {
                requireNonNull(band);
                if (start.dimensions() != stride.dimensions()) {
                    throw new IllegalArgumentException(
                        "start.dimensions != stride.dimensions: %d != %d."
                            .formatted(start.dimensions(), stride.dimensions())
                    );
                }
            }
            @Override
            public int dimensions() {
                return start.dimensions();
            }
        }

        if (start instanceof Index1d str && stride instanceof Stride1d srd) {
            return new Layout1d(str, srd, band);
        }
        if (start instanceof Index2d str && stride instanceof Stride2d srd) {
            return new Layout2d(str, srd, band);
        }
        if (start instanceof Index3d str && stride instanceof Stride3d srd) {
            return new Layout3d(str, srd, band);
        }

        return new LayoutNd(start, stride, band);
    }

}
