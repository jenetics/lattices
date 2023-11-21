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
import io.jenetics.lattices.structure.Extent3d;
import io.jenetics.lattices.structure.Projection3d;
import io.jenetics.lattices.structure.Structure3d;

/**
 * Generic class for 3-d lattices holding {@code double} elements. The
 * {@code DoubleLattice3d} is <em>just</em> a 3-d view onto a 1-d Java
 * {@code double[]} array. The following example shows how to create such a grid
 * view from a given {@code double[]} array.
 *
 * <pre>{@code
 * final var values = new double[3*50*100];
 * final var grid = new DoubleLattice3d(
 *     new Structure3d(new Extent3d(3, 50, 100)),
 *     new DenseDoubleArray(values)
 * );
 * }</pre>
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record DoubleLattice3d(Structure3d structure, BaseArray.OfDouble array)
    implements Lattice3d.OfDouble<BaseArray.OfDouble>
{

    /**
     * Factory for creating <em>dense</em> lattice instances.
     */
    public static final Lattice3d.Factory<DoubleLattice3d> DENSE =
        extent -> new DoubleLattice3d(
            new Structure3d(extent),
            DenseDoubleArray.ofLength(extent.cells())
        );

    /**
     * Factory for creating <em>sparse</em> lattice instances.
     */
    public static final Lattice3d.Factory<DoubleLattice3d> SPARSE =
        extent -> new DoubleLattice3d(
            new Structure3d(extent),
            new SparseDoubleArray(extent.cells())
        );

    /**
     * Create a new grid view from the given lattice.
     *
     * @param lattice the underlying lattice data
     */
    public DoubleLattice3d(Lattice3d<? extends BaseArray.OfDouble> lattice) {
        this(lattice.structure(), lattice.array());
    }

    /**
     * Create a 3-d grid view of the given input {@code values}.
     *
     * @implSpec
     * The given input data is <b>not</b> copied, the returned object is a
     * <b>view</b> onto the given input data.
     *
     * @param extent the extent of the given values
     * @param values the returned grid values
     * @throws IllegalArgumentException if the desired extent of the lattice
     *         requires fewer elements than given
     */
    public DoubleLattice3d(Extent3d extent, double... values) {
        this(new Structure3d(extent), new DenseDoubleArray(values));
    }

    /**
     * Return a 2-d projection from this 3-d lattice. The returned 2-d grid is
     * a view onto this lattice {@link #array()}.
     *
     * @param projection the projection to apply
     * @return a 2-d projection from this 3-d lattice
     */
    public DoubleLattice2d project(Projection3d projection) {
        return new DoubleLattice2d(projection.apply(structure()), array());
    }

}
