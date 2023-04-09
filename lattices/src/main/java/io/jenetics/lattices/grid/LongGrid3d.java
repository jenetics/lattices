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

import io.jenetics.lattices.array.DenseLongArray;
import io.jenetics.lattices.array.LongArray;
import io.jenetics.lattices.lattice.Lattice3d;
import io.jenetics.lattices.lattice.LongLattice3d;
import io.jenetics.lattices.structure.Extent3d;
import io.jenetics.lattices.structure.Projection3d;
import io.jenetics.lattices.structure.Structure3d;

/**
 * Generic class for 3-d grids holding {@code long} elements. The
 * {@code LongGrid3d} is <em>just</em> a 3-d view onto a 1-d Java
 * {@code long[]} array. The following example shows how to create such a grid
 * view from a given {@code long[]} array.
 *
 * <pre>{@code
 * final var extent = new Extent3d(30, 50, 100);
 * final var values = new long[extent.size()];
 * final var grid = new LongGrid3d(
 *     Structure3d.of(extent),
 *     new DenseLongArray(values)
 * );
 * }</pre>
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record LongGrid3d(Structure3d structure, LongArray array)
    implements LongLattice3d, Grid3d<LongArray, LongGrid3d>
{

    /**
     * Factory for creating <em>dense</em> grid instances.
     */
    public static final Grid3d.Factory<LongGrid3d> DENSE =
        extent -> new LongGrid3d(
            Structure3d.of(extent),
            DenseLongArray.ofSize(extent.size())
        );

    /**
     * Create a new grid view from the given lattice.
     *
     * @param lattice the underlying lattice data
     */
    public LongGrid3d(Lattice3d<? extends LongArray> lattice) {
        this(lattice.structure(), lattice.array());
    }

    @Override
    public LongGrid3d create(Structure3d structure, LongArray array) {
        return new LongGrid3d(structure, array);
    }

    @Override
    public void assign(LongGrid3d other) {
        LongLattice3d.super.assign(other);
    }

    /**
     * Return a 2-d projection from this 3-d grid. The returned 2-d grid is
     * a view onto this grid {@link #array()}.
     *
     * @param projection the projection to apply
     * @return a 2-d projection from this 1-d grid
     */
    public LongGrid2d project(final Projection3d projection) {
        return new LongGrid2d(projection.apply(structure()), array());
    }

    public static LongGrid3d of(Extent3d extent, long... values) {
        return new LongGrid3d(
            Structure3d.of(extent),
            new DenseLongArray(values)
        );
    }

}
