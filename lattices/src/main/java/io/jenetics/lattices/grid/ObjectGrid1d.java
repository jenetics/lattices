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
import io.jenetics.lattices.array.DenseObjectArray;
import io.jenetics.lattices.lattice.Lattice1d;
import io.jenetics.lattices.structure.Structure1d;

/**
 * Object 1-d grid implementation.
 *
 * @param <T> the grid element type
 * @param structure The structure, which defines the <em>extent</em> of the grid
 *        and the <em>order</em> which determines the index mapping {@code N -> N}.
 * @param array The underlying {@code double[]} array.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version 3.0
 * @since 3.0
 */
public record ObjectGrid1d<T>(Structure1d structure, Array.OfObject<T> array)
    implements Lattice1d.OfObject<T, Array.OfObject<T>>, Grid1d<Array.OfObject<T>, ObjectGrid1d<T>>
{

    /**
     * Create a new grid view from the given lattice.
     *
     * @param lattice the underlying lattice data
     */
    public ObjectGrid1d(Lattice1d<? extends Array.OfObject<T>> lattice) {
        this(lattice.structure(), lattice.array());
    }

    @Override
    public ObjectGrid1d<T> create(Structure1d structure, Array.OfObject<T> array) {
        return new ObjectGrid1d<>(structure, array);
    }

    /**
     * Return a factory for creating dense 1-d object grids.
     *
     * @param __ not used (Java trick for getting "reified" element type)
     * @param <T> the grid element type
     * @return the dense object factory
     */
    @SuppressWarnings("varargs")
    @SafeVarargs
    public static <T> Grid1d.Factory<ObjectGrid1d<T>> dense(T... __) {
        return extent -> new ObjectGrid1d<T>(
            new Structure1d(extent),
            DenseObjectArray.ofLength(extent.cells(), __)
        );
    }

}
