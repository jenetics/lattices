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
package io.jenetics.lattices.grid;

/**
 * Factory interface for creating 1-d matrices.
 *
 * @param <T> the type created by the factory
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
@FunctionalInterface
public interface Factory1d<T> {

    /**
     * Create a new matrix with the given {@code structure}.
     *
     * @param structure the structure of the new matrix
     * @return a new matrix with the given {@code structure}
     */
    T create(final Structure1d structure);

    /**
     * Create a new matrix with the given {@code dimension} and default
     * <em>order</em>.
     *
     * @param dim the dimension of the created array
     * @return a new matrix with the given {@code dimension}
     */
    default T create(final Extent1d dim) {
        return create(new Structure1d(dim));
    }

    /**
     * Create a new matrix with the given {@code size}.
     *
     * @param size the number of matrix elements
     * @return a new matrix with the given size
     */
    default T create(final int size) {
        return create(new Extent1d(size));
    }
}
