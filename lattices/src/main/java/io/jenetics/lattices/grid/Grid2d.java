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
import io.jenetics.lattices.lattice.Lattice2d;
import io.jenetics.lattices.structure.Extent2d;
import io.jenetics.lattices.structure.Projection2d;
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
    extends Lattice2d<A>, Self<G>
{

    /**
     * Create a new grid (view) with the given structure and the underlying
     * data array.
     *
     * @param structure the structure of the created grid
     * @param array the grid elements
     * @return a new grid (view)
     */
    G create(Structure2d structure, A array);

    /**
     * Create a new grid (view) with the given underlying lattice structure
     * and lattice array.
     *
     * @param lattice the underlying lattice data
     * @return a new grid (view)
     */
    default G create(Lattice2d<? extends A> lattice) {
        return create(lattice.structure(), lattice.array());
    }

    /**
     * Creates a new grid with the given {@code extent} and the properties of
     * the underlying array.
     *
     * @param extent the extent of the new grid
     * @return a new grid
     */
    default G like(Extent2d extent) {
        return create(
            new Structure2d(extent),
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
    default G view(View2d view) {
        return create(view.apply(structure()), array());
    }


    /**
     * This interface <em>structures</em> the elements into a 2-dimensional lattice.
     *
     * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
     * @since 3.0
     * @version 3.0
     */
    interface OfDouble<G extends OfDouble<G>>
        extends Lattice2d.OfDouble<Array.OfDouble>, Grid2d<Array.OfDouble, G>
    {

        /**
         * Return a 1-d projection from this 2-d grid. The returned 1-d grid is
         * a view onto this grid {@link #array()}.
         *
         * @param projection the projection to apply
         * @return a 1-d projection from this 2-d matrix
         */
        default Grid1d.OfDouble<?> project(Projection2d projection) {
            return new DoubleGrid1d(projection.apply(structure()), array());
        }

    }

    /**
     * This interface <em>structures</em> the elements into a 2-dimensional
     * int grid.
     *
     * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
     * @since 3.0
     * @version 3.0
     */
    interface OfInt<G extends Grid2d.OfInt<G>>
        extends Lattice2d.OfInt<Array.OfInt>, Grid2d<Array.OfInt, G>
    {

        /**
         * Return a 1-d projection from this 2-d grid. The returned 1-d grid is
         * a view onto this grid {@link #array()}.
         *
         * @param projection the projection to apply
         * @return a 1-d projection from this 2-d matrix
         */
        default Grid1d.OfInt<?> project(Projection2d projection) {
            return new IntGrid1d(projection.apply(structure()), array());
        }

    }

    /**
     * This interface <em>structures</em> the elements into a 2-dimensional
     * long grid.
     *
     * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
     * @since 3.0
     * @version 3.0
     */
    interface OfLong<G extends Grid2d.OfLong<G>>
        extends Lattice2d.OfLong<Array.OfLong>, Grid2d<Array.OfLong, G>
    {
        /**
         * Return a 1-d projection from this 2-d grid. The returned 1-d grid is
         * a view onto this grid {@link #array()}.
         *
         * @param projection the projection to apply
         * @return a 1-d projection from this 2-d matrix
         */
        default Grid1d.OfLong<?> project(Projection2d projection) {
            return new LongGrid1d(projection.apply(structure()), array());
        }

    }

    /**
     * This interface <em>structures</em> the elements into a 2-dimensional
     * object grid.
     *
     * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
     * @since 3.0
     * @version 3.0
     */
    interface OfObject<T, G extends Grid2d.OfObject<T, G>>
        extends Lattice2d.OfObject<T, Array.OfObject<T>>, Grid2d<Array.OfObject<T>, G>
    {
        /**
         * Return a 1-d projection from this 2-d grid. The returned 1-d grid is
         * a view onto this grid {@link #array()}.
         *
         * @param projection the projection to apply
         * @return a 1-d projection from this 2-d matrix
         */
        default Grid1d.OfObject<T, ?> project(Projection2d projection) {
            return new ObjectGrid1d<>(projection.apply(structure()), array());
        }

    }


}
