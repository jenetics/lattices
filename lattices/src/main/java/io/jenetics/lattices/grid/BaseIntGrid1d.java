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

import java.util.OptionalInt;
import java.util.function.BiFunction;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;

import io.jenetics.lattices.array.IntArray;
import io.jenetics.lattices.structure.Extent1d;
import io.jenetics.lattices.structure.Structure1d;

/**
 * Abstract int grid implementation.
 *
 * @param <G> the grid type
 */
public abstract class BaseIntGrid1d<G extends BaseIntGrid1d<G>>
    implements Grid1d<IntArray, G>
{

    /**
     * The structure, which defines the <em>extent</em> of the grid and the
     * <em>order</em> which determines the index mapping {@code N -> N}.
     */
    private final Structure1d structure;

    /**
     * The underlying {@code int[]} array.
     */
    private final IntArray array;

    private final BiFunction<Structure1d, IntArray, G> constructor;

    /**
     * Create a new 1-d grid with the given {@code structure} and element
     * {@code array}.
     *
     * @param structure the matrix structure
     * @param array the element array
     * @param constructor the constructor of the actual grid type
     * @throws IllegalArgumentException if the size of the given {@code array}
     *         is not able to hold the required number of elements. It is still
     *         possible that an {@link IndexOutOfBoundsException} is thrown when
     *         the defined order of the grid tries to access an array index,
     *         which is not within the bounds of the {@code array}.
     * @throws NullPointerException if one of the arguments is {@code null}
     */
    protected BaseIntGrid1d(
        final Structure1d structure,
        final IntArray array,
        final BiFunction<? super Structure1d, ? super IntArray, ? extends G> constructor
    ) {
        checkArraySize(structure, array.length());

        this.structure = structure;
        this.array = array;

        @SuppressWarnings("unchecked")
        final var ctr = (BiFunction<Structure1d, IntArray, G>)constructor;
        this.constructor = requireNonNull(ctr);
    }

    @Override
    public Structure1d structure() {
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
    public G create(final Structure1d structure, final IntArray array) {
        return constructor.apply(structure, array);
    }

    /**
     * Returns the matrix cell value at coordinate {@code index}.
     *
     * @param index the index of the cell
     * @return the value of the specified cell
     * @throws IndexOutOfBoundsException if the given coordinates are out of
     *         bounds
     */
    public int get(final int index) {
        return array.get(structure().offset(index));
    }

    /**
     * Sets the matrix cell at coordinate {@code index} to the specified
     * {@code value}.
     *
     * @param index the index of the cell
     * @param value  the value to be filled into the specified cell
     * @throws IndexOutOfBoundsException if the given coordinates are out of
     *         bounds
     */
    public void set(final int index, final int value) {
        array.set(structure().offset(index),  value);
    }

    /**
     * Replaces all cell values of the receiver with the values of another
     * matrix. Both matrices must have the same number of rows and columns.
     *
     * @param other the source matrix to copy from (maybe identical to the
     *        receiver).
     * @throws IllegalArgumentException if {@code !extent().equals(other.extent())}
     */
    @Override
    public void assign(final G other) {
        if (other == this) {
            return;
        }
        checkSameExtent(extent(), other.extent());

        forEach(i -> set(i, other.get(i)));
    }

    /**
     * Sets all cells to the state specified by {@code values}.
     *
     * @param values the values to be filled into the cells
     */
    public void assign(final int[] values) {
        checkSameExtent(extent(), new Extent1d(values.length));
        forEach(i -> set(i, values[i]));
    }

    /**
     * Sets all cells to the state specified by {@code values}.
     *
     * @param value the value to be filled into the cells
     */
    public void assign(final int value) {
        forEach(i -> set(i, value));
    }

    /**
     * Assigns the result of a function to each cell.
     * <pre>{@code
     * this[i] = f(this[i])
     * }</pre>
     *
     * @param f a function object taking as argument the current cell's value.
     */
    public void assign(final IntUnaryOperator f) {
        requireNonNull(f);
        forEach(i -> set(i, f.applyAsInt(get(i))));
    }

    /**
     * Updates this grid with the values of {@code a} which are transformed by
     * the given function {@code f}.
     * <pre>{@code
     * this[i] = f(this[i], a[i])
     * }</pre>
     *
     * @param a the grid used for the update
     * @param f the combiner function
     */
    public void assign(final BaseIntGrid1d<?> a, final IntBinaryOperator f) {
        checkSameExtent(extent(), a.extent());
        forEach(i -> set(i, f.applyAsInt(get(i), a.get(i))));
    }

    /**
     * Swaps each element {@code this[i]} with {@code other[i]}.
     *
     * @throws IllegalArgumentException if {@code size() != other.size()}.
     */
    public void swap(final BaseIntGrid1d<?> other) {
        checkSameExtent(extent(), other.extent());
        forEach(i -> {
            final var tmp = get(i);
            set(i, other.get(i));
            other.set(i, tmp);
        });
    }

    /**
     * Applies a function to each cell and aggregates the results.
     *
     * @param reducer an aggregation function taking as first argument the
     *        current aggregation and as second argument the transformed current
     *        cell value
     * @param f a function transforming the current cell value
     * @return the aggregated measure or {@link OptionalInt#empty()} if
     *         {@code size() == 0}
     */
    public OptionalInt
    reduce(final IntBinaryOperator reducer, final IntUnaryOperator f) {
        requireNonNull(reducer);
        requireNonNull(f);

        if (size() == 0) {
            return OptionalInt.empty();
        }

        var a = f.applyAsInt(get(size() - 1));
        for (int i = size() - 1; --i >= 0;) {
            a = reducer.applyAsInt(a, f.applyAsInt(get(i)));
        }

        return OptionalInt.of(a);
    }

    /**
     * Checks whether the given matrices have the same dimension and contains
     * the same values.
     *
     * @param other the second matrix to compare
     * @return {@code true} if the two given matrices are equal, {@code false}
     *         otherwise
     */
    public boolean equals(final BaseIntGrid1d<?> other) {
        return extent().equals(other.extent()) &&
            allMatch(i -> get(i) == other.get(i));
    }

    @Override
    public int hashCode() {
        final int[] hash = new int[] { 37 };
        forEach(i -> hash[0] += Integer.hashCode(get(i))*17);
        return hash[0];
    }

    @Override
    public boolean equals(final Object object) {
        return object == this ||
            object instanceof BaseIntGrid1d<?> grid &&
                equals(grid);
    }

    @Override
    public String toString() {
        final var out = new StringBuilder();
        out.append("[");
        for (int i = 0; i < size(); ++i) {
            out.append(get(i));
            if (i < size() - 1) {
                out.append(", ");
            }
        }
        out.append("]");
        return out.toString();
    }

}
