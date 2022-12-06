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
package io.jenetics.linealgebra.matrix;

import static java.util.Objects.requireNonNull;

import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

import io.jenetics.linealgebra.array.DenseDoubleArray;
import io.jenetics.linealgebra.array.DoubleArray;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
public class DoubleMatrix1d implements Matrix1d<DoubleMatrix1d> {

    /**
     * Factory for creating dense 1-d double matrices.
     */
    public static final Factory<DoubleMatrix1d> DENSE_FACTORY = struct ->
        new DoubleMatrix1d(
            struct,
            DenseDoubleArray.ofSize(struct.dim().size())
        );

    private final Structure structure;
    private final DoubleArray elements;

    public DoubleMatrix1d(final Structure structure, final DoubleArray elements) {
        if (structure.dim().size() > elements.size()) {
            throw new IllegalArgumentException(
                "The number of available elements is smaller than the number of " +
                    "required matrix cells: %d < %d."
                        .formatted(structure.dim().size(), elements.size())
            );
        }

        this.structure = structure;
        this.elements = elements;
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

    @Override
    public Structure structure() {
        return structure;
    }

    @Override
    public DoubleMatrix1d view(final Structure structure) {
        return new DoubleMatrix1d(structure, elements);
    }

    @Override
    public DoubleMatrix1d copy(final Structure structure) {
        return new DoubleMatrix1d(structure, elements.copy());
    }

    @Override
    public Factory<DoubleMatrix1d> factory() {
        return struct -> new DoubleMatrix1d(
            struct,
            elements.newArrayOfSize(struct.dim().size())
        );
    }

    /* *************************************************************************
     * Additional matrix methods.
     * ************************************************************************/

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
        for (int i = size(); --i >= 0;) {
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

        for (int i = size(); --i >= 0;) {
            set(i, f.applyAsDouble(get(i)));
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

    /**
     * Returns the dot product of two vectors x and y, which is
     * {@code Sum(x[i]*y[i])}, where {@code x == this}.
     * Operates on cells at indexes {@code from ..
     * Min(size(), y.size(), from + length) - 1}.
     *
     * @param y the second vector
     * @param from the first index to be considered
     * @param length the number of cells to be considered
     * @return the sum of products, zero if {@code from<0 || length<0}
     */
    public double dotProduct(
        final DoubleMatrix1d y,
        final int from,
        final int length
    ) {
        if (from < 0 || length <= 0) {
            return 0;
        }

        int tail = from + length;
        if (size() < tail) {
            tail = size();
        }
        if (y.size() < tail) {
            tail = y.size();
        }
        int l = tail - from;

        double sum = 0;
        int i = tail - 1;
        for (int k = l; --k >= 0; i--) {
            sum += get(i) * y.get(i);
        }
        return sum;
    }

    /**
     * Returns the dot product of two vectors x and y, which is {
     * @code Sum(x[i]*y[i])}, where {@code x == this}.
     * Operates on cells at indexes {@code 0 .. Math.min(size(), y.size())}.
     *
     * @param y the second vector
     * @return the sum of products
     */
    public double dotProduct(final DoubleMatrix1d y) {
        return dotProduct(y, 0, size());
    }

    /**
     * Returns the sum of all cells {@code Sum(x[i])}.
     *
     * @return the sum
     */
    public double sum() {
        if (size() == 0) {
            return 0;
        } else {
            return reduce(Double::sum, DoubleUnaryOperator.identity());
        }
    }

}
