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
import io.jenetics.lattices.array.DenseDoubleArray;
import io.jenetics.lattices.array.SparseDoubleArray;
import io.jenetics.lattices.structure.Extent2d;
import io.jenetics.lattices.structure.Projection2d;
import io.jenetics.lattices.structure.Structure2d;

/**
 * Generic class for 2-d lattices holding {@code double} elements. The
 * {@code DoubleGrid2d} is <em>just</em> a 2-d view onto a 1-d Java
 * {@code double[]} array. The following example shows how to create such a grid
 * view from a given {@code double[]} array.
 *
 * <pre>{@code
 * final var values = new double[50*100];
 * final var grid = new DoubleLattice2d(
 *     new Structure2d(new Extent2d(50, 100)),
 *     new DenseDoubleArray(values)
 * );
 * }</pre>
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record DoubleLattice2d(Structure2d structure, BaseArray.OfDouble array)
    implements Lattice2d.OfDouble<BaseArray.OfDouble>
{

    /**
     * Factory for creating <em>dense</em> lattice instances.
     */
    public static final Lattice2d.Factory<DoubleLattice2d> DENSE =
        extent -> new DoubleLattice2d(
            new Structure2d(extent),
            DenseDoubleArray.ofLength(extent.cells())
        );

    /**
     * Factory for creating <em>sparse</em> lattice instances.
     */
    public static final Lattice2d.Factory<DoubleLattice2d> SPARSE =
        extent -> new DoubleLattice2d(
            new Structure2d(extent),
            new SparseDoubleArray(extent.cells())
        );

    /**
     * Create a new lattice view from the given lattice.
     *
     * @param lattice the underlying lattice data
     */
    public DoubleLattice2d(Lattice2d<? extends BaseArray.OfDouble> lattice) {
        this(lattice.structure(), lattice.array());
    }

    /**
     * Create a 2-d lattice view of the given input {@code values}. It is assumed
     * that the values are given in row-major order. The following example shows
     * how to create a <em>dense</em> 3x4 grid.
     * <pre>{@code
     * final var grid = new DoubleLattice2d(
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
    public DoubleLattice2d(Extent2d extent, double... values) {
        this(new Structure2d(extent), new DenseDoubleArray(values));
    }

    /**
     * Return a 1-d projection from this 2-d grid. The returned 1-d grid is
     * a view onto this grid {@link #array()}.
     *
     * @param projection the projection to apply
     * @return a 1-d projection from this 2-d grid
     */
    public DoubleLattice1d project(Projection2d projection) {
        return new DoubleLattice1d(projection.apply(structure()), array());
    }

}
