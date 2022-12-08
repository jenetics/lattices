/*
 * Java Linear Algebra Library (@__identifier__@).
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
package io.jenetics.linealgebra.structure;

import static java.util.Objects.requireNonNull;

import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

import io.jenetics.linealgebra.array.DoubleArray;

/**
 * Generic class for 1-d grids holding {@code double} elements.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
public class DoubleGrid1d implements Structural1d {

    protected final Structure1d structure;
    protected final DoubleArray elements;

    public DoubleGrid1d(
        final Structure1d structure,
        final DoubleArray elements
    ) {
        if (structure.extent().size() > elements.size()) {
            throw new IllegalArgumentException(
                "The number of available elements is smaller than the number of " +
                    "required matrix cells: %d > %d."
                        .formatted(structure.extent().size(), elements.size())
            );
        }

        this.structure = structure;
        this.elements = elements;
    }

    @Override
    public Structure1d structure() {
        return structure;
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
        return elements.get(order().index(index));
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
        elements.set(order().index(index),  value);
    }

    /**
     * Replaces all cell values of the receiver with the values of another
     * matrix. Both matrices must have the same number of rows and columns.
     *
     * @param other the source matrix to copy from (maybe identical to the
     *        receiver).
     * @throws IllegalArgumentException if {@code !extent().equals(other.extent())}
     */
    public void assign(final DoubleGrid1d other) {
        if (other == this) {
            return;
        }
        requireSameExtent(other.extent());

        for (int i = 0; i < size(); ++i) {
            set(i, other.get(i));
        }
    }

    /**
     * Sets all cells to the state specified by {@code values}.
     *
     * @param values the values to be filled into the cells
     */
    public void assign(final double[] values) {
        for (int i = 0; i < Math.min(values.length, size()); ++i) {
            set(i, values[i]);
        }
    }

    /**
     * Sets all cells to the state specified by {@code values}.
     *
     * @param value the value to be filled into the cells
     */
    public void assign(final double value) {
        for (int i = 0; i < size(); ++i) {
            set(i, value);
        }
    }

    /**
     * Assigns the result of a function to each cell: {@code x[i] = f(x[i])}.
     *
     * @param f a function object taking as argument the current cell's value.
     */
    public void assign(final DoubleUnaryOperator f) {
        requireNonNull(f);

        for (int i = 0; i < size(); ++i) {
            set(i, f.applyAsDouble(get(i)));
        }
    }

    /**
     * Swaps each element {@code this[i]} with {@code other[i]}.
     *
     * @throws IllegalArgumentException if {@code size() != other.size()}.
     */
    public void swap(final DoubleGrid1d other) {
        requireSameExtent(other.extent());

        for (int i = size(); --i >= 0;) {
            final var tmp = get(i);
            set(i, other.get(i));
            other.set(i, tmp);
        }
    }

    /**
     * Applies a function to each cell and aggregates the results.
     * Returns a value {@code v} such that {@code v == a(size())} where
     * {@code a(i) == reducer( a(i - 1), f(get(i)) )} and terminators are
     * {@code a(1) == f(get(0)), a(0)==Double.NaN}.
     *
     * @param reducer an aggregation function taking as first argument the
     *        current aggregation and as second argument the transformed current
     *        cell value
     * @param f a function transforming the current cell value
     * @return the aggregated measure
     */
    public double reduce(
        final DoubleBinaryOperator reducer,
        final DoubleUnaryOperator f
    ) {
        requireNonNull(reducer);
        requireNonNull(f);

        if (size() == 0) {
            return Double.NaN;
        }

        double a = f.applyAsDouble(get(size() - 1));
        for (int i = size() - 1; --i >= 0;) {
            a = reducer.applyAsDouble(a, f.applyAsDouble(get(i)));
        }

        return a;
    }

}
