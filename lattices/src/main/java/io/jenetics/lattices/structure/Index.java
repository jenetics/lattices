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

/**
 * Base interface of <em>index</em> classes.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public interface Index extends DiscreteSpatial {

    /**
     * Create a new index object from the given index values. The values for the
     * index coordinates are given in <em>reverse</em> order:
     * {@code [x4, x3, x2, x1, x0]}. This conforms with the usual way to access
     * <em>matrix</em> elements; {@code [row, col]}.
     *
     * @param indexes the index values
     * @return a new index object with the given values
     */
    static Index of(int... indexes) {
        record IndexNd(int... indexes) implements Index {
            IndexNd {
                if (indexes.length == 0) {
                    throw new IllegalArgumentException(
                        "Dimensionality must not be zero."
                    );
                }
            }
            @Override
            public int dimensionality() {
                return indexes.length;
            }
            @Override
            public int at(int dimension) {
                return indexes[dimension];
            }
            @Override
            public int[] toArray() {
                return indexes.clone();
            }
            @Override
            public int hashCode() {
                return Arrays.hashCode(indexes);
            }
            @Override
            public boolean equals(Object obj) {
                return obj instanceof IndexNd idx &&
                    Arrays.equals(indexes, idx.indexes);
            }
            @Override
            public String toString() {
                return Arrays.toString(indexes);
            }
        }

        return switch (indexes.length) {
            case 1 -> new Index1d(indexes[0]);
            case 2 -> new Index2d(indexes[0], indexes[1]);
            case 3 -> new Index3d(indexes[0], indexes[1], indexes[2]);
            default -> new IndexNd(indexes);
        };
    }

}
