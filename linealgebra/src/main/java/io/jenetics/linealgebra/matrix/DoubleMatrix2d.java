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

import java.util.function.DoubleUnaryOperator;

import io.jenetics.linealgebra.array.DenseDoubleArray;
import io.jenetics.linealgebra.array.DoubleArray;
import io.jenetics.linealgebra.grid.DoubleGrid2d;
import io.jenetics.linealgebra.grid.Extent1d;
import io.jenetics.linealgebra.grid.Extent2d;
import io.jenetics.linealgebra.grid.Factory2d;
import io.jenetics.linealgebra.grid.Loop2d;
import io.jenetics.linealgebra.grid.Range2d;
import io.jenetics.linealgebra.grid.StrideOrder2d;
import io.jenetics.linealgebra.grid.Structure1d;
import io.jenetics.linealgebra.grid.Structure2d;

/**
 * Generic class for 2-d matrices holding {@code double} elements. Instances
 * of this class are usually created via a factory.
 * <pre>{@code
 * final DoubleMatrix2d matrix5x10 = DENSE_FACTORY.newInstance(5, 10);
 * }</pre>
 *
 * @see #DENSE_FACTORY
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
public class DoubleMatrix2d
    extends DoubleGrid2d
    implements Matrix2d<DoubleMatrix2d>
{

    /**
     * Factory for creating <em>dense</em> 2-d double matrices.
     */
    public static final Factory2d<DoubleMatrix2d> DENSE_FACTORY = struct ->
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
     * Create a new matrix <em>view</em> from the given {@code grid}.
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
    public DoubleMatrix2d view(final Structure2d structure) {
        return new DoubleMatrix2d(structure, array);
    }

    @Override
    public DoubleMatrix2d copy(final Range2d range) {
        // Fast copy, if applicable.
        if (range.row() == 0 &&
            range.col() == 0 &&
            range.height() == rows() &&
            range.width() == cols() &&
            structure.order().equals(new StrideOrder2d(new Extent2d(range))))
        {
            return new DoubleMatrix2d(structure, array.copy());
        } else {
            final var struct = structure.copy(range);
            final var elems = array.like(range.size());

            final var loop = new Loop2d.RowMajor(struct.extent());
            loop.forEach((r, c) ->
                elems.set(
                    struct.order().index(r, c),
                    get(r + range.row(), c + range.col())
                )
            );

            return new DoubleMatrix2d(struct, elems);
        }
    }

    @Override
    public DoubleMatrix2d transpose() {
        return new DoubleMatrix2d(structure.transpose(), array);
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
     *         is not an instance of {@link StrideOrder2d}
     */
    public DoubleMatrix1d colAt(final int index) {
        return new DoubleMatrix1d(structure.colAt(index), array);
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
        return new DoubleMatrix1d(structure.rowAt(index), array);
    }

    /* *************************************************************************
     * Matrix algebra methods.
     * ************************************************************************/

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
            final var struct = new Structure1d(new Extent1d(rows()));
            final var elems = array.like(struct.extent().size());
            z = new DoubleMatrix1d(struct, elems);
        }

        if (cols() != y.size() || rows() > z.size()) {
            throw new IllegalArgumentException(
                "Incompatible args: " + extent() + ", " + y.extent() + ", " + z.extent()
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
            C = like(m, p);
        }

        if (B.rows() != n) {
            throw new IllegalArgumentException(
                "2-d matrix inner dimensions must be equal:" +
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

        for (int j = p; --j >= 0;) {
            for (int i = m; --i >= 0;) {
                double s = 0;
                for (int k = n; --k >= 0;) {
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
        return obj instanceof DoubleMatrix2d m && equals(m, 0.0);
    }

}
