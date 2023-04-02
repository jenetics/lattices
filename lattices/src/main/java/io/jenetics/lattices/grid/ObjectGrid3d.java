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

import static java.util.Objects.requireNonNull;
import static io.jenetics.lattices.grid.Grids.checkArraySize;
import static io.jenetics.lattices.grid.Grids.checkSameExtent;

import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import io.jenetics.lattices.array.DenseObjectArray;
import io.jenetics.lattices.array.ObjectArray;
import io.jenetics.lattices.structure.Projection3d;
import io.jenetics.lattices.structure.Structure3d;

/**
 * Object 3-d grid implementation.
 *
 * @param <T> the grid element type
 * @param structure The structure, which defines the <em>extent</em> of the grid
 *        and the <em>order</em> which determines the index mapping {@code N -> N^3}.
 * @param array The underlying {@code double[]} array.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version 3.0
 * @since 3.0
 */
public record ObjectGrid3d<T>(Structure3d structure, ObjectArray<T> array)
    implements Grid3d<ObjectArray<T>, ObjectGrid3d<T>>
{

    /**
     * Create a new 3-d grid with the given {@code structure} and element
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
    public ObjectGrid3d {
        checkArraySize(structure, array.length());
    }

    @Override
    public ObjectGrid3d<T> create(
        final Structure3d structure,
        final ObjectArray<T> array
    ) {
        return new ObjectGrid3d<>(structure, array);
    }

    /**
     * Returns the matrix cell value at coordinate {@code [row, col]}.
     *
     * @param slice the slice of the slice-coordinate
     * @param row the index of the row-coordinate
     * @param col the index of the column-coordinate
     * @return the value of the specified cell
     * @throws IndexOutOfBoundsException if the given coordinates are out of
     *         bounds
     */
    public T get(final int slice, final int row, final int col) {
        return array.get(mapping().offset(slice, row, col));
    }

    /**
     * Sets the grid cell at coordinate {@code [row, col]} to the specified
     * {@code value}.
     *
     * @param slice the slice of the slice-coordinate
     * @param row the index of the row-coordinate
     * @param col the index of the column-coordinate
     * @param value  the value to be filled into the specified cell
     * @throws IndexOutOfBoundsException if the given coordinates are out of
     *         bounds
     */
    public void set(final int slice, final int row, final int col, final T value) {
        array.set(mapping().offset(slice, row, col), value);
    }

    @Override
    public void assign(final ObjectGrid3d<T> other) {
        checkSameExtent(extent(), other.extent());
        forEach((s, r, c) -> set(s, r, c, other.get(s, r, c)));
    }

    /**
     * Sets all cells to the state specified by given {@code values}. The
     * {@code values} are required to have the form {@code values[row][column]}
     * and have exactly the same number of rows and columns as the receiver.
     *
     * @param values the values to be filled into the cells.
     * @throws IllegalArgumentException if {@code extent() != other.extent()}
     *
     * @implNote
     * The {@code values} are copied and subsequent chances to the {@code values}
     * are not reflected in the matrix, and vice-versa
     */
    public void assign(final T[][][] values) {
        if (values.length != structure.extent().slices()) {
            throw new IllegalArgumentException(
                "Values must have the same number of slices: " +
                    values.length + " != " + structure.extent().slices()
            );
        }

        for (int s = slices(); --s >= 0;) {
            final var slice = values[s];
            if (slice.length != rows()) {
                throw new IllegalArgumentException(
                    "Values must have the same number of rows: " +
                        slice.length + " != " + extent().rows()
                );
            }

            for (int r = rows(); --r >= 0;) {
                final var row = slice[r];
                if (row.length != cols()) {
                    throw new IllegalArgumentException(
                        "Values must have the same number of columns: " +
                            row.length + " != " + extent().cols()
                    );
                }

                for (int c = cols(); --c >= 0;) {
                    set(s, r, c, row[c]);
                }
            }
        }
    }

    /**
     * Sets all cells to the state specified by {@code values}.
     *
     * @param value the value to be filled into the cells
     */
    public void assign(final T value) {
        forEach((s, r, c) -> set(s, r, c, value));
    }

    /**
     * Assigns the result of a function to each cell.
     * <pre>{@code
     * this[i, j, k] = f(this[i, j, k])
     * }</pre>
     *
     * @param f a function object taking as argument the current cell's value.
     */
    public void assign(final UnaryOperator<T> f) {
        requireNonNull(f);
        forEach((s, r, c) -> set(s, r, c, f.apply(get(s, r, c))));
    }

    /**
     * Updates this grid with the values of {@code a} which are transformed by
     * the given function {@code f}.
     * <pre>{@code
     * this[i, j, k] = f(this[i, j, k], a[i, j, k])
     * }</pre>
     *
     * @param a the grid used for the update
     * @param f the combiner function
     * @throws IllegalArgumentException if {@code extent() != other.extent()}
     */
    public void assign(
        final ObjectGrid3d<? extends T> a,
        final BinaryOperator<T> f
    ) {
        checkSameExtent(extent(), a.extent());
        forEach((s, r, c) ->
            set(s, r, c, f.apply(get(s, r, c), a.get(s, r, c)))
        );
    }

    /**
     * Updates this grid with the values of {@code a} which are transformed by
     * the given function {@code f}.
     * <pre>{@code
     * this[i, j, k] = f(a[i, j, k])
     * }</pre>
     * <pre>{@code
     * final ObjectGrid3d<Integer> ints = ObjectGrid3d
     *     .<Integer>dense()
     *     .create(10, 15, 40);
     *
     * final ObjectGrid3d<String> strings = ObjectGrid3d
     *     .<String>dense()
     *     .create(10, 15, 40);
     *
     * ints.forEach((s, r, c) -> ints.set(s, r, c, s*r*c));
     * strings.assign(ints, Object::toString);
     * }</pre>
     *
     * @param a the grid used for the update
     * @param f the mapping function
     * @throws IllegalArgumentException if {@code extent() != other.extent()}
     */
    public <A> void assign(
        final ObjectGrid3d<? extends A> a,
        final Function<? super A, ? extends T> f
    ) {
        checkSameExtent(extent(), a.extent());
        forEach((s, r, c) -> set(s, r, c, f.apply(a.get(s, r, c))));
    }

    /**
     * Swaps each element {@code this[i, j, k]} with {@code other[i, j, k]}.
     *
     * @throws IllegalArgumentException if {@code extent() != other.extent()}
     */
    public void swap(final ObjectGrid3d<T> other) {
        checkSameExtent(extent(), other.extent());

        forEach((s, r, c) -> {
            final var tmp = get(s, r, c);
            set(s, r, c, other.get(s, r, c));
            other.set(s, r, c, tmp);
        });
    }

    /**
     * Return a 2-d projection from this 3-d grid. The returned 2-d grid is
     * a view onto this grid {@link #array()}.
     *
     * @param projection the projection to apply
     * @return a 1-d projection from this 2-d grid
     */
    public ObjectGrid2d<T> project(final Projection3d projection) {
        return new ObjectGrid2d<>(projection.apply(structure()), array());
    }

    /**
     * Checks whether the given matrices have the same dimension and contains
     * the same values.
     *
     * @param other the second matrix to compare
     * @return {@code true} if the two given matrices are equal, {@code false}
     * otherwise
     */
    public boolean equals(final ObjectGrid3d<?> other) {
        return extent().equals(other.extent()) &&
            allMatch((s, r, c) -> Objects.equals(get(s, r, c), other.get(s, r, c)));
    }

    @Override
    public int hashCode() {
        final int[] hash = new int[]{37};
        forEach((s, r, c) -> hash[0] += Objects.hashCode(get(s, r, c)) * 17);
        return hash[0];
    }

    @Override
    public boolean equals(final Object object) {
        return object == this ||
            object instanceof ObjectGrid3d<?> grid &&
                equals(grid);
    }

    /**
     * Return a factory for creating dense 3-d object grids.
     *
     * @param __ not used (Java trick for getting "reified" element type)
     * @param <T> the grid element type
     * @return the dense object factory
     */
    @SuppressWarnings("varargs")
    @SafeVarargs
    public static <T> Factory3d<ObjectGrid3d<T>> dense(final T... __) {
        return structure -> new ObjectGrid3d<T>(
            structure,
            DenseObjectArray.ofSize(structure.extent().size(), __)
        );
    }

}
