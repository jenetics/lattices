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
import io.jenetics.lattices.grid.array.DenseLongArray;
import io.jenetics.lattices.grid.lattice.Lattice2d;
import io.jenetics.lattices.structure.Extent2d;
import io.jenetics.lattices.structure.Projection2d;
import io.jenetics.lattices.structure.Structure2d;

/**
 * Generic class for 2-d grids holding {@code long} elements. The
 * {@code LongGrid2d} is <em>just</em> a 2-d view onto a 1-d Java
 * {@code long[]} array. The following example shows how to create such a grid
 * view from a given {@code long[]} array.
 *
 * <pre>{@code
 * final var extent = new Extent2d(50, 100);
 * final var values = new long[extent.size()];
 * final var grid = new LongGrid2d(
 *     new Structure2d(extent),
 *     new DenseLongArray(values)
 * );
 * }</pre>
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record LongGrid2d(Structure2d structure, Array.OfLong array)
    implements Lattice2d.OfLong<Array.OfLong>, Grid2d<Array.OfLong, LongGrid2d>
{

    /**
     * Factory for creating <em>dense</em> grid instances.
     */
    public static final Grid2d.Factory<LongGrid2d> DENSE =
        extent -> new LongGrid2d(
            new Structure2d(extent),
            DenseLongArray.ofSize(extent.cells())
        );

    /**
     * Create a new grid view from the given lattice.
     *
     * @param lattice the underlying lattice data
     */
    public LongGrid2d(Lattice2d<? extends Array.OfLong> lattice) {
        this(lattice.structure(), lattice.array());
    }

    @Override
    public LongGrid2d create(Structure2d structure, Array.OfLong array) {
        return new LongGrid2d(structure, array);
    }

    /**
     * Return a 1-d projection from this 2-d grid. The returned 1-d grid is
     * a view onto this grid {@link #array()}.
     *
     * @param projection the projection to apply
     * @return a 1-d projection from this 2-d grid
     */
    public LongGrid1d project(Projection2d projection) {
        return new LongGrid1d(projection.apply(structure()), array());
    }


    /**
     * Return a 2-d grid view of the given input {@code values}. It is assumed
     * that the values are given in row-major order. The following example shows
     * how to create a <em>dense</em> 3x4 grid.
     * <pre>{@code
     * final var grid = DoubleGrid2d.of(
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
    public static LongGrid2d of(Extent2d extent, long... values) {
        return new LongGrid2d(
            new Structure2d(extent),
            new DenseLongArray(values)
        );
    }


}
