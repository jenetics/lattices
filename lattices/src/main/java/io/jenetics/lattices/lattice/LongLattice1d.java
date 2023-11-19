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

import io.jenetics.lattices.array.Array;
import io.jenetics.lattices.array.BaseArray;
import io.jenetics.lattices.array.DenseLongArray;
import io.jenetics.lattices.structure.Extent1d;
import io.jenetics.lattices.structure.Structure1d;

/**
 * Generic class for 1-d lattice holding {@code long} elements. The
 * {@code LongLattice1d} is <em>just</em> a 1-d view onto a 1-d Java
 * {@code long[]} array. The following example shows how to create such a lattice
 * view from a given {@code long[]} array.
 *
 * <pre>{@code
 * final var extent = new Extent1d(100);
 * final var values = new long[extent.size()];
 * final var grid = new LongLattice1d(
 *     new Structure1d(extent),
 *     new DenseLongArray(values)
 * );
 * }</pre>
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record LongLattice1d(Structure1d structure, BaseArray.OfLong array)
    implements Lattice1d.OfLong<BaseArray.OfLong>
{

    /**
     * Factory for creating <em>dense</em> grid instances.
     */
    public static final Lattice1d.Factory<LongLattice1d> DENSE =
        extent -> new LongLattice1d(
            new Structure1d(extent),
            new DenseLongArray(new long[extent.elements()])
        );

    /**
     * Create a new grid view from the given lattice.
     *
     * @param lattice the underlying lattice data
     */
    public LongLattice1d(Lattice1d<? extends BaseArray.OfLong> lattice) {
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
    public LongLattice1d(long... values) {
        this(
            new Structure1d(new Extent1d(values.length)),
            new DenseLongArray(values)
        );
    }

}
