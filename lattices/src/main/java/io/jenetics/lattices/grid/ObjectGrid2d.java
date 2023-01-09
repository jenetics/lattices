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
import io.jenetics.lattices.structure.Projection2d;
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
    implements Grid2d<ObjectArray<T>, ObjectGrid2d<T>>
{

    /**
     * Create a new 2-d grid with the given {@code structure} and element
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
        checkArraySize(structure, array.length());
    }

    @Override
    public ObjectGrid2d<T> create(
        final Structure2d structure,
        final ObjectArray<T> array
    ) {
        return new ObjectGrid2d<>(structure, array);
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

    @Override
    public void assign(final ObjectGrid2d<T> other) {
        if (other == this) {
            return;
        }
        checkSameExtent(extent(), other.extent());
        forEach((r, c) -> set(r, c, other.get(r, c)));
    }

    /**
     * Sets all cells to the state specified by given {@code values}. The
     * {@code values} are required to have the form {@code values[row][column]}
     * and have exactly the same number of rows and columns as the receiver.
     *
     * @implNote
     * The {@code values} are copied and subsequent chances to the {@code values}
     * are not reflected in the matrix, and vice-versa
     *
     * @param values the values to be filled into the cells.
     * @throws IllegalArgumentException if {@code !extent().equals(other.extent())}
     */
    public void assign(final T[][] values) {
        if (values.length != structure.extent().rows()) {
            throw new IllegalArgumentException(
                "Values must have the same number of rows: " +
                    values.length + " != " + structure.extent().rows()
            );
        }

        for (int r = rows(); --r >= 0;) {
            final var row = values[r];

            if (row.length != cols()) {
                throw new IllegalArgumentException(
                    "Values must have the same number of columns: " +
                        row.length + " != " + extent().cols()
                );
            }

            for (int c = cols(); --c >= 0;) {
                set(r, c, row[c]);
            }
        }
    }

    /**
     * Sets all cells to the state specified by {the @code value}.
     *
     * @param value the value to be filled into the cells
     */
    public void assign(final T value) {
        forEach((r, c) -> set(r, c, value));
    }

    /**
     * Assigns the result of a function to each cell.
     * <pre>{@code
     * this[i, j] = f(this[i, j])
     * }</pre>
     *
     * @param f a function object taking as argument the current cell's value.
     */
    public void assign(final UnaryOperator<T> f) {
        requireNonNull(f);
        forEach((r, c) -> set(r, c, f.apply(get(r, c))));
    }

    /**
     * Updates this grid with the values of {@code a} which are transformed by
     * the given function {@code f}.
     * <pre>{@code
     * this[i, j] = f(this[i, j], a[i, j])
     * }</pre>
     *
     * @param a the grid used for the update
     * @param f the combiner function
     * @throws IllegalArgumentException if {@code extent() != other.extent()}
     */
    public void assign(
        final ObjectGrid2d<? extends T> a,
        final BinaryOperator<T> f
    ) {
        checkSameExtent(extent(), a.extent());
        forEach((r, c) -> set(r, c, f.apply(get(r, c), a.get(r, c))));
    }

    /**
     * Updates this grid with the values of {@code a} which are transformed by
     * the given function {@code f}.
     * <pre>{@code
     * this[i, j] = f(a[i, j])
     * }</pre>
     * This can be used to assign a grid of strings from a grid of integers.
     * <pre>{@code
     * final ObjectGrid2d<Integer> ints = ObjectGrid2d
     *     .<Integer>dense()
     *     .create(10, 15);
     *
     * final ObjectGrid2d<String> strings = ObjectGrid2d
     *     .<String>dense()
     *     .create(10, 15);
     *
     * ints.forEach((r, c) -> ints.set(r, c, r*c));
     * strings.assign(ints, Object::toString);
     * }</pre>
     *
     * @param a the grid used for the update
     * @param f the mapping function
     * @throws IllegalArgumentException if {@code extent() != other.extent()}
     */
    public <A> void assign(
        final ObjectGrid2d<? extends A> a,
        final Function<? super A, ? extends T> f
    ) {
        checkSameExtent(extent(), a.extent());
        forEach((r, c) -> set(r, c, f.apply(a.get(r, c))));
    }

    /**
     * Swaps each element {@code this[i, j]} with {@code other[i, j]}.
     *
     * @throws IllegalArgumentException if {@code extent() != other.extent()}
     */
    public void swap(final ObjectGrid2d<T> other) {
        checkSameExtent(extent(), other.extent());

        forEach((r, c) -> {
            final var tmp = get(r, c);
            set(r, c, other.get(r, c));
            other.set(r, c, tmp);
        });
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
     * Checks whether the given matrices have the same dimension and contains
     * the same values.
     *
     * @param other the second matrix to compare
     * @return {@code true} if the two given matrices are equal, {@code false}
     * otherwise
     */
    public boolean equals(final ObjectGrid2d<?> other) {
        return extent().equals(other.extent()) &&
            allMatch((r, c) -> Objects.equals(get(r, c), other.get(r, c)));
    }

    @Override
    public int hashCode() {
        final int[] hash = new int[]{37};
        forEach((r, c) -> hash[0] += Objects.hashCode(get(r, c)) * 17);
        return hash[0];
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
