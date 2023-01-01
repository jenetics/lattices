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
 * Defines the structure of a 1-d matrix, which is defined by the dimension of
 * the matrix and the index order of the underlying element array.
 *
 * @param extent the extent of the structure
 * @param order the element order
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record Structure1d(Extent1d extent, Order1d order) {

    public Structure1d {
        requireNonNull(extent);
        requireNonNull(order);
    }

    /**
     * Create a new matrix structure with the given dimension and the default
     * element order.
     *
     * @param extent the extent of the structure
     */
    public Structure1d(final Extent1d extent) {
        this(extent, Order1d.DEFAULT);
    }

}
