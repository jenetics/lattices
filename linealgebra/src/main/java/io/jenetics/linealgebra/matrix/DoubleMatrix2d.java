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
import io.jenetics.linealgebra.structure.Extent2d;
import io.jenetics.linealgebra.structure.Structure2d;

/**
 * Generic class for 2-d matrices holding {@code double} elements.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
public class DoubleMatrix2d implements Matrix2d<DoubleMatrix2d> {

    /**
     * Factory for creating dense 2-d double matrices.
     */
    public static final Factory<DoubleMatrix2d> DENSE_FACTORY = struct ->
        new DoubleMatrix2d(
            struct,
            DenseDoubleArray.ofSize(struct.extent().size())
        );

    private final Structure2d structure;
    private final DoubleArray elements;

    public DoubleMatrix2d(final Structure2d structure, final DoubleArray elements) {
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

    @Override
    public Structure2d structure() {
        return structure;
    }

    @Override
    public DoubleMatrix2d view(final Structure2d struct) {
        return new DoubleMatrix2d(struct, elements);
    }

    @Override
    public DoubleMatrix2d copy(final Structure2d struct) {
        final var elems = elements.newArrayOfSize(size());
        extent().forEach((r, c) -> elems.set(struct.order().index(r, c), get(r, c)));

        return new DoubleMatrix2d(struct, elems);
    }

    @Override
    public Factory<DoubleMatrix2d> factory() {
        return struct -> new DoubleMatrix2d(
            struct,
            elements.newArrayOfSize(struct.extent().size())
        );
    }

    /* *************************************************************************
     * Matrix view methods.
     * ************************************************************************/

    /**
     * Constructs and returns a <em>view</em> representing the rows of the given
     * column. The returned view is backed by this matrix, so changes in the
     * returned view are reflected in this matrix, and vice-versa.
     *
     * @param index the column index.
     * @return a new column view.
     * @throws IndexOutOfBoundsException if {@code index < 0 || index >= cols()}
     * @throws UnsupportedOperationException if the {@link #order()} function
     *         is not an instance of {@link io.jenetics.linealgebra.structure.MajorOrder2d}
     */
    public DoubleMatrix1d col(final int index) {
        return new DoubleMatrix1d(structure.col(index), elements);
    }

    /**
     * Constructs and returns a <em>view</em> representing the columns of the
     * given row. The returned view is backed by this matrix, so changes in the
     * returned view are reflected in this matrix, and vice-versa.
     *
     * @param index the row index.
     * @return a new row view.
     * @throws IndexOutOfBoundsException if {@code index < 0 || index >= rows()}
     * @throws UnsupportedOperationException if the {@link #order()} function
     *         is not an instance of {@link io.jenetics.linealgebra.structure.MajorOrder2d}
     */
    public DoubleMatrix1d row(final int index) {
        return new DoubleMatrix1d(structure.row(index), elements);
    }

    /* *************************************************************************
     * Additional matrix methods.
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
        final DoubleMatrix2d y,
        final DoubleBinaryOperator f
    ) {
        requireNonNull(f);
        checkDim(y.extent());

        for (int r = rows(); --r >= 0; ) {
            for (int c = cols(); --c >= 0; ) {
                set(r, c, f.applyAsDouble(get(r, c), y.get(r, c)));
            }
        }
    }

    private void checkDim(final Extent2d other) {
        if (!extent().equals(other)) {
            throw new IllegalArgumentException(
                "Incompatible dimensions: %s != %s.".formatted(extent(), extent())
            );
        }
    }

    /**
     * Replaces all cell values of the receiver with the values of another
     * matrix. Both matrices must have the same number of rows and columns.
     *
     * @param other the source matrix to copy from (maybe identical to the
     *        receiver).
     * @throws IllegalArgumentException if {@code cols() != other.cols() ||
     *         rows() != other.rows()}
     */
    public void assign(final DoubleMatrix2d other) {
        if (other == this) {
            return;
        }
        checkDim(other.extent());

        for (int r = rows(); --r >= 0; ) {
            for (int c = cols(); --c >= 0; ) {
                set(r, c, other.get(r, c));
            }
        }
    }

    /**
     * Assigns the result of a function to each cell
     * {@code x[row, col] = f(x[row, col])}.
     *
     * @param f a function object taking as argument the current cell's value.
     */
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
     * Linear algebraic matrix-vector multiplication
     * <pre>
     *     z = alpha * A * y + beta*z
     *     z[i] = alpha*Sum(A[i, j] * y[j]) + beta*z[i],
     *           i = 0..A.rows() - 1, j = 0..y.size() - 1
     *     where
     *     A == this
     * </pre>
     *
     * @implNote
     * Matrix shape conformance is checked <em>after</em> potential
     * transpositions.
     *
     * @param y the source vector.
     * @param z the vector where results are to be stored. Set this parameter to
     *          {@code null} to indicate that a new result vector should be
     *          constructed.
     * @return z, or a newly created result matrix
     * @throws IllegalArgumentException if {@code A.cols() != y.size() ||
     *         A.rows() > z.size())}.
     */
    public DoubleMatrix1d mult(
        final DoubleMatrix1d y,
        DoubleMatrix1d z,
        final double alpha,
        final double beta,
        final boolean transposeA
    ) {
        if (transposeA) {
            return view(structure().transpose())
                .mult(y, z, alpha, beta, false);
        }

        if (z == null) {
            final var struct = new Matrix1d.Structure(new Matrix1d.Dim(rows()));
            final var elems = elements.newArrayOfSize(struct.dim().size());
            z = new DoubleMatrix1d(struct, elems);
        }

        if (cols() != y.size() || rows() > z.size()) {
            throw new IllegalArgumentException(
                "Incompatible args: " + extent() + ", " + y.dim() + ", " + z.dim()
            );
        }

        for (int i = rows(); --i >= 0; ) {
            double s = 0;
            for (int j = cols(); --j >= 0;) {
                s += get(i, j) * y.get(j);
            }
            z.set(i, alpha * s + beta * z.get(i));
        }

        return z;
    }

    /**
     * Linear algebraic matrix-vector multiplication; {@code z = A * y};
     * Equivalent to {@code return A.mult(y, z, 1, 0);}
     *
     * @see #mult(DoubleMatrix1d, DoubleMatrix1d, double, double, boolean)
     *
     * @param y the source vector.
     * @param z the vector where results are to be stored. Set this parameter to
     *          {@code null} to indicate that a new result vector should be
     *          constructed.
     * @return z, or a newly created result matrix
     */
    public DoubleMatrix1d mult(final DoubleMatrix1d y, final DoubleMatrix1d z) {
        return mult(y, z, 1, (z == null ? 1 : 0), false);
    }

    /**
     * <em>Linear algebraic matrix-matrix multiplication:</em>
     * <pre>
     *     C = alpha * A x B + beta*C
     *     C[i, j] = alpha*Sum(A[i, k] * B[k, j]) + beta*C[i, j], k = 0..n-1
     * </pre>
     * <em>Matrix shapes:</em>
     * <pre>
     *     A(m x n), B(n x p), C(m x p)
     * </pre>
     *
     * @implNote
     * Matrix shape conformance is checked <em>after</em> potential
     * transpositions.
     *
     * @param B the second source matrix.
     * @param C the matrix where results are to be stored. Set this parameter to
     *          {@code null} to indicate that a new result matrix should be
     *          constructed.
     * @return C, or a newly created result matrix
     * @throws IllegalArgumentException if {@code B.rows() != A.columns()} or
     *         {@code C.rows() != A.rows() || C.cols() != B.cols()} or
     *         {@code A == C || B == C}
     */
    public DoubleMatrix2d mult(
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
                .mult(B, C, alpha, beta, false, transposeB);
        }
        if (transposeB) {
            return mult(
                B.view(B.structure().transpose()), C,
                alpha, beta, transposeA, false
            );
        }

        final int m = rows();
        final int n = cols();
        final int p = B.cols();

        if (C == null) {
            final var struct = new Structure2d(new Extent2d(m, p));
            final var elems = elements.newArrayOfSize(struct.extent().size());
            C = new DoubleMatrix2d(struct, elems);
        }

        if (B.rows() != n) {
            throw new IllegalArgumentException(
                "2-d matrix inner dimensions must equal:" +
                    extent() + ", " + B.extent()
            );
        }
        if (C.rows() != m || C.cols() != p) {
            throw new IllegalArgumentException(
                "Incompatible result matrix: " +
                    extent() + ", " + B.extent() + ", " + C.extent()
            );
        }

        if (this == C || B == C) {
            throw new IllegalArgumentException("Matrices must not be identical.");
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
     * Linear algebraic matrix-matrix multiplication {@code C = A x B}, which is
     * equivalent to {@code A.mult(B, C, 1, 0, false, false)}.
     *
     * @see #mult(DoubleMatrix2d, DoubleMatrix2d, double, double, boolean, boolean)
     *
     * @param B the second source matrix.
     * @param C the matrix where results are to be stored. Set this parameter to
     *          {@code null} to indicate that a new result matrix should be
     *          constructed.
     * @return C, or a newly created result matrix.
     * @throws IllegalArgumentException if {@code B.rows() != A.columns()} or
     *         {@code C.rows() != A.rows() || C.cols() != B.cols()} or
     *         {@code A == C || B == C}
     */
    public DoubleMatrix2d mult(final DoubleMatrix2d B, final DoubleMatrix2d C) {
        return mult(B, C, 1, (C == null ? 1 : 0), false, false);
    }

    /**
     * Return the sum of all cells: {@code Sum(x[i, j])}.
     *
     * @return the sum of all cells
     */
    public double sum() {
        if (size() == 0) {
            return 0;
        } else {
            return reduce(Double::sum, DoubleUnaryOperator.identity());
        }
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof DoubleMatrix2d m &&
            equals(this, m, 0.0);
    }

    /* *************************************************************************
     * Static matrix helper methods.
     * ************************************************************************/

    /**
     * Checks whether the given matrices have the same dimension and contains
     * the same values.
     *
     * @param a the first matrix
     * @param b the second matrix
     * @param error the allowed relative error
     * @return {@code true} if the two given matrices are equal, {@code false}
     *         otherwise
     */
    public static boolean equals(
        final DoubleMatrix2d a,
        final DoubleMatrix2d b,
        final double error
    ) {
        return a.extent().equals(b.extent()) &&
            a.extent().allMatch((r, c) -> equals(r, c, a, b, error));
    }

    private static boolean equals(
        final int r,
        final int c,
        final DoubleMatrix2d a,
        final DoubleMatrix2d b,
        final double error
    ) {
        final double v1 = a.get(r, c);
        final double v2 = b.get(r, c);

        return Math.abs(v1 - v2) <= Math.abs(v1*error);
    }

}
