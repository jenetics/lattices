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

import io.jenetics.linealgebra.array.DenseDoubleArray;
import io.jenetics.linealgebra.array.DoubleArray;

import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

import static java.util.Objects.requireNonNull;

/**
 * Generic class for 2-d matrices holding {@code double} elements.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
public class DoubleMatrix2d implements Matrix2d<DoubleMatrix2d> {

    public static final Factory<DoubleMatrix2d> DENSE_FACTORY = struct ->
        new DoubleMatrix2d(
            struct,
            DenseDoubleArray.ofSize(struct.dim().size())
        );

    private final Structure structure;
    private final DoubleArray elements;

    public DoubleMatrix2d(final Structure structure, final DoubleArray elements) {
        if (structure.dim().size() < elements.size()) {
            throw new IllegalArgumentException(
                "The number of available elements is smaller than the number of " +
                    "required matrix cells: %d < %d."
                        .formatted(structure.dim().size(), elements.size())
            );
        }

        this.structure = structure;
        this.elements = elements;
    }

    @Override
    public Structure structure() {
        return structure;
    }

    /**
     * Returns the matrix cell value at coordinate {@code [row,col]}.
     *
     * @param row the index of the row-coordinate.
     * @param col the index of the column-coordinate.
     * @return the value of the specified cell.
     * @throws IndexOutOfBoundsException if the given coordinates are out of
     *         bounds
     */
    public double get(final int row, final int col) {
        return elements.get(order().index(row, col));
    }

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
    public void set(final int row, final int col, final double value) {
        elements.set(order().index(row, col),  value);
    }

    @Override
    public DoubleMatrix2d view(final Structure structure) {
        return new DoubleMatrix2d(structure, elements);
    }

    @Override
    public DoubleMatrix2d copy(final Structure structure) {
        return new DoubleMatrix2d(structure, elements.copy());
    }

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

    public void assign(final DoubleUnaryOperator f) {
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

    public DoubleMatrix2d zMult(
        final DoubleMatrix2d B,
        DoubleMatrix2d C,
        final double alpha,
        final double beta,
        final boolean transposeA,
        final boolean transposeB
    ) {
        requireNonNull(B);

        if (transposeA) {
            return view(structure().transpose())
                .zMult(B, C, alpha, beta, false, transposeB);
        }
        if (transposeB) {
            return zMult(
                B.view(B.structure().transpose()), C,
                alpha, beta, transposeA, false
            );
        }

        final int m = rows();
        final int n = cols();
        final int p = B.cols();

        if (C == null) {
            final var struct = new Structure(new Dim(m, p));
            final var elems = elements.newArrayOfSize(struct.dim().size());
            C = new DoubleMatrix2d(structure, elems);
        }

        if (B.rows() != n) {
            throw new IllegalArgumentException(
                "Matrix2D inner dimensions must agree:" +
                    toStringShort() + ", " + B.toStringShort()
            );
        }
        if (C.rows() != m || C.cols() != p) {
            throw new IllegalArgumentException(
                "Incompatibel result matrix: " +
                    toStringShort() + ", " + B.toStringShort() + ", " + C.toStringShort()
            );
        }

        if (this == C || B == C) {
            throw new IllegalArgumentException("Matrices must not be identical");
        }

        for (int j = p; --j >= 0; ) {
            for (int i = m; --i >= 0; ) {
                double s = 0;
                for (int k = n; --k >= 0; ) {
                    s += get(i, k) * B.get(k, j);
                }
                C.set(i, j, alpha * s + beta * C.get(i, j));
            }
        }

        return C;
    }

    /**
     * Return the sum of all cells: {@code Sum(x[i, j])}.
     *
     * @return the sum of all cells
     */
    public double zSum() {
        if (size() == 0) {
            return 0;
        } else {
            return reduce(Double::sum, DoubleUnaryOperator.identity());
        }
    }

}
