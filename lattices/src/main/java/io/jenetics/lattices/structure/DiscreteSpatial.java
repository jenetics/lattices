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
 * Mixin interface for objects with discrete spatial properties.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public interface DiscreteSpatial extends Dimensional {

    /**
     * Return the discrete value for the given {@code dimension}.
     *
     * @param dimension the dimension (coordinate) value
     * @return the value of the given {@code dimension} (coordinate)
     * @throws IndexOutOfBoundsException if the given {@code dimension} is out
     *         of the valid range
     */
    int at(int dimension);

    /**
     * Return the spatial values as {@code int[]} array.
     *
     * @return a new {@code int[]} array with the dimensional components
     */
    default int[] toArray() {
        final var result = new int[dimensionality()];
        for (int i = dimensionality(); --i >= 0;) {
            result[i] = at(i);
        }
        return result;
    }

}
