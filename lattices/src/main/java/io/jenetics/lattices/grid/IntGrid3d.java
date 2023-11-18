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
import io.jenetics.lattices.array.DenseIntArray;
import io.jenetics.lattices.grid.lattice.Lattice3d;
import io.jenetics.lattices.structure.Extent3d;
import io.jenetics.lattices.structure.Projection3d;
import io.jenetics.lattices.structure.Structure3d;

/**
 * Generic class for 3-d grids holding {@code int} elements. The
 * {@code IntGrid3d} is <em>just</em> a 3-d view onto a 1-d Java
 * {@code int[]} array. The following example shows how to create such a grid
 * view from a given {@code int[]} array.
 *
 * <pre>{@code
 * final var values = new int[3*50*100];
 * final var grid = new IntGrid3d(
 *     new Structure3d(new Extent3d(3, 50, 100)),
 *     new DenseIntArray(values)
 * );
 * }</pre>
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record IntGrid3d(Structure3d structure, Array.OfInt array)
    implements Lattice3d.OfInt<Array.OfInt>, Grid3d<Array.OfInt, IntGrid3d>
{

    /**
     * Factory for creating <em>dense</em> grid instances.
     */
    public static final Grid3d.Factory<IntGrid3d> DENSE =
        extent -> new IntGrid3d(
            new Structure3d(extent),
            DenseIntArray.ofSize(extent.cells())
        );

    /**
     * Create a new grid view from the given lattice.
     *
     * @param lattice the underlying lattice data
     */
    public IntGrid3d(Lattice3d<? extends Array.OfInt> lattice) {
        this(lattice.structure(), lattice.array());
    }

    @Override
    public IntGrid3d create(Structure3d structure, Array.OfInt array) {
        return new IntGrid3d(structure, array);
    }

    /**
     * Return a 2-d projection from this 3-d grid. The returned 2-d grid is
     * a view onto this grid {@link #array()}.
     *
     * @param projection the projection to apply
     * @return a 2-d projection from this 1-d grid
     */
    public IntGrid2d project(Projection3d projection) {
        return new IntGrid2d(projection.apply(structure()), array());
    }

    public static IntGrid3d of(Extent3d extent, int... values) {
        return new IntGrid3d(
            new Structure3d(extent),
            new DenseIntArray(values)
        );
    }

}
