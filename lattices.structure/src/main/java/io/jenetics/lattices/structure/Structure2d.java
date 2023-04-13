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
 * Defines a 2-d structure, which is defined by the extent of the structure and
 * the layout of the underlying 1-d structure. This is the main class for working
 * with 2-d structures (latices/grids). The {@link View2d} and {@link Projection2d}
 * functions are used for <em>manipulating</em> this structure object.
 *
 * <pre>{@code
 * // Creating a new structure with the given extent.
 * final var structure = Structure2d.of(new Extent2d(500, 1000));
 * }</pre>
 *
 * @param extent the extent of the structure
 * @param layout the element order
 * @param channel the channel number of the structure
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record Structure2d(Extent2d extent, Layout2d layout, Channel channel)
    implements OffsetMapper2d
{

    public Structure2d {
        requireNonNull(extent);
        requireNonNull(layout);
    }

    @Override
    public int offset(int row, int col) {
        return layout.offset(row, col) + channel.value();
    }

    @Override
    public int offset(Index2d index) {
        return layout.offset(index) + channel.value();
    }

    @Override
    public Index2d index(int offset) {
        return layout.index(offset - channel.value());
    }

    /**
     * Create a new matrix structure with the given dimension and the default
     * element order. This is the usual way for creating instances of structure
     * objects.
     *
     * @param extent the extent of the structure
     * @return a new structure object with the given extent
     */
    public static Structure2d of(Extent2d extent) {
        return of(extent, Channels.ONE);
    }

    /**
     * Create a new matrix structure with the given dimension and the default
     * element order. This is the usual way for creating instances of structure
     * objects.
     *
     * @param extent the extent of the structure
     * @return a new structure object with the given extent
     */
    public static Structure2d of(Extent2d extent, Channels channels) {
        return new Structure2d(
            extent,
            new Layout2d(
                Index2d.ZERO,
                new Stride2d(extent.cols()*channels.value(), channels.value()),
                channels
            ),
            Channel.ZERO
        );
    }

    /**
     * Create a new matrix structure with the given dimension and the default
     * element order. This is the usual way for creating instances of structure
     * objects.
     *
     * @param rows the number of rows of the structure
     * @param cols the number of columns of the structure
     * @return a new structure object with the given extent
     */
    public static Structure2d of(int rows, int cols) {
        return of(new Extent2d(rows, cols));
    }

}
