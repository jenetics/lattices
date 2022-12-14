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
package io.jenetics.linealgebra.blas;

import io.jenetics.linealgebra.NumericalContext;
import io.jenetics.linealgebra.grid.Grids;
import io.jenetics.linealgebra.grid.Range1d;
import io.jenetics.linealgebra.grid.Range2d;
import io.jenetics.linealgebra.matrix.DoubleMatrix1d;
import io.jenetics.linealgebra.matrix.DoubleMatrix2d;

import static io.jenetics.linealgebra.blas.Permutations.permuteRows;
import static io.jenetics.linealgebra.matrix.Matrices.isSingular;
import static java.util.Objects.requireNonNull;

/**
 * Store the result of an <em>LU</em>-decomposition.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
public final class LU {

    private final DoubleMatrix2d LU;
    private final int[] pivot;
    private final int pivsign;
    private final boolean singular;

    private final NumericalContext context;

    private LU(
        final DoubleMatrix2d LU,
        final int[] pivot,
        final int pivsign,
        final NumericalContext context
    ) {
        this.LU = requireNonNull(LU);
        this.pivot = requireNonNull(pivot);
        this.pivsign = pivsign;
        singular = isSingular(LU);
        this.context = requireNonNull(context);
    }

    /**
     * Returns a copy of the combined lower and upper triangular factor,
     * <em>LU</em>.
     *
     * @return a copy of the combined lower and upper triangular factor
     */
    public DoubleMatrix2d LU() {
        return LU.copy();
    }

    /**
     * Return the lower triangular factor {@code L}.
     *
     * @return the lower triangular factor
     */
    public DoubleMatrix2d L() {
        final var l = LU.copy();
        lowerTriangular(l);
        return l;
    }

    /**
     * Return the upper triangular factor {@code U}.
     *
     * @return the upper triangular factor
     */
    public DoubleMatrix2d U() {
        final var u = LU.copy();
        upperTriangular(u);
        return u;
    }

    /**
     * Returns a copy of the pivot permutation vector.
     *
     * @return a copy of the pivot permutation vector
     */
    public int[] pivot() {
        return pivot.clone();
    }

    /**
     * Returns the determinant {@code det(A)}.
     *
     * @throws IllegalArgumentException if the matrix is not square
     */
    public double det() {
        Grids.checkSquare(LU);

        if (singular) {
            return 0;
        }

        double det = pivsign;
        for (int j = 0; j < LU.cols(); j++) {
            det *= LU.get(j, j);
        }
        return det;
    }

    /**
     * Solves {@code A*X = B}.
     *
     * @param B a matrix with as many rows as {@code A} and any number of
     *        columns
     * @return {@code X} so that {@code L*U*X = B(piv,:)}
     * @throws IllegalArgumentException if {@code B.rows() != A.rows()} or
     *         {@code isSingular(lU)} or {@code A.rows() < A.cols()}
     */
    public DoubleMatrix2d solve(final DoubleMatrix2d B) {
        LU.requireRectangular();

        final var X = B.copy();
        int m = LU.rows();
        int n = LU.cols();

        if (m == 0 || n == 0) {
            return X;
        }

        if (X.rows() != m) {
            throw new IllegalArgumentException(
                "Matrix row dimensions must agree: %s != %s."
                    .formatted(X.rows(), m)
            );
        }
        if (singular) {
            throw new IllegalArgumentException("LU-matrix is singular.");
        }

        // Right hand side with pivoting
        permuteRows(X, pivot);

        // Precompute and cache some views to avoid regenerating them time
        // and again.
        final DoubleMatrix1d[] Brows = new DoubleMatrix1d[n];
        for (int k = 0; k < n; ++k) {
            Brows[k] = X.rowAt(k);
        }

        final var Browk = X.colAt(0).like();

        // Solve L*Y = B(piv,:)
        for (int k = 0; k < n; ++k) {
            Browk.assign(Brows[k]);

            for (int i = k + 1; i < n; ++i) {
                final double multiplier = -LU.get(i, k);
                if (context.isNotZero(multiplier)) {
                    Brows[i].assign(Browk, (a, b) -> Math.fma(multiplier, b, a));
                }
            }
        }

        // Solve U*B = Y;
        for (int k = n - 1; k >= 0; k--) {
            final double multiplier1 = 1.0/ LU.get(k, k);
            Brows[k].assign(a -> a*multiplier1);
            Browk.assign(Brows[k]);

            for (int i = 0; i < k; ++i) {
                final double multiplier2 = -LU.get(i, k);
                if (context.isNotZero(multiplier2)) {
                    Brows[i].assign(Browk, (a, b) -> Math.fma(multiplier2, b, a));
                }
            }
        }

        return X;
    }

    /**
     * Performs an <em>LU</em>-decomposition of the given matrix {@code A}.
     *
     * @param A the matrix to be decomposed
     * @return the <em>LU</em>-decomposition of the given matrix {@code A}
     */
    public static LU decompose(final DoubleMatrix2d A) {
        final var context = NumericalContext.instance();
        final var lu = A.copy();

        final int m = lu.rows();
        final int n = lu.cols();

        int pivsign = 1;
        final int[] piv = new int[m];
        for (int i = 0; i < m; ++i) {
            piv[i] = i;
        }

        if (m == 0 || n == 0) {
            return new LU(lu, piv, pivsign, context);
        }

        final var rows = new DoubleMatrix1d[m];
        for (int i = 0; i < m; ++i) {
            rows[i] = lu.rowAt(i);
        }

        final var colj = lu.colAt(0).like();
        for (int j = 0; j < n; ++j) {
            colj.assign(lu.colAt(j));

            // Apply previous transformations.
            for (int i = 0; i < m; ++i) {
                int kmax = Math.min(i, j);
                double s = rows[i].dotProduct(colj, 0, kmax);
                double before = colj.get(i);
                double after = before - s;

                colj.set(i, after);
                lu.set(i, j, after);
            }

            // Find pivot and exchange if necessary.
            int p = j;
            if (p < m) {
                double max = Math.abs(colj.get(p));
                for (int i = j + 1; i < m; ++i) {
                    double v = Math.abs(colj.get(i));
                    if (v > max) {
                        p = i;
                        max = v;
                    }
                }
            }

            if (p != j) {
                rows[p].swap(rows[j]);

                int k = piv[p];
                piv[p] = piv[j];
                piv[j] = k;
                pivsign = -pivsign;
            }

            final double jj = lu.get(j, j);
            if (j < m && context.isNotZero(jj)) {
                final var multiplier = 1.0/jj;
                lu.colAt(j)
                    .view(new Range1d(j + 1, m - (j + 1)))
                    .assign(v -> v*multiplier);
            }
        }

        return new LU(lu, piv, pivsign, context);
    }

    private static void lowerTriangular(final DoubleMatrix2d A) {
        final int min = Math.min(A.rows(), A.cols());

        for (int r = min; --r >= 0;) {
            for (int c = min; --c >= 0;) {
                if (r < c) {
                    A.set(r, c, 0);
                } else if (r == c) {
                    A.set(r, c, 1);
                }
            }
        }
        if (A.cols() > A.rows()) {
            A.view(new Range2d(0, min, A.rows(), A.cols() - min)).assign(0);
        }
    }

    private static void upperTriangular(final DoubleMatrix2d A) {
        final int min = Math.min(A.rows(), A.cols());

        for (int r = min; --r >= 0;) {
            for (int c = min; --c >= 0;) {
                if (r > c) {
                    A.set(r, c, 0);
                }
            }
        }
        if (A.cols() < A.rows()) {
            A.view(new Range2d(min, 0, A.rows() - min, A.cols())).assign(0);
        }
    }

}
