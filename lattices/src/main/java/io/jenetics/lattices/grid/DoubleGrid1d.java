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

import io.jenetics.lattices.array.DenseDoubleArray;
import io.jenetics.lattices.array.DoubleArray;
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
public final class DoubleGrid1d extends BaseDoubleGrid1d<DoubleGrid1d> {

    /**
     * Factory for creating dense 1-d double grids.
     */
    public static final Factory1d<DoubleGrid1d> DENSE = structure ->
        new DoubleGrid1d(
            structure,
            DenseDoubleArray.ofSize(structure.extent().size())
        );

    /**
     * Create a new 1-d grid with the given {@code structure} and element
     * {@code array}.
     *
     * @param structure the matrix structure
     * @param array the element array
     * @throws IllegalArgumentException if the size of the given {@code array}
     *         is not able to hold the required number of elements. It is still
     *         possible that an {@link IndexOutOfBoundsException} is thrown when
     *         the defined order of the grid tries to access an array index,
     *         which is not within the bounds of the {@code array}.
     * @throws NullPointerException if one of the arguments is {@code null}
     */
    public DoubleGrid1d(final Structure1d structure, final DoubleArray array) {
        super(structure, array, DoubleGrid1d::new);
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
    public static DoubleGrid1d of(final double... values) {
        return new DoubleGrid1d(
            Structure1d.of(new Extent1d(values.length)),
            new DenseDoubleArray(values)
        );
    }

}
