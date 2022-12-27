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

import io.jenetics.lattices.array.DenseDoubleArray;
import io.jenetics.lattices.array.DoubleArray;
import io.jenetics.lattices.grid.DoubleGrid2d;
import io.jenetics.lattices.grid.DoubleGrid2dOps;
import io.jenetics.lattices.grid.Factory2d;
import io.jenetics.lattices.structure.Extent1d;
import io.jenetics.lattices.structure.Extent2d;
import io.jenetics.lattices.structure.Projection2d;
import io.jenetics.lattices.structure.StrideOrder2d;
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
public final class DoubleMatrix2d
    extends DoubleGrid2dOps
    implements Matrix2d<DoubleArray, DoubleMatrix2d>
{

    /**
     * Factory for creating <em>dense</em> 2-d double matrices.
     */
    public static final Factory2d<DoubleMatrix2d> DENSE = struct ->
        new DoubleMatrix2d(
            struct,
            DenseDoubleArray.ofSize(struct.extent().size())
        );

    /**
     * Create a new 2-d matrix with the given {@code structure} and element
     * {@code array}.
     *
     * @param structure the matrix structure
     * @param array the element array
     */
    public DoubleMatrix2d(final Structure2d structure, final DoubleArray array) {
        super(structure, array);
    }

    /**
     * Create a new matrix <em>view</em> from the given {@code grid}, no data is
     * actually copied.
     *
     * @param grid the data grid
     */
    public DoubleMatrix2d(final DoubleGrid2d grid) {
        this(grid.structure(), grid.array());
    }

    @Override
    public Factory2d<DoubleMatrix2d> factory() {
        return struct -> new DoubleMatrix2d(
            struct,
            array.like(struct.extent().size())
        );
    }

    @Override
    public DoubleMatrix2d create(final Structure2d structure2d, final DoubleArray array) {
        return new DoubleMatrix2d(structure2d, array);
    }

    @Override
    public void assign(final DoubleMatrix2d other) {
        super.assign(other);
    }

    /* *************************************************************************
     * Matrix view methods.
     * ************************************************************************/

    /**
     * Return a <em>transposed</em> view of this matrix.
     *
     * @return a <em>transposed</em> view of this matrix
     */
    public DoubleMatrix2d transpose() {
        return map(View2d.TRANSPOSE);
    }

    /**
     * Constructs and returns a <em>view</em> representing the rows of the given
     * column. The returned view is backed by this matrix, so changes in the
     * returned view are reflected in this matrix, and vice-versa.
     *
     * @param index the column index.
     * @return a new column view.
     * @throws IndexOutOfBoundsException if {@code index < 0 || index >= cols()}
     * @throws UnsupportedOperationException if the {@link #order()} function
     *         is not an instance of {@link StrideOrder2d}
     */
    public DoubleMatrix1d colAt(final int index) {
        return new DoubleMatrix1d(
            Projection2d.col(index).apply(structure),
            array
        );
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
     *         is not an instance of {@link StrideOrder2d}
     */
    public DoubleMatrix1d rowAt(final int index) {
        return new DoubleMatrix1d(
            Projection2d.row(index).apply(structure),
            array
        );
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
        final DoubleMatrix1d y,
        final DoubleMatrix1d z,
        final double alpha,
        final double beta,
        final boolean transposeA
    ) {
        if (transposeA) {
            return transpose().mult(y, z, alpha, beta, false);
        }
        if (z == null) {
            final var struct = new Structure1d(new Extent1d(rows()));
            final var elems = array.like(struct.extent().size());
            return mult(y, new DoubleMatrix1d(struct, elems), alpha, beta, false);
        }

        if (cols() != y.size() || rows() > z.size()) {
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
     * @throws IllegalArgumentException if {@code B.rows() != A.cols()} or
     *         {@code C.rows() != A.rows() || C.cols() != B.cols()} or
     *         {@code A == C || B == C}
     */
    public DoubleMatrix2d mult(
        final DoubleMatrix2d B,
        final DoubleMatrix2d C,
        final double alpha,
        final double beta,
        final boolean transposeA,
        final boolean transposeB
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
    public boolean equals(final Object object) {
        return object == this ||
            object instanceof DoubleMatrix2d matrix &&
            equals(matrix);
    }

}
