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
import io.jenetics.lattices.array.DenseDoubleArray;
import io.jenetics.lattices.array.SparseDoubleArray;
import io.jenetics.lattices.lattice.Lattice1d;
import io.jenetics.lattices.structure.Extent1d;
import io.jenetics.lattices.structure.Structure1d;

/**
 * Generic class for 1-d grids holding {@code double} elements. The
 * {@code DoubleGrid1d} is <em>just</em> a view onto a 1-d Java {@code double[]}
 * array. The following example shows how to create such a grid view from a given
 * {@code double[]} array.
 *
 * <pre>{@code
 * final var values = new double[100];
 * final var grid = new DoubleGrid1d(
 *     new Structure1d(new Extent1d(100)),
 *     new DenseDoubleArray(values)
 * );
 * }</pre>
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record DoubleGrid1d(Structure1d structure, Array.OfDouble array)
    implements Lattice1d.OfDouble<Array.OfDouble>, Grid1d.OfDouble<DoubleGrid1d>
{

    /**
     * Factory for creating <em>dense</em> grid instances.
     */
    public static final Lattice1d.Factory<DoubleGrid1d> DENSE =
        extent -> new DoubleGrid1d(
            new Structure1d(extent),
            DenseDoubleArray.ofLength(extent.cells())
        );

    /**
     * Factory for creating <em>sparse</em> grid instances.
     */
    public static final Lattice1d.Factory<DoubleGrid1d> SPARSE =
        extent -> new DoubleGrid1d(
            new Structure1d(extent),
            new SparseDoubleArray(extent.cells())
        );

    /**
     * Create a new grid view from the given lattice.
     *
     * @param lattice the underlying lattice data
     */
    public DoubleGrid1d(Lattice1d<? extends Array.OfDouble> lattice) {
        this(lattice.structure(), lattice.array());
    }

    /**
     * Create a 1-d grid view of the given input {@code values}.
     *
     * @implSpec
     * The given input data is <b>not</b> copied, the returned object is a
     * <em>view</em> onto the given input data.
     *
     * @param values the returned grid
     */
    public DoubleGrid1d(double... values) {
        this(
            new Structure1d(new Extent1d(values.length)),
            new DenseDoubleArray(values)
        );
    }

    @Override
    public DoubleGrid1d create(Structure1d structure, Array.OfDouble array) {
        return new DoubleGrid1d(structure, array);
    }

}
