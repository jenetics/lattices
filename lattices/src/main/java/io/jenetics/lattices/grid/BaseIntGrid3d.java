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

import io.jenetics.lattices.array.IntArray;
import io.jenetics.lattices.structure.Structure3d;

import java.util.function.BiFunction;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;
import java.util.function.LongBinaryOperator;
import java.util.function.LongUnaryOperator;

import static io.jenetics.lattices.grid.Grids.checkArraySize;
import static io.jenetics.lattices.grid.Grids.checkSameExtent;
import static java.util.Objects.requireNonNull;

/**
 * Abstract double grid implementation.
 *
 * @param <G> the grid type
 */
public abstract class BaseIntGrid3d<G extends BaseIntGrid3d<G>>
    implements Grid3d<IntArray, G>
{

    /**
     * The structure, which defines the <em>extent</em> of the grid and the
     * <em>order</em> which determines the index mapping {@code N^2 -> N}.
     */
    private final Structure3d structure;

    /**
     * The underlying {@code int[]} array.
     */
    private final IntArray array;

    private final BiFunction<Structure3d, IntArray, G> constructor;

    /**
     * Create a new 3-d grid with the given {@code structure} and element
     * {@code array}.
     *
     * @param structure the matrix structure
     * @param array the element array
     * @param constructor the constructor of the actual grid type
     * @throws IllegalArgumentException if the size of the given {@code array}
     * is not able to hold the required number of elements. It is still possible
     * that an {@link IndexOutOfBoundsException} is thrown when the defined
     * order of the grid tries to access an array index, which is not within the
     * bounds of the {@code array}.
     * @throws NullPointerException if one of the arguments is {@code null}
     */
    protected BaseIntGrid3d(
        final Structure3d structure,
        final IntArray array,
        final BiFunction<? super Structure3d, ? super IntArray, ? extends G> constructor
    ) {
        checkArraySize(structure.extent(), array.length());

        this.structure = structure;
        this.array = array;

        @SuppressWarnings("unchecked") final var ctr = (BiFunction<Structure3d, IntArray, G>) constructor;
        this.constructor = requireNonNull(ctr);
    }

    /**
     * Return the structure for grid.
     *
     * @return the structure for grid
     */
    @Override
    public Structure3d structure() {
        return structure;
    }

    /**
     * Return the underlying element array.
     *
     * @return the underlying element array
     */
    @Override
    public IntArray array() {
        return array;
    }

    @Override
    public G create(final Structure3d structure, final IntArray array) {
        return constructor.apply(structure, array);
    }

    /**
     * Returns the matrix cell value at coordinate {@code [row, col]}.
     *
     * @param slice the index of the slice-coordinate
     * @param row the index of the row-coordinate
     * @param col the index of the column-coordinate
     * @return the value of the specified cell
     * @throws IndexOutOfBoundsException if the given coordinates are out of
     * bounds
     */
    public int get(final int slice, final int row, final int col) {
        return array.get(order().index(slice, row, col));
    }

    /**
     * Sets the matrix cell at coordinate {@code [row, col]} to the specified
     * {@code value}.
     *
     * @param slice the index of the slice-coordinate
     * @param row the index of the row-coordinate
     * @param col the index of the column-coordinate
     * @param value the value to be filled into the specified cell
     * @throws IndexOutOfBoundsException if the given coordinates are out of
     * bounds
     */
    public void set(final int slice, final int row, final int col, final int value) {
        array.set(order().index(slice, row, col), value);
    }

    /**
     * Replaces all cell values of the receiver with the values of another
     * matrix. Both matrices must have the same number of rows and columns.
     *
     * @param other the source matrix to copy from (maybe identical to the
     *        receiver).
     * @throws IllegalArgumentException if
     *         {@code !extent().equals(other.extent())}
     */
    @Override
    public void assign(final G other) {
        if (other == this) {
            return;
        }
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
    public void assign(final int[][][] values) {
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
    public void assign(final int value) {
        forEach((s, r, c) -> set(s, r, c, value));
    }

    /**
     * Assigns the result of a function to each cell
     * {@code x[slice, row, col] = f(x[slice, row, col], y[slice, row, col])}.
     *
     * @param y the secondary matrix to operate on.
     * @param f a function object taking as first argument the current cell's
     * value of {@code this}, and as second argument the current cell's value of
     * {@code y}
     * @throws IllegalArgumentException if {@code !extent().equals(y.extent())}
     */
    public void assign(
        final BaseIntGrid3d<?> y,
        final IntBinaryOperator f
    ) {
        requireNonNull(f);
        checkSameExtent(extent(), y.extent());

        forEach((s, r, c) -> set(s, r, c, f.applyAsInt(get(s, r, c), y.get(s, r, c))));
    }

    /**
     * Assigns the result of a function to each cell
     * {@code x[slice, row, col] = f(x[slice, row, col])}.
     *
     * @param f a function object taking as argument the current cell's value.
     */
    public void assign(final IntUnaryOperator f) {
        requireNonNull(f);
        forEach((s, r, c) -> set(s, r, c, f.applyAsInt(get(s, r, c))));
    }

    /**
     * Swaps each element {@code this[i, j]} with {@code other[i, j]}.
     *
     * @throws IllegalArgumentException if {@code extent() != other.extent()}.
     */
    public void swap(final BaseIntGrid3d<?> other) {
        checkSameExtent(extent(), other.extent());

        forEach((s, r, c) -> {
            final var tmp = get(s, r, c);
            set(s, r, c, other.get(s, r, c));
            other.set(s, r, c, tmp);
        });
    }

    /**
     * Applies a function to each cell and aggregates the results. Returns a
     * value <em>v</em> such that <em>v==a(size())</em> where
     * <em>a(i) == reduce(a(i - 1), f(get(slice, row, col)))</em> and
     * terminators are
     * <em>a(1) == f(get(0, 0, 0)), a(0) == Double.NaN</em>.
     * <p><b>Example:</b></p>
     * <pre>
     * 2 x 2 matrix
     * 0 1
     * 2 3
     *
     * // Sum(x[slice, row, col]*x[slice, row, col])
     * matrix.aggregate(Double::sum, a -> a*a) --> 14
     * </pre>
     *
     * @param reducer an aggregation function taking as first argument the
     * current aggregation and as second argument the transformed current cell
     * value
     * @param f a function transforming the current cell value
     * @return the aggregated value
     */
    public long reduce(final LongBinaryOperator reducer, final LongUnaryOperator f) {
        requireNonNull(reducer);
        requireNonNull(f);

        if (extent().size() == 0) {
            return 0;
        }

        long a = f.applyAsLong(get(slices() - 1, rows() - 1, cols() - 1));
        int d = 1;
        for (int s = slices(); --s >= 0; ) {
            for (int r = rows(); --r >= 0; ) {
                for (int c = cols() - d; --c >= 0; ) {
                    a = reducer.applyAsLong(a, f.applyAsLong(get(s, r, c)));
                }
                d = 0;
            }
        }
        return a;
    }

    /**
     * Checks whether the given matrices have the same dimension and contains
     * the same values.
     *
     * @param other the second matrix to compare
     * @return {@code true} if the two given matrices are equal, {@code false}
     * otherwise
     */
    public boolean equals(final BaseIntGrid3d<?> other) {
        return extent().equals(other.extent()) &&
            allMatch((s, r, c) -> get(s, r, c) == other.get(s, r, c));
    }

    @Override
    public int hashCode() {
        final int[] hash = new int[]{37};
        forEach((i, j, k) -> hash[0] += Integer.hashCode(get(i, j, k)) * 17);
        return hash[0];
    }

    @Override
    public boolean equals(final Object object) {
        return object == this ||
            object instanceof  BaseIntGrid3d<?> grid &&
                equals(grid);
    }

}
