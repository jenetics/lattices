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

import io.jenetics.lattices.array.Array;
import io.jenetics.lattices.grid.lattice.Lattice3d;
import io.jenetics.lattices.structure.Extent3d;
import io.jenetics.lattices.structure.Structure3d;
import io.jenetics.lattices.structure.View3d;

/**
 * This interface <em>structures</em> the elements into a 3-dimensional grid.
 *
 * @param <A> the array type which stores the grid elements
 * @param <G> the <em>self</em> grid type
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public interface Grid3d<A extends Array<A>, G extends Grid3d<A, G>>
    extends Lattice3d<A>, Self<G>
{

    /**
     * Create a new grid (view) with the given structure and the underlying
     * data array.
     *
     * @param structure the structure of the created grid
     * @param array the grid elements
     * @return a new grid (view)
     */
    G create(Structure3d structure, final A array);

    /**
     * Create a new grid (view) with the given underlying lattice structure
     * and lattice array.
     *
     * @param lattice the underlying lattice data
     * @return a new grid (view)
     */
    default G create(Lattice3d<? extends A> lattice) {
        return create(lattice.structure(), lattice.array());
    }

    /**
     * Creates a new grid with the given {@code extent} and the properties of
     * the underlying array.
     *
     * @param extent the extent of the new grid
     * @return a new grid
     */
    default G like(Extent3d extent) {
        return create(
            new Structure3d(extent),
            array().like(extent.elements())
        );
    }

    /**
     * Create a new grid with the same extent and properties as this grid.
     *
     * @return a new grid with the same extent and properties as this grid
     */
    default G like() {
        return like(structure().extent());
    }

    /**
     * Return a copy of {@code this} grid.
     *
     * @return a copy of {@code this} grid
     */
    default G copy() {
        final var copy = like();
        copy.assign(self());
        return copy;
    }

    /**
     * Create a new grid by applying the given {@code view} transformation.
     *
     * @param view the grid view transformation to apply
     * @return a new grid view
     */
    default G view(View3d view) {
        return create(view.apply(structure()), array());
    }

}
