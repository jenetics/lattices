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
package io.jenetics.linealgebra.grid;

import static java.util.Objects.requireNonNull;

import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

import io.jenetics.linealgebra.array.DoubleArray;

/**
 * Generic class for 2-d grids holding {@code double} elements.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
public class DoubleGrid2d implements Structural2d {

    protected final Structure2d structure;
    protected final DoubleArray elements;

    public DoubleGrid2d(
        final Structure2d structure,
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
    public Structure2d structure() {
        return structure;
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
    public double get(final int row, final int col) {
        return elements.get(order().index(row, col));
    }

    /**
     * Sets the matrix cell at coordinate {@code [row, col]} to the specified
     * {@code value}.
     *
     * @param row the index of the row-coordinate
     * @param col the index of the column-coordinate
     * @param value  the value to be filled into the specified cell
     * @throws IndexOutOfBoundsException if the given coordinates are out of
     *         bounds
     */
    public void set(final int row, final int col, final double value) {
        elements.set(order().index(row, col),  value);
    }

    /**
     * Replaces all cell values of the receiver with the values of another
     * matrix. Both matrices must have the same number of rows and columns.
     *
     * @param other the source matrix to copy from (maybe identical to the
     *        receiver).
     * @throws IllegalArgumentException if {@code !extent().equals(other.extent())}
     */
    public void assign(final DoubleGrid2d other) {
        if (other == this) {
            return;
        }
        requireSameExtent(other.extent());

        for (int r = rows(); --r >= 0; ) {
            for (int c = cols(); --c >= 0; ) {
                set(r, c, other.get(r, c));
            }
        }
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
    public void assign(final double[][] values) {
        if (values.length != rows()) {
            throw new IllegalArgumentException(
                "Values must have the same number of rows: " +
                    values.length + " != " + rows()
            );
        }

        for (int r = rows(); --r >= 0;) {
            final double[] row = values[r];

            if (row.length != cols()) {
                throw new IllegalArgumentException(
                    "Values must have the same number of columns: " +
                        row.length + " != " + cols()
                );
            }

            for (int c = cols(); --c >= 0;) {
                set(r, c, row[c]);
            }
        }
    }

    /**
     * Sets all cells to the state specified by {@code values}.
     *
     * @param value the value to be filled into the cells
     */
    public void assign(final double value) {
        for (int r = rows(); --r >= 0; ) {
            for (int c = cols(); --c >= 0; ) {
                set(r, c, value);
            }
        }
    }

    /**
     * Assigns the result of a function to each cell {@code x[row, col] =
     * f(x[row, col], y[row, col])}.
     *
     * @param y the secondary matrix to operate on.
     * @param f a function object taking as first argument the current cell's
     *          value of {@code this}, and as second argument the current cell's
     *          value of {@code y}
     * @throws IllegalArgumentException if {@code !extent().equals(y.extent())}
     */
    public void assign(
        final DoubleGrid2d y,
        final DoubleBinaryOperator f
    ) {
        requireNonNull(f);
        requireSameExtent(y.extent());

        for (int r = rows(); --r >= 0; ) {
            for (int c = cols(); --c >= 0; ) {
                set(r, c, f.applyAsDouble(get(r, c), y.get(r, c)));
            }
        }
    }

    /**
     * Assigns the result of a function to each cell
     * {@code x[row, col] = f(x[row, col])}.
     *
     * @param f a function object taking as argument the current cell's value.
     */
    public void update(final DoubleUnaryOperator f) {
        for (int r = rows(); --r >= 0; ) {
            for (int c = cols(); --c >= 0; ) {
                set(r, c, f.applyAsDouble(get(r, c)));
            }
        }
    }

    /**
     * Applies a function to each cell and aggregates the results. Returns a
     * value <em>v</em> such that <em>v==a(size())</em> where
     * <em>a(i) == reduce(a(i - 1), f(get(row, col)))</em> and terminators are
     * <em>a(1) == f(get(0,0)), a(0) == Double.NaN</em>.
     * <p><b>Example:</b></p>
     * <pre>
     * 2 x 2 matrix
     * 0 1
     * 2 3
     *
     * // Sum(x[row, col]*x[row, col])
     * matrix.aggregate(Double::sum, a -> a*a) --> 14
     * </pre>
     *
     * @param reducer an aggregation function taking as first argument the
     *        current aggregation and as second argument the transformed current
     *        cell value
     * @param f a function transforming the current cell value
     * @return the aggregated value
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

        double a = f.applyAsDouble(get(rows() - 1, cols() - 1));
        int d = 1;
        for (int r = rows(); --r >= 0;) {
            for (int c = cols() - d; --c >= 0;) {
                a = reducer.applyAsDouble(a, f.applyAsDouble(get(r, c)));
            }
            d = 0;
        }
        return a;
    }

    /**
     * Checks whether the given matrices have the same dimension and contains
     * the same values.
     *
     * @param other the second matrix to compare
     * @param error the allowed relative error
     * @return {@code true} if the two given matrices are equal, {@code false}
     *         otherwise
     */
    public boolean equals(final DoubleGrid2d other, final double error) {
        return extent().equals(other.extent()) &&
            allMatch((r, c) -> equals(r, c, this, other, error));
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

    /* *************************************************************************
     * Static matrix helper methods.
     * ************************************************************************/

    static boolean equals(
        final int r,
        final int c,
        final DoubleGrid2d a,
        final DoubleGrid2d b,
        final double error
    ) {
        final double v1 = a.get(r, c);
        final double v2 = b.get(r, c);

        return Math.abs(v1 - v2) <= Math.abs(v1*error);
    }

}
