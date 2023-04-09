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

import io.jenetics.lattices.array.DenseObjectArray;
import io.jenetics.lattices.array.ObjectArray;
import io.jenetics.lattices.lattice.ObjectLattice2d;
import io.jenetics.lattices.structure.Projection2d;
import io.jenetics.lattices.structure.Projection3d;
import io.jenetics.lattices.structure.Structure2d;

/**
 * Object 2-d grid implementation.
 *
 * @param <T> the grid element type
 * @param structure The structure, which defines the <em>extent</em> of the grid
 *        and the <em>order</em> which determines the index mapping {@code N -> N^2}.
 * @param array The underlying {@code double[]} array.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version 3.0
 * @since 3.0
 */
public record ObjectGrid2d<T>(Structure2d structure, ObjectArray<T> array)
    implements ObjectLattice2d<T>, Grid2d<ObjectArray<T>, ObjectGrid2d<T>>
{

    @Override
    public ObjectGrid2d<T> create(Structure2d structure, ObjectArray<T> array) {
        return new ObjectGrid2d<>(structure, array);
    }

    @Override
    public void assign(ObjectGrid2d<T> other) {
        ObjectLattice2d.super.assign(other);
    }

    public ObjectGrid1d<T> project(Projection2d projection) {
        return new ObjectGrid1d<>(projection.apply(structure), array);
    }

    @Override
    public boolean equals(final Object object) {
        return object == this ||
            object instanceof ObjectGrid2d<?> grid &&
                equals(grid);
    }

    @Override
    public String toString() {
        final var out = new StringBuilder();

        out.append("[");
        for (int i = 0; i < rows(); ++i) {
            if (i != 0) {
                out.append(" ");
            }
            out.append("[");
            for (int j = 0; j < cols(); ++j) {
                out.append(get(i, j));
                if (j < cols() - 1) {
                    out.append(", ");
                }
            }
            out.append("]");
            if (i < rows() - 1) {
                out.append("\n");
            }
        }

        out.append("]");

        return out.toString();
    }

    /**
     * Return a factory for creating dense 2-d object grids.
     *
     * @param __ not used (Java trick for getting "reified" element type)
     * @param <T> the grid element type
     * @return the dense object factory
     */
    @SuppressWarnings("varargs")
    @SafeVarargs
    public static <T> Factory2d<ObjectGrid2d<T>> dense(final T... __) {
        return structure -> new ObjectGrid2d<T>(
            structure,
            DenseObjectArray.ofSize(structure.extent().size(), __)
        );
    }

}
