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
package io.jenetics.lattices.lattice;

import io.jenetics.lattices.array.BaseArray;
import io.jenetics.lattices.array.DenseIntArray;
import io.jenetics.lattices.array.SparseIntArray;
import io.jenetics.lattices.structure.Extent1d;
import io.jenetics.lattices.structure.Structure1d;

/**
 * Generic class for 1-d lattice holding {@code int} elements. The
 * {@code IntLattice1d} is <em>just</em> a view onto a 1-d Java {@code int[]}
 * array. The following example shows how to create such a grid view from a given
 * {@code int[]} array.
 *
 * <pre>{@code
 * final var values = new int[100];
 * final var grid = new IntLattice1d(
 *     new Structure1d(new Extent1d(100)),
 *     new DenseIntArray(values)
 * );
 * }</pre>
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record IntLattice1d(Structure1d structure, BaseArray.OfInt array)
    implements Lattice1d.OfInt<BaseArray.OfInt>
{

    /**
     * Factory for creating <em>dense</em> lattice instances.
     */
    public static final Lattice1d.Factory<IntLattice1d> DENSE =
        extent -> new IntLattice1d(
            new Structure1d(extent),
            DenseIntArray.ofLength(extent.cells())
        );

    /**
     * Factory for creating <em>sparse</em> lattice instances.
     */
    public static final Lattice1d.Factory<IntLattice1d> SPARSE =
        extent -> new IntLattice1d(
            new Structure1d(extent),
            new SparseIntArray(extent.cells())
        );

    /**
     * Create a new grid view from the given lattice.
     *
     * @param lattice the underlying lattice data
     */
    public IntLattice1d(Lattice1d<? extends BaseArray.OfInt> lattice) {
        this(lattice.structure(), lattice.array());
    }

    /**
     * Create a 1-d lattice view of the given input {@code values}.
     *
     * @implSpec
     * The given input data is <b>not</b> copied, the returned object is a
     * <em>view</em> onto the given input data.
     *
     * @param values the lattice values
     */
    public IntLattice1d(int... values) {
        this(
            new Structure1d(new Extent1d(values.length)),
            new DenseIntArray(values)
        );
    }

}
