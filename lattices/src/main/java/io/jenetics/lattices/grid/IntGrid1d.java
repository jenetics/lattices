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

import io.jenetics.lattices.grid.array.DenseIntArray;
import io.jenetics.lattices.grid.array.IntArray;
import io.jenetics.lattices.grid.lattice.Lattice1d;
import io.jenetics.lattices.structure.Extent1d;
import io.jenetics.lattices.structure.Structure1d;

/**
 * Generic class for 1-d grids holding {@code int} elements. The
 * {@code DoubleGrid1d} is <em>just</em> a view onto a 1-d Java {@code int[]}
 * array. The following example shows how to create such a grid view from a given
 * {@code int[]} array.
 *
 * <pre>{@code
 * final var values = new int[100];
 * final var grid = new IntGrid1d(
 *     new Structure1d(new Extent1d(100)),
 *     new DenseIntArray(values)
 * );
 * }</pre>
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record IntGrid1d(Structure1d structure, IntArray array)
    implements Lattice1d.OfInt<IntArray>, Grid1d<IntArray, IntGrid1d>
{

    /**
     * Factory for creating <em>dense</em> grid instances.
     */
    public static final Grid1d.Factory<IntGrid1d> DENSE =
        extent -> new IntGrid1d(
            new Structure1d(extent),
            DenseIntArray.ofSize(extent.cells())
        );

    /**
     * Create a new grid view from the given lattice.
     *
     * @param lattice the underlying lattice data
     */
    public IntGrid1d(Lattice1d<? extends IntArray> lattice) {
        this(lattice.structure(), lattice.array());
    }

    @Override
    public IntGrid1d create(Structure1d structure, IntArray array) {
        return new IntGrid1d(structure, array);
    }

    @Override
    public void assign(IntGrid1d other) {
        OfInt.super.assign(other);
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
    public static IntGrid1d of(int... values) {
        return new IntGrid1d(
            new Structure1d(new Extent1d(values.length)),
            new DenseIntArray(values)
        );
    }

}
