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
import io.jenetics.lattices.structure.Extent2d;
import io.jenetics.lattices.structure.Range2d;
import io.jenetics.lattices.structure.Structural2d;
import io.jenetics.lattices.structure.Structure2d;
import io.jenetics.lattices.structure.View2d;

/**
 * This interface <em>structures</em> the elements into a 2-dimensional grid.
 *
 * @param <A> the array type which stores the grid elements
 * @param <G> the <em>self</em> grid type
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public interface Grid2d<A extends Array<A>, G extends Grid2d<A, G>>
    extends Structural2d, Loopable2d, Grid<A, G>
{

    /**
     * Create a new grid (view) with the given structure and the underlying
     * data array.
     *
     * @param structure the structure of the created grid
     * @param array the grid elements
     * @return a new grid (view)
     */
    G create(final Structure2d structure, final A array);

    /**
     * Assigns the elements of the {@code other} grid to this grid.
     *
     * @param other the source of the grid elements
     */
    void assign(final G other);

    /**
     * Creates a new grid with the given {@code extent} and the properties of
     * the underlying array.
     *
     * @param extent the extent of the new grid
     * @return a new grid
     */
    default G like(final Extent2d extent) {
        return create(
            Structure2d.of(extent),
            array().like(extent.size())
        );
    }

    /**
     * Create a new grid with the same extent and properties as this grid.
     *
     * @return a new grid with the same extent and properties as this grid
     */
    default G like() {
        return like(extent());
    }

    @Override
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
    default G view(final View2d view) {
        return create(view.apply(structure()), array());
    }

    /**
     * Return the default looping strategy of this structural, which can be
     * overridden by the implementation, if desired.
     *
     * @return the looping strategy of this structural
     */
    @Override
    default Loop2d loop() {
        return Loop2d.of(new Range2d(extent()));
    }

}
