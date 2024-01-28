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
 * Base interface of <em>stride</em> classes.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public interface Stride extends Spatial {

    /**
     * Create a new stride object from the given stride values. The values for
     * the strides are given in <em>reverse</em> order: {@code [s3, s2, s1, s0]}.
     * This conforms with the usual way to access <em>matrix</em> elements.
     *
     * @param strides the stride values
     * @return a new stride object with the given values
     */
    static Stride of(int... strides) {
        record StrideNd(int... strides) implements Stride {
            StrideNd {
                if (strides.length == 0) {
                    throw new IllegalArgumentException(
                        "Dimensionality must not be zero."
                    );
                }
                for (var stride : strides) {
                    if (stride < 1) {
                        throw new IndexOutOfBoundsException(
                            "Strides must be positive: [%s]."
                                .formatted(Arrays.toString(strides))
                        );
                    }
                }
            }
            @Override
            public int dimensionality() {
                return strides.length;
            }
            @Override
            public int at(int dimension) {
                return strides[dimension];
            }
            @Override
            public int[] toArray() {
                return strides.clone();
            }
            @Override
            public int hashCode() {
                return Arrays.hashCode(strides);
            }
            @Override
            public boolean equals(Object obj) {
                return obj instanceof StrideNd str &&
                    Arrays.equals(strides, str.strides);
            }
            @Override
            public String toString() {
                return "Stride" + Arrays.toString(strides);
            }
        }

        return switch (strides.length) {
            case 1 -> new Stride1d(strides[0]);
            case 2 -> new Stride2d(strides[0], strides[1]);
            case 3 -> new Stride3d(strides[0], strides[1], strides[2]);
            default -> new StrideNd(strides);
        };
    }

}
