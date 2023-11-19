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
import io.jenetics.lattices.lattice.Lattice1d;
import io.jenetics.lattices.matrix.DoubleMatrix1d;
import io.jenetics.lattices.structure.Extent1d;
import io.jenetics.lattices.structure.Projection2d;
import io.jenetics.lattices.structure.Structure1d;
import io.jenetics.lattices.structure.View1d;

/**
 * This interface <em>structures</em> the elements into a 1-dimensional grid.
 *
 * @param <A> the array type used for storing the gird data
 * @param <G> the type created by the factory
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public interface Grid1d<A extends Array<A>, G extends Grid1d<A, G>>
    extends Lattice1d<A>, Self<G>
{

    /**
     * Create a new grid (view) with the given structure and the underlying
     * data array.
     *
     * @param structure the structure of the created grid
     * @param array the grid elements
     * @return a new grid (view)
     */
    G create(Structure1d structure, A array);

    /**
     * Create a new grid (view) with the given underlying lattice structure
     * and lattice array.
     *
     * @param lattice the underlying lattice data
     * @return a new grid (view)
     */
    default G create(Lattice1d<? extends A> lattice) {
        return create(lattice.structure(), lattice.array());
    }

    /**
     * Creates a new grid with the given {@code extent} and the properties of
     * the underlying array.
     *
     * @param extent the extent of the new grid
     * @return a new grid
     */
    default G like(Extent1d extent) {
        return create(
            new Structure1d(extent),
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
        copy.assign(this);
        return copy;
    }

    /**
     * Create a new grid by applying the given {@code view} transformation.
     *
     * @param view the grid view transformation to apply
     * @return a new grid view
     */
    default G view(View1d view) {
        return create(view.apply(structure()), array());
    }

    /**
     * This interface <em>structures</em> the elements into a 1-dimensional
     * double grid.
     *
     * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
     * @since 3.0
     * @version 3.0
     */
    interface OfDouble<G extends Grid1d.OfDouble<G>>
        extends Lattice1d.OfDouble<Array.OfDouble>, Grid1d<Array.OfDouble, G>
    {
    }

    /**
     * This interface <em>structures</em> the elements into a 1-dimensional
     * int grid.
     *
     * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
     * @since 3.0
     * @version 3.0
     */
    interface OfInt<G extends Grid1d.OfInt<G>>
        extends Lattice1d.OfInt<Array.OfInt>, Grid1d<Array.OfInt, G>
    {
    }

    /**
     * This interface <em>structures</em> the elements into a 1-dimensional
     * long grid.
     *
     * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
     * @since 3.0
     * @version 3.0
     */
    interface OfLong<G extends Grid1d.OfLong<G>>
        extends Lattice1d.OfLong<Array.OfLong>, Grid1d<Array.OfLong, G>
    {
    }

    /**
     * This interface <em>structures</em> the elements into a 1-dimensional
     * object grid.
     *
     * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
     * @since 3.0
     * @version 3.0
     */
    interface OfObject<T, G extends Grid1d.OfObject<T, G>>
        extends Lattice1d.OfObject<T, Array.OfObject<T>>, Grid1d<Array.OfObject<T>, G>
    {
    }

}
