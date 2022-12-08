/*
 * Java Linear Algebra Library (@__identifier__@).
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
package io.jenetics.linealgebra.structure;

/**
 * Factory interface for creating 2-d matrices.
 *
 * @param <T> the type created by the factory
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
@FunctionalInterface
public interface Factory2d<T> {

    /**
     * Create a new matrix with the given {@code structure}.
     *
     * @param structure the structure of the new structure
     * @return a new structure with the given {@code structure}
     */
    T newInstance(final Structure2d structure);

    /**
     * Create a new structure with the given {@code extent} and default
     * <em>order</em>.
     *
     * @param extent the extent of the created structure
     * @return a new structure with the given {@code extent}
     */
    default T newInstance(final Extent2d extent) {
        return newInstance(new Structure2d(extent, new StrideOrder2d(extent)));
    }

    /**
     * Create a new matrix with the given number of {@code rows} and
     * {@code cols}.
     *
     * @param rows the number of rows of the created structure
     * @param cols the number of columns of the created structure
     * @return a new matrix with the given size
     */
    default T newInstance(final int rows, final int cols) {
        return newInstance(new Extent2d(rows, cols));
    }

}
