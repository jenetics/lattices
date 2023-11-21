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
import io.jenetics.lattices.array.DenseLongArray;
import io.jenetics.lattices.array.SparseLongArray;
import io.jenetics.lattices.structure.Extent2d;
import io.jenetics.lattices.structure.Projection2d;
import io.jenetics.lattices.structure.Structure2d;

/**
 * Generic class for 2-d lattice holding {@code long} elements. The
 * {@code LongLattice2d} is <em>just</em> a 2-d view onto a 1-d Java
 * {@code long[]} array. The following example shows how to create such a grid
 * view from a given {@code long[]} array.
 *
 * <pre>{@code
 * final var extent = new Extent2d(50, 100);
 * final var values = new long[extent.size()];
 * final var grid = new LongLattice2d(
 *     new Structure2d(extent),
 *     new DenseLongArray(values)
 * );
 * }</pre>
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record LongLattice2d(Structure2d structure, BaseArray.OfLong array)
    implements Lattice2d.OfLong<BaseArray.OfLong>
{

    /**
     * Factory for creating <em>dense</em> lattice instances.
     */
    public static final Lattice2d.Factory<LongLattice2d> DENSE =
        extent -> new LongLattice2d(
            new Structure2d(extent),
            DenseLongArray.ofLength(extent.cells())
        );

    /**
     * Factory for creating <em>sparse</em> lattice instances.
     */
    public static final Lattice2d.Factory<LongLattice2d> SPARSE =
        extent -> new LongLattice2d(
            new Structure2d(extent),
            new SparseLongArray(extent.cells())
        );

    /**
     * Create a new grid view from the given lattice.
     *
     * @param lattice the underlying lattice data
     */
    public LongLattice2d(Lattice2d<? extends BaseArray.OfLong> lattice) {
        this(lattice.structure(), lattice.array());
    }

    /**
     * Create a 2-d lattice view of the given input {@code values}. It is assumed
     * that the values are given in row-major order. The following example shows
     * how to create a <em>dense</em> 3x4 grid.
     * <pre>{@code
     * final var grid = new LongLattice2d(
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
     * @throws IllegalArgumentException if the desired extent of the grid
     *         requires fewer elements than given
     */
    public LongLattice2d(Extent2d extent, long... values) {
        this(new Structure2d(extent), new DenseLongArray(values));
    }

    /**
     * Return a 1-d projection from this 2-d grid. The returned 1-d grid is
     * a view onto this grid {@link #array()}.
     *
     * @param projection the projection to apply
     * @return a 1-d projection from this 2-d grid
     */
    public LongLattice1d project(Projection2d projection) {
        return new LongLattice1d(projection.apply(structure()), array());
    }

}
