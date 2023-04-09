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
import io.jenetics.lattices.lattice.Lattice1d;
import io.jenetics.lattices.lattice.LongLattice1d;
import io.jenetics.lattices.structure.Extent1d;
import io.jenetics.lattices.structure.Structure1d;

/**
 * Generic class for 1-d grids holding {@code long} elements. The
 * {@code LongGrid1d} is <em>just</em> a 1-d view onto a 1-d Java
 * {@code long[]} array. The following example shows how to create such a grid
 * view from a given {@code long[]} array.
 *
 * <pre>{@code
 * final var extent = new Extent1d(100);
 * final var values = new long[extent.size()];
 * final var grid = new LongGrid1d(
 *     Structure1d.of(extent),
 *     new DenseLongArray(values)
 * );
 * }</pre>
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record LongGrid1d(Structure1d structure, LongArray array)
    implements LongLattice1d, Grid1d<LongArray, LongGrid1d>
{

    /**
     * Factory for creating <em>dense</em> grid instances.
     */
    public static final Grid1d.Factory<LongGrid1d> DENSE =
        extent -> new LongGrid1d(
            Structure1d.of(extent),
            new DenseLongArray(new long[extent.size()])
        );

    /**
     * Create a new grid view from the given lattice.
     *
     * @param lattice the underlying lattice data
     */
    public LongGrid1d(Lattice1d<? extends LongArray> lattice) {
        this(lattice.structure(), lattice.array());
    }

    @Override
    public LongGrid1d create(Structure1d structure, LongArray array) {
        return new LongGrid1d(structure, array);
    }

    @Override
    public void assign(LongGrid1d other) {
        LongLattice1d.super.assign(other);
    }

    /**
     * Return a 1-d grid view of the given input {@code values}.
     *
     * @implSpec
     * The given input data is <b>not</b> copied, the returned object is a
     * <em>view</em> onto the given input data.
     *
     * @param values the returned grid
     * @return a grid view of the given input data
     */
    public static LongGrid1d of(long... values) {
        return new LongGrid1d(
            Structure1d.of(new Extent1d(values.length)),
            new DenseLongArray(values)
        );
    }

}
