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

import io.jenetics.lattices.grid.array.DenseDoubleArray;
import io.jenetics.lattices.grid.array.DoubleArray;
import io.jenetics.lattices.grid.lattice.DoubleLattice3d;
import io.jenetics.lattices.grid.lattice.Lattice3d;
import io.jenetics.lattices.structure.Extent3d;
import io.jenetics.lattices.structure.Projection3d;
import io.jenetics.lattices.structure.Structure3d;

/**
 * Generic class for 3-d grids holding {@code double} elements. The
 * {@code DoubleGrid3d} is <em>just</em> a 3-d view onto a 1-d Java
 * {@code double[]} array. The following example shows how to create such a grid
 * view from a given {@code double[]} array.
 *
 * <pre>{@code
 * final var values = new double[3*50*100];
 * final var grid = new DoubleGrid3d(
 *     Structure3d.of(new Extent3d(3, 50, 100)),
 *     new DenseDoubleArray(values)
 * );
 * }</pre>
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record DoubleGrid3d(Structure3d structure, DoubleArray array)
    implements DoubleLattice3d, Grid3d<DoubleArray, DoubleGrid3d>
{

    /**
     * Factory for creating <em>dense</em> grid instances.
     */
    public static final Grid3d.Factory<DoubleGrid3d> DENSE =
        extent -> new DoubleGrid3d(
            Structure3d.of(extent),
            DenseDoubleArray.ofSize(extent.size())
        );

    /**
     * Create a new grid view from the given lattice.
     *
     * @param lattice the underlying lattice data
     */
    public DoubleGrid3d(Lattice3d<? extends DoubleArray> lattice) {
        this(lattice.structure(), lattice.array());
    }

    @Override
    public DoubleGrid3d create(Structure3d structure, DoubleArray array) {
        return new DoubleGrid3d(structure, array);
    }

    @Override
    public void assign(DoubleGrid3d other) {
        DoubleLattice3d.super.assign(other);
    }

    /**
     * Return a 2-d projection from this 3-d grid. The returned 2-d grid is
     * a view onto this grid {@link #array()}.
     *
     * @param projection the projection to apply
     * @return a 2-d projection from this 3-d grid
     */
    public DoubleGrid2d project(Projection3d projection) {
        return new DoubleGrid2d(projection.apply(structure()), array());
    }

    public static DoubleGrid3d of(Extent3d extent, double... values) {
        return new DoubleGrid3d(
            Structure3d.of(extent),
            new DenseDoubleArray(values)
        );
    }

}
