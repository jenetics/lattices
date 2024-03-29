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

import io.jenetics.lattices.grid.array.Array;
import io.jenetics.lattices.grid.array.DenseIntArray;
import io.jenetics.lattices.grid.lattice.Lattice2d;
import io.jenetics.lattices.structure.Extent2d;
import io.jenetics.lattices.structure.Projection2d;
import io.jenetics.lattices.structure.Structure2d;

/**
 * Generic class for 2-d grids holding {@code int} elements. The
 * {@code IntGrid2d} is <em>just</em> a 2-d view onto a 1-d Java
 * {@code int[]} array. The following example shows how to create such a grid
 * view from a given {@code int[]} array.
 *
 * <pre>{@code
 * final var values = new int[50*100];
 * final var grid = new IntGrid2d(
 *     new Structure2d(new Extent2d(50, 100)),
 *     new DenseIntArray(values)
 * );
 * }</pre>
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record IntGrid2d(Structure2d structure, Array.OfInt array)
    implements Lattice2d.OfInt<Array.OfInt>, Grid2d<Array.OfInt, IntGrid2d>
{

    /**
     * Factory for creating <em>dense</em> grid instances.
     */
    public static final Grid2d.Factory<IntGrid2d> DENSE =
        extent -> new IntGrid2d(
            new Structure2d(extent),
            DenseIntArray.ofSize(extent.cells())
        );

    /**
     * Create a new grid view from the given lattice.
     *
     * @param lattice the underlying lattice data
     */
    public IntGrid2d(Lattice2d<? extends Array.OfInt> lattice) {
        this(lattice.structure(), lattice.array());
    }

    @Override
    public IntGrid2d create(Structure2d structure, Array.OfInt array) {
        return new IntGrid2d(structure, array);
    }

    /**
     * Return a 1-d projection from this 2-d grid. The returned 1-d grid is
     * a view onto this grid {@link #array()}.
     *
     * @param projection the projection to apply
     * @return a 1-d projection from this 2-d grid
     */
    public IntGrid1d project(Projection2d projection) {
        return new IntGrid1d(projection.apply(structure()), array());
    }


    /**
     * Return a 2-d grid view of the given input {@code values}. It is assumed
     * that the values are given in row-major order. The following example shows
     * how to create a <em>dense</em> 3x4 grid.
     * <pre>{@code
     * final var grid = IntGrid2d.of(
     *     new Extent2d(3, 4),
     *     1, 2,  3,  4,
     *     5, 6,  7,  8,
     *     9, 10, 11, 12
     * );
     * }</pre>
     *
     * @implSpec
     * The given input data is <b>not</b> copied, the returned object is a
     * <b>view</b> onto the given input data.
     *
     * @param extent the extent of the given values
     * @param values the returned grid values
     * @return a grid view of the given input data
     * @throws IllegalArgumentException if the desired extent of the grid
     *         requires fewer elements than given
     */
    public static IntGrid2d of(Extent2d extent, int... values) {
        return new IntGrid2d(
            new Structure2d(extent),
            new DenseIntArray(values)
        );
    }

}
