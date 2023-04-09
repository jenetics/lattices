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

import io.jenetics.lattices.array.DenseIntArray;
import io.jenetics.lattices.array.IntArray;
import io.jenetics.lattices.lattice.IntLattice3d;
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
public record IntGrid3d(Structure3d structure, IntArray array)
    implements IntLattice3d, Grid3d<IntArray, IntGrid3d>
{

    @Override
    public IntGrid3d create(Structure3d structure, IntArray array) {
        return new IntGrid3d(structure, array);
    }

    @Override
    public void assign(IntGrid3d other) {
        IntLattice3d.super.assign(other);
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
            Structure3d.of(extent),
            new DenseIntArray(values)
        );
    }

}
