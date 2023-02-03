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

import io.jenetics.lattices.structure.Extent3d;
import io.jenetics.lattices.structure.Layout3d;
import io.jenetics.lattices.structure.Structure3d;

/**
 * Factory interface for creating 3-d structures.
 *
 * @param <T> the type created by the factory
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
@FunctionalInterface
public interface Factory3d<T> {

    /**
     * Create a new matrix with the given {@code structure}.
     *
     * @param structure the structure of the new structure
     * @return a new structure with the given {@code structure}
     */
    T create(final Structure3d structure);

    /**
     * Create a new structure with the given {@code extent} and default
     * <em>order</em>.
     *
     * @param extent the extent of the created structure
     * @return a new structure with the given {@code extent}
     */
    default T create(final Extent3d extent) {
        return create(new Structure3d(extent, new Layout3d(extent)));
    }

    /**
     * Create a new matrix with the given number of {@code rows} and
     * {@code cols}.
     *
     * @param rows the number of rows of the created structure
     * @param cols the number of columns of the created structure
     * @return a new matrix with the given size
     */
    default T create(final int slices, final int rows, final int cols) {
        return create(new Extent3d(slices, rows, cols));
    }

}
