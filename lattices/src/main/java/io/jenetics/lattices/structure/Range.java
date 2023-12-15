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
     * The extent of the range.
     *
     * @return the extent of the range
     */
    Extent extent();

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
                if (start.dimensions() != extent.dimensions()) {
                    throw new IllegalArgumentException(
                        "start.dimensions != extent.dimensions: %d != %d."
                            .formatted(start.dimensions(), extent.dimensions())
                    );
                }
            }
            @Override
            public int dimensions() {
                return start.dimensions();
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

}
