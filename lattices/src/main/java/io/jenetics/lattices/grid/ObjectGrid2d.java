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
import io.jenetics.lattices.structure.Projection2d;
import io.jenetics.lattices.structure.Structure2d;

/**
 * Object grid class.
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
public record ObjectGrid2d<T>(Structure2d structure, ObjectArray<T> array)
    implements Grid2d<ObjectArray<T>, ObjectGrid2d<T>>
{

    /**
     * Create a new 1-d grid with the given {@code structure} and element
     * {@code array}.
     *
     * @param structure the matrix structure
     * @param array the element array
     * @throws IllegalArgumentException if the size of the given {@code array}
     * is not able to hold the required number of elements. It is still possible
     * that an {@link IndexOutOfBoundsException} is thrown when the defined
     * order of the grid tries to access an array index, which is not within the
     * bounds of the {@code array}.
     * @throws NullPointerException if one of the arguments is {@code null}
     */
    public ObjectGrid2d {
        if (structure.extent().size() > array.length()) {
            throw new IllegalArgumentException(
                "The number of available elements is smaller than the number of " +
                    "required grid cells: %d > %d."
                        .formatted(structure.extent().size(), array.length())
            );
        }
    }

    @Override
    public ObjectGrid2d<T> create(
        final Structure2d structure,
        final ObjectArray<T> array
    ) {
        return new ObjectGrid2d<>(structure, array);
    }

    @Override
    public void assign(final ObjectGrid2d<T> other) {
        forEach((r, c) -> set(r, c, other.get(r, c)));
    }

    /**
     * Returns the matrix cell value at coordinate {@code [row, col]}.
     *
     * @param row the index of the row-coordinate
     * @param col the index of the column-coordinate
     * @return the value of the specified cell
     * @throws IndexOutOfBoundsException if the given coordinates are out of
     *         bounds
     */
    public T get(final int row, final int col) {
        return array.get(order().index(row, col));
    }

    /**
     * Sets the grid cell at coordinate {@code [row, col]} to the specified
     * {@code value}.
     *
     * @param row the index of the row-coordinate
     * @param col the index of the column-coordinate
     * @param value  the value to be filled into the specified cell
     * @throws IndexOutOfBoundsException if the given coordinates are out of
     *         bounds
     */
    public void set(final int row, final int col, final T value) {
        array.set(order().index(row, col), value);
    }

    /**
     * Return a 1-d projection from this 2-d grid. The returned 1-d grid is
     * a view onto this grid {@link #array()}.
     *
     * @param projection the projection to apply
     * @return a 1-d projection from this 2-d grid
     */
    public ObjectGrid1d<T> project(final Projection2d projection) {
        return new ObjectGrid1d<>(projection.apply(structure()), array());
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
        return struct -> new ObjectGrid2d<T>(
            struct,
            DenseObjectArray.ofSize(struct.extent().size(), __)
        );
    }

}
