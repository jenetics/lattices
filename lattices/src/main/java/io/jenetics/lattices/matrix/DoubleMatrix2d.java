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
package io.jenetics.lattices.matrix;

import static java.util.Objects.requireNonNull;
import static io.jenetics.lattices.matrix.DenseDoubleMatrix2dMult.denseMult;
import static io.jenetics.lattices.matrix.DenseDoubleMatrix2dMult.isDense;

import java.util.function.DoubleUnaryOperator;

import io.jenetics.lattices.grid.Grid2d;
import io.jenetics.lattices.grid.array.DenseDoubleArray;
import io.jenetics.lattices.grid.array.DoubleArray;
import io.jenetics.lattices.grid.lattice.DoubleLattice2d;
import io.jenetics.lattices.grid.lattice.Lattice2d;
import io.jenetics.lattices.structure.Extent1d;
import io.jenetics.lattices.structure.Extent2d;
import io.jenetics.lattices.structure.Projection2d;
import io.jenetics.lattices.structure.Structure1d;
import io.jenetics.lattices.structure.Structure2d;
import io.jenetics.lattices.structure.View2d;

/**
 * Generic class for 2-d matrices holding {@code double} elements. Instances
 * of this class are usually created via a factory.
 * <pre>{@code
 * final DoubleMatrix2d matrix5x10 = DoubleMatrix2d.DENSE.create(5, 10);
 * }</pre>
 *
 * @see #DENSE
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record DoubleMatrix2d(Structure2d structure, DoubleArray array)
    implements DoubleLattice2d<DoubleArray>, Grid2d<DoubleArray, DoubleMatrix2d>
{

    /**
     * Factory for creating <em>dense</em> 2-d double matrices.
     */
    public static final Grid2d.Factory<DoubleMatrix2d> DENSE =
        extent -> new DoubleMatrix2d(
            new Structure2d(extent),
            DenseDoubleArray.ofSize(extent.cells())
        );

    /**
     * Create a new matrix view from the given lattice.
     *
     * @param lattice the underlying lattice data
     */
    public DoubleMatrix2d(Lattice2d<? extends DoubleArray> lattice) {
        this(lattice.structure(), lattice.array());
    }

    @Override
    public DoubleMatrix2d create(Structure2d structure, DoubleArray array) {
        return new DoubleMatrix2d(structure, array);
    }

    @Override
    public void assign(DoubleMatrix2d other) {
        DoubleLattice2d.super.assign(other);
    }

    /* *************************************************************************
     * Matrix view methods.
     * ************************************************************************/

    public DoubleMatrix2d view(View2d view) {
        return new DoubleMatrix2d(view.apply(structure), array);
    }

    /**
     * Return a <em>transposed</em> view of this matrix.
     *
     * @return a <em>transposed</em> view of this matrix
     */
    public DoubleMatrix2d transpose() {
        return view(View2d.TRANSPOSE);
    }

    /**
     * Return a 1-d projection from this 2-d matrix. The returned 1-d matrix is
     * a view onto this matrix {@link #array()}.
     *
     * @param projection the projection to apply
     * @return a 1-d projection from this 2-d matrix
     */
    public DoubleMatrix1d project(Projection2d projection) {
        return new DoubleMatrix1d(projection.apply(structure()), array());
    }

    /**
     * Constructs and returns a <em>view</em> representing the rows of the given
     * column. The returned view is backed by this matrix, so changes in the
     * returned view are reflected in this matrix, and vice-versa.
     *
     * @see #project(Projection2d)
     *
     * @param index the column index.
     * @return a new column view.
     * @throws IndexOutOfBoundsException if {@code index < 0 || index >= cols()}
     */
    public DoubleMatrix1d colAt(int index) {
        return project(Projection2d.col(index));
    }

    /**
     * Constructs and returns a <em>view</em> representing the columns of the
     * given row. The returned view is backed by this matrix, so changes in the
     * returned view are reflected in this matrix, and vice-versa.
     *
     * @see #project(Projection2d)
     *
     * @param index the row index.
     * @return a new row view.
     * @throws IndexOutOfBoundsException if {@code index < 0 || index >= rows()}
     */
    public DoubleMatrix1d rowAt(int index) {
        return project(Projection2d.row(index));
    }

    /* *************************************************************************
     * Matrix algebra methods.
     * ************************************************************************/

    /**
     * Linear algebraic matrix-vector multiplication
     * <pre>{@code
     *     z = alpha * A * y + beta*z
     *     z[i] = alpha*Sum(A[i, j] * y[j]) + beta*z[i],
     *           i = 0..A.rows() - 1, j = 0..y.size() - 1
     *     where
     *     A == this
     * }</pre>
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
        DoubleMatrix1d y,
        DoubleMatrix1d z,
        double alpha,
        double beta,
        boolean transposeA
    ) {
        if (transposeA) {
            return transpose().mult(y, z, alpha, beta, false);
        }
        if (z == null) {
            final var struct = new Structure1d(new Extent1d(rows()));
            final var elems = array().like(struct.extent().elements());
            return mult(y, new DoubleMatrix1d(struct, elems), alpha, beta, false);
        }

        if (cols() != y.extent().elements() || rows() > z.extent().elements()) {
            throw new IllegalArgumentException(
                "Incompatible args: " + extent() + ", " + y.extent() + ", " + z.extent()
            );
        }

        for (int r = 0; r < rows(); ++r) {
            double s = 0;
            for (int c = 0; c < cols(); ++c) {
                s = Math.fma(get(r, c), y.get(c), s);
            }
            z.set(r, Math.fma(alpha, s, beta*z.get(r)));
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
    public DoubleMatrix1d mult(DoubleMatrix1d y, DoubleMatrix1d z) {
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
     * @throws IllegalArgumentException if {@code B.rows() != A.cols()} or
     *         {@code C.rows() != A.rows() || C.cols() != B.cols()} or
     *         {@code A == C || B == C}
     */
    public DoubleMatrix2d mult(
        DoubleMatrix2d B,
        DoubleMatrix2d C,
        double alpha,
        double beta,
        boolean transposeA,
        boolean transposeB
    ) {
        requireNonNull(B);

        if (transposeA) {
            return transpose().mult(B, C, alpha, beta, false, transposeB);
        }
        if (transposeB) {
            return mult(B.transpose(), C, alpha, beta, false, false);
        }
        if (C == null) {
            return mult(B, like(new Extent2d(rows(), B.cols())), alpha, beta, false, false);
        }

        final int m = rows();
        final int n = cols();
        final int p = B.cols();

        if (B.rows() != n) {
            throw new IllegalArgumentException(
                "Matrix inner dimensions must be equal:" +
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
            throw new IllegalArgumentException(
                "Matrices A, B or C must not be identical."
            );
        }

        // If dense matrix multiplication doesn't apply, do classic variant.
        if (isDense(this, B, C)) {
            denseMult(this, B, C, alpha, beta);
        } else {
            for (int j = p; --j >= 0;) {
                for (int i = m; --i >= 0;) {
                    double s = 0;
                    for (int k = n; --k >= 0;) {
                        s = Math.fma(get(i, k), B.get(k, j), s);
                    }
                    C.set(i, j, Math.fma(alpha, s, beta*C.get(i, j)));
                }
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
     * @throws IllegalArgumentException if {@code B.rows() != A.cols()} or
     *         {@code C.rows() != A.rows() || C.cols() != B.cols()} or
     *         {@code A == C || B == C}
     */
    public DoubleMatrix2d mult(DoubleMatrix2d B, DoubleMatrix2d C) {
        return mult(B, C, 1, (C == null ? 1 : 0), false, false);
    }

    /**
     * Return the sum of all cells: {@code Sum(x[i, j])}.
     *
     * @return the sum of all cells
     */
    public double sum() {
        return reduce(Double::sum, DoubleUnaryOperator.identity())
            .orElse(0);
    }

    /**
     * Checks whether the given matrices have the same dimension and contains
     * the same values.
     *
     * @param other the second matrix to compare
     * @return {@code true} if the two given matrices are equal, {@code false}
     *         otherwise
     */
    public boolean equals(DoubleMatrix2d other) {
        final var context = NumericalContext.get();

        return extent().equals(other.extent()) &&
            allMatch((r, c) -> context.equals(get(r, c), other.get(r, c)));
    }

    @Override
    public boolean equals(Object object) {
        return object == this ||
            object instanceof DoubleMatrix2d matrix &&
            equals(matrix);
    }

    /**
     * Return a 2-d matrix view of the given input {@code values}. It is assumed
     * that the values are given in row-major order. The following example shows
     * how to create a <em>dense</em> 3x4 matrix.
     * <pre>{@code
     * final var matrix = DoubleMatrix2d.of(
     *     new Extent2d(3, 4),
     *     1, 2,  3,  4,
     *     5, 6,  7,  8,
     *     9, 10, 11, 12
     * );
     * }</pre>
     *
     * @implSpec
     * The given input data is <b>not</b> copied, the returned object is a
     * <b>view</b> onto the given input data.
     *
     * @param extent the extent of the given values
     * @param values the returned matrix values
     * @return a matrix view of the given input data
     * @throws IllegalArgumentException if the desired extent of the matrix
     *         requires fewer elements than given
     */
    public static DoubleMatrix2d of(Extent2d extent, double... values) {
        return new DoubleMatrix2d(
            new Structure2d(extent),
            new DenseDoubleArray(values)
        );
    }

}
