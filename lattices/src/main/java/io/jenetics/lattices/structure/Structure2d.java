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
 * final var structure = new Structure2d(new Extent2d(500, 1000));
 * }</pre>
 *
 * @apiNote
 * Although the structure object consists of an <em>extent</em> and a
 * <em>layout</em>, it is normally created with the
 * {@link Structure2d#Structure2d(Extent2d)} constructor, which takes care of
 * the proper construction of the underlying <em>layout</em>.
 *
 * @param extent the extent of the structure
 * @param layout the element order
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record Structure2d(Extent2d extent, Layout2d layout) {

    public Structure2d {
        requireNonNull(extent);
        requireNonNull(layout);
    }

    /**
     * Create a new structure with the given extent and the default element order.
     * This is the <em>default</em> constructor used for creating structure
     * instances.
     *
     * @apiNote
     * This is the <em>default</em> constructor which should be used by the
     * clients for creating a new <em>structure</em> object.
     *
     * @param extent the extent of the structure
     */
    public Structure2d(Extent2d extent) {
        this(extent, new Layout2d(extent));
    }

}
