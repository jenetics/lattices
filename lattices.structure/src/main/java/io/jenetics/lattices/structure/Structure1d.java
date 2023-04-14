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
 * the matrix and the index order of the underlying element array. The
 * {@link View1d} function1 is used for <em>manipulating</em> this structure
 * object.
 *
 * <pre>{@code
 * // Creating a new structure with the given extent.
 * final var structure = Structure1d.of(new Extent1d(1000));
 * }</pre>
 *
 * @param extent the extent of the structure
 * @param layout the element layout
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record Structure1d(Extent1d extent, Layout1d layout, Channel channel)
    implements OffsetMapper1d
{

    public Structure1d {
        requireNonNull(extent);
        requireNonNull(layout);
    }

    @Override
    public int offset(int index) {
        return layout.offset(index) + channel.value();
    }

    @Override
    public int offset(Index1d index) {
        return layout.offset(index) + channel.value();
    }

    @Override
    public Index1d index(int offset) {
        return layout.index(offset - channel.value());
    }

    /**
     * Create a new matrix structure with the given dimension and the default
     * element order. This is the usual way for creating instances of structure
     * objects.
     *
     * @param extent the extent of the structure
     * @param channels the number of channels of the created structure
     * @return a new structure object with the given extent
     */
    public static Structure1d of(Extent1d extent, Channels channels) {
        return new Structure1d(
            extent,
            new Layout1d(
                Index1d.ZERO,
                new Stride1d(channels.value())
            ),
            Channel.ZERO
        );
    }

    /**
     * Create a new matrix structure with the given dimension and the default
     * element order. This is the usual way for creating instances of structure
     * objects.
     *
     * @param extent the extent of the structure
     * @return a new structure object with the given extent
     */
    public static Structure1d of(Extent1d extent) {
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
    public static Structure1d of(int extent) {
        return of(new Extent1d(extent), Channels.ONE);
    }

}
