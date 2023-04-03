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

import java.util.OptionalDouble;
import java.util.function.BiFunction;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

import io.jenetics.lattices.NumericalContext;
import io.jenetics.lattices.array.DoubleArray;
import io.jenetics.lattices.structure.Extent1d;
import io.jenetics.lattices.structure.Structure1d;

/**
 * Abstract double grid implementation.
 *
 * @param <G> the grid type
 */
public abstract class BaseDoubleGrid1d<G extends BaseDoubleGrid1d<G>>
    implements Grid1d<DoubleArray, G>
{

    /**
     * The structure, which defines the <em>extent</em> of the grid and the
     * <em>order</em> which determines the index mapping {@code N -> N}.
     */
    private final Structure1d structure;

    /**
     * The underlying {@code double[]} array.
     */
    private final DoubleArray array;

    private final BiFunction<Structure1d, DoubleArray, G> constructor;

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
    protected BaseDoubleGrid1d(
        final Structure1d structure,
        final DoubleArray array,
        final BiFunction<? super Structure1d, ? super DoubleArray, ? extends G> constructor
    ) {
        checkArraySize(structure, array.length());

        this.structure = structure;
        this.array = array;

        @SuppressWarnings("unchecked")
        final var ctr = (BiFunction<Structure1d, DoubleArray, G>)constructor;
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
    public DoubleArray array() {
        return array;
    }

    @Override
    public G create(final Structure1d structure, final DoubleArray array) {
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
    public double get(final int index) {
        return array.get(mapper().offset(index));
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
    public void set(final int index, final double value) {
        array.set(mapper().offset(index),  value);
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
    public void assign(final double[] values) {
        checkSameExtent(extent(), new Extent1d(values.length));
        forEach(i -> set(i, values[i]));
    }

    /**
     * Sets all cells to the state specified by {@code values}.
     *
     * @param value the value to be filled into the cells
     */
    public void assign(final double value) {
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
    public void assign(final DoubleUnaryOperator f) {
        requireNonNull(f);
        forEach(i -> set(i, f.applyAsDouble(get(i))));
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
    public void assign(final BaseDoubleGrid1d<?> a, final DoubleBinaryOperator f) {
        checkSameExtent(extent(), a.extent());
        forEach(i -> set(i, f.applyAsDouble(get(i), a.get(i))));
    }

    /**
     * Swaps each element {@code this[i]} with {@code other[i]}.
     *
     * @throws IllegalArgumentException if {@code size() != other.size()}.
     */
    public void swap(final BaseDoubleGrid1d<?> other) {
        checkSameExtent(extent(), other.extent());
        forEach(i -> {
            final var tmp = get(i);
            set(i, other.get(i));
            other.set(i, tmp);
        });
    }

    /**
     * Applies a function to each cell and aggregates the results.
     * Returns a value {@code v} such that {@code v == a(size())} where
     * {@code a(i) == reducer( a(i - 1), f(get(i)) )} and terminators are
     * {@code a(1) == f(get(0))}.
     *
     * @param reducer an aggregation function taking as first argument the
     *        current aggregation and as second argument the transformed current
     *        cell value
     * @param f a function transforming the current cell value
     * @return the aggregated measure or {@link OptionalDouble#empty()} if
     *         {@code size() == 0}
     */
    public OptionalDouble
    reduce(final DoubleBinaryOperator reducer, final DoubleUnaryOperator f) {
        requireNonNull(reducer);
        requireNonNull(f);

        if (size() == 0) {
            return OptionalDouble.empty();
        }

        double a = f.applyAsDouble(get(size() - 1));
        for (int i = size() - 1; --i >= 0;) {
            a = reducer.applyAsDouble(a, f.applyAsDouble(get(i)));
        }

        return OptionalDouble.of(a);
    }

    /**
     * Checks whether the given matrices have the same dimension and contains
     * the same values.
     *
     * @param other the second matrix to compare
     * @return {@code true} if the two given matrices are equal, {@code false}
     *         otherwise
     */
    public boolean equals(final BaseDoubleGrid1d<?> other) {
        final var context = NumericalContext.get();

        return extent().equals(other.extent()) &&
            allMatch(i -> context.equals(get(i), other.get(i)));
    }

    @Override
    public int hashCode() {
        final int[] hash = new int[] { 37 };
        forEach(i -> hash[0] += Double.hashCode(get(i))*17);
        return hash[0];
    }

    @Override
    public boolean equals(final Object object) {
        return object == this ||
            object instanceof BaseDoubleGrid1d<?> grid &&
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
