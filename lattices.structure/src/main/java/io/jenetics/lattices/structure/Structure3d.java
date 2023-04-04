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
 * Defines a 3-d structure, which is defined by the extent of the structure and
 * the index oder of the underlying 1-d structure. The {@link View3d} and
 * {@link Projection3d} functions are used for <em>manipulating</em> this
 * structure object.
 *
 * <pre>{@code
 * // Creating a new structure with the given extent.
 * final var structure = Structure3d.of(new Extent3d(500, 1000, 50));
 * }</pre>
 *
 * @param extent the extent of the structure
 * @param layout the element order
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record Structure3d(Extent3d extent, Layout3d layout) {

    public Structure3d {
        requireNonNull(extent);
        requireNonNull(layout);
    }

    /**
     * Create a new matrix structure with the given dimension and the default
     * element order. This is the usual way for creating instances of structure
     * objects.
     *
     * @param extent the extent of the structure
     * @return a new structure object with the given extent
     */
    public static Structure3d of(Extent3d extent) {
        return new Structure3d(
            extent,
            new Layout3d(
                Index3d.ZERO,
                new Stride3d(
                    extent.rows()*extent.cols(),
                    extent.cols(),
                    1
                )
            )
        );
    }

    /**
     * Create a new matrix structure with the given dimension and the default
     * element order. This is the usual way for creating instances of structure
     * objects.
     *
     * @param slices the number of slices of the structure
     * @param rows the number of rows of the structure
     * @param cols the number of columns of the structure
     * @return a new structure object with the given extent
     */
    public static Structure3d of(int slices, int rows, int cols) {
        return of(new Extent3d(slices, rows, cols));
    }

}
