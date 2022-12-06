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
package io.jenetics.linealgebra;

import static java.util.Objects.requireNonNull;

import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

/**
 * Interface for 2-d matrices holding {@code double} elements.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
public interface DoubleMatrix2D extends Matrix2D<DoubleMatrix2D> {

    /**
     * Returns the matrix cell value at coordinate {@code [row,col]}.
     *
     * @param row the index of the row-coordinate.
     * @param col the index of the column-coordinate.
     * @return the value of the specified cell.
     * @throws IndexOutOfBoundsException if the given coordinates are out of
     *         bounds
     */
    double get(final int row, final int col);

    /**
     * Sets the matrix cell at coordinate {@code [row,col]} to the specified
     * {@code value}.
     *
     * @param row the index of the row-coordinate.
     * @param col the index of the column-coordinate.
     * @param value  the value to be filled into the specified cell.
     * @throws IndexOutOfBoundsException if the given coordinates are out of
     *         bounds
     */
    void set(final int row, final int col, final double value);


    /* *************************************************************************
     * Default implementation of this double-matrix.
     * ************************************************************************/

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
     * @throws IllegalArgumentException if {@code values.length != rows()} or
     *         for any {@code 0 <= row <= rows(): values[row].length != columns()}
     */
    default void assign(final double[][] values) {
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
     * matrix.aggregate(Double::sum, a -> a*a);
     * --> 14
     * </pre>
     *
     * @param reducer an aggregation function taking as first argument the
     *        current aggregation and as second argument the transformed current
     *        cell value
     * @param f a function transforming the current cell value
     * @return the aggregated value
     */
    default double reduce(
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
     * Return the sum of all cells: {@code Sum(x[i, j])}.
     *
     * @return the sum of all cells
     */
    default double zSum() {
        if (size() == 0) {
            return 0;
        } else {
            return reduce(Double::sum, DoubleUnaryOperator.identity());
        }
    }

}
