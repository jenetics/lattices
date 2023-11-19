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

import java.util.Objects;

import io.jenetics.lattices.array.BaseArray;
import io.jenetics.lattices.array.DenseObjectArray;
import io.jenetics.lattices.structure.Projection3d;
import io.jenetics.lattices.structure.Structure3d;

/**
 * Object 3-d lattice implementation.
 *
 * @param <T> the lattice element type
 * @param structure The structure, which defines the <em>extent</em> of the grid
 *        and the <em>order</em> which determines the index mapping {@code N -> N^3}.
 * @param array The underlying {@code double[]} array.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version 3.0
 * @since 3.0
 */
public record ObjectLattice3d<T>(Structure3d structure, BaseArray.OfObject<T> array)
    implements Lattice3d.OfObject<T, BaseArray.OfObject<T>>
{

    /**
     * Create a new lattice view from the given lattice.
     *
     * @param lattice the underlying lattice data
     */
    public ObjectLattice3d(Lattice3d<? extends BaseArray.OfObject<T>> lattice) {
        this(lattice.structure(), lattice.array());
    }

    public ObjectLattice2d<T> project(Projection3d projection) {
        return new ObjectLattice2d<>(projection.apply(structure), array);
    }

    @Override
    public int hashCode() {
        final int[] hash = new int[]{37};
        forEach((s, r, c) -> hash[0] += Objects.hashCode(get(s, r, c)) * 17);
        return hash[0];
    }

    @Override
    public boolean equals(Object object) {
        return object == this ||
            object instanceof ObjectLattice3d<?> grid &&
                equals(grid);
    }

    /**
     * Return a factory for creating dense 3-d object grids.
     *
     * @param __ not used (a Java trick for getting "reified" element type)
     * @param <T> the grid element type
     * @return the dense object factory
     */
    @SuppressWarnings("varargs")
    @SafeVarargs
    public static <T> Lattice3d.Factory<ObjectLattice3d<T>> dense(T... __) {
        return extent -> new ObjectLattice3d<T>(
            new Structure3d(extent),
            DenseObjectArray.ofLength(extent.cells(), __)
        );
    }

}
