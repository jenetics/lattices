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
import io.jenetics.lattices.structure.Extent3d;
import io.jenetics.lattices.structure.Projection3d;
import io.jenetics.lattices.structure.Structure3d;

/**
 * Generic class for 3-d lattice holding {@code int} elements. The
 * {@code IntLattice3d} is <em>just</em> a 3-d view onto a 1-d Java
 * {@code int[]} array. The following example shows how to create such a lattice
 * view from a given {@code int[]} array.
 *
 * <pre>{@code
 * final var values = new int[3*50*100];
 * final var grid = new IntLattice3d(
 *     new Structure3d(new Extent3d(3, 50, 100)),
 *     new DenseIntArray(values)
 * );
 * }</pre>
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record IntLattice3d(Structure3d structure, BaseArray.OfInt array)
    implements Lattice3d.OfInt<BaseArray.OfInt>
{

    /**
     * Factory for creating <em>dense</em> lattice instances.
     */
    public static final Lattice3d.Factory<IntLattice3d> DENSE =
        extent -> new IntLattice3d(
            new Structure3d(extent),
            DenseIntArray.ofLength(extent.cells())
        );

    /**
     * Create a new lattice view from the given lattice.
     *
     * @param lattice the underlying lattice data
     */
    public IntLattice3d(Lattice3d<? extends BaseArray.OfInt> lattice) {
        this(lattice.structure(), lattice.array());
    }

    /**
     * Create a 3-d lattice view of the given input {@code values}.
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
    public IntLattice3d(Extent3d extent, int... values) {
        this(new Structure3d(extent), new DenseIntArray(values));
    }

    /**
     * Return a 2-d projection from this 3-d grid. The returned 2-d grid is
     * a view onto this grid {@link #array()}.
     *
     * @param projection the projection to apply
     * @return a 2-d projection from this 1-d grid
     */
    public IntLattice2d project(Projection3d projection) {
        return new IntLattice2d(projection.apply(structure()), array());
    }

}
