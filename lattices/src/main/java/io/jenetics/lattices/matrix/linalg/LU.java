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
package io.jenetics.lattices.matrix.linalg;

import static java.util.Objects.requireNonNull;
import static io.jenetics.lattices.grid.Structures.checkRectangular;
import static io.jenetics.lattices.grid.Structures.checkSquare;
import static io.jenetics.lattices.matrix.Matrices.isSingular;

import io.jenetics.lattices.matrix.DoubleMatrix1d;
import io.jenetics.lattices.matrix.DoubleMatrix2d;
import io.jenetics.lattices.matrix.NumericalContext;
import io.jenetics.lattices.structure.Extent1d;
import io.jenetics.lattices.structure.Index1d;
import io.jenetics.lattices.structure.Index2d;
import io.jenetics.lattices.structure.Range1d;
import io.jenetics.lattices.structure.Range2d;
import io.jenetics.lattices.structure.View1d;
import io.jenetics.lattices.structure.View2d;

/**
 * Store the result of an <em>LU</em>-decomposition.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public final class LU implements Solver {

    private final DoubleMatrix2d LU;
    private final int[] pivot;
    private final int pivsign;
    private final boolean singular;

    private final NumericalContext context;

    private LU(
        DoubleMatrix2d LU,
        int[] pivot,
        int pivsign,
        NumericalContext context
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
        checkSquare(LU.extent());

        if (singular) {
            return 0;
        }

        double det = pivsign;
        for (int j = 0; j < LU.cols(); ++j) {
            det *= LU.get(j, j);
        }
        return det;
    }

    public boolean isNonSingular() {
        return !singular;
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
    @Override
    public DoubleMatrix2d solve(DoubleMatrix2d B) {
        checkRectangular(LU.extent());

        final var X = B.copy();
        final int m = LU.rows();
        final int n = LU.cols();

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

        // Right-hand side with pivoting
        Permutations.permuteRows(X, pivot);

        final DoubleMatrix1d[] B_rows = new DoubleMatrix1d[n];
        for (int k = 0; k < n; ++k) {
            B_rows[k] = X.rowAt(k);
        }

        final var B_row_k = X.rowAt(0).like();

        // Solve L*Y = B(piv,:)
        for (int k = 0; k < n; ++k) {
            B_row_k.assign(B_rows[k]);

            for (int i = k + 1; i < n; ++i) {
                final double multiplier = -LU.get(i, k);
                if (context.isNotZero(multiplier)) {
                    B_rows[i].assign(B_row_k, (a, b) -> Math.fma(multiplier, b, a));
                }
            }
        }

        // Solve U*B = Y;
        for (int k = n - 1; k >= 0; k--) {
            final double multiplier1 = 1.0/LU.get(k, k);
            B_rows[k].assign(a -> a*multiplier1);
            B_row_k.assign(B_rows[k]);

            for (int i = 0; i < k; ++i) {
                final double multiplier2 = -LU.get(i, k);
                if (context.isNotZero(multiplier2)) {
                    B_rows[i].assign(B_row_k, (a, b) -> Math.fma(multiplier2, b, a));
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
    public static LU decompose(DoubleMatrix2d A) {
        final var context = NumericalContext.get();
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

        final var col_j = lu.colAt(0).like();
        for (int j = 0; j < n; ++j) {
            col_j.assign(lu.colAt(j));

            // Apply previous transformations.
            for (int i = 0; i < m; ++i) {
                int kmax = Math.min(i, j);
                double s = rows[i].dotProduct(col_j, 0, kmax);
                double before = col_j.get(i);
                double after = before - s;

                col_j.set(i, after);
                lu.set(i, j, after);
            }

            // Find pivot and exchange if necessary.
            int p = j;
            if (p < m) {
                double max = Math.abs(col_j.get(p));
                for (int i = j + 1; i < m; ++i) {
                    double v = Math.abs(col_j.get(i));
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
                    .view(View1d.of(new Range1d(
                        new Index1d(j + 1),
                        new Extent1d(m - (j + 1)))))
                    .assign(v -> v*multiplier);
            }
        }

        return new LU(lu, piv, pivsign, context);
    }

    private static void lowerTriangular(DoubleMatrix2d A) {
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
            final var range = new Range2d(
                new Index2d(0, min),
                new Index2d(A.rows(), A.cols() - min)
            );
            A.view(View2d.of(range)).assign(0);
        }
    }

    private static void upperTriangular(DoubleMatrix2d A) {
        final int min = Math.min(A.rows(), A.cols());

        for (int r = min; --r >= 0;) {
            for (int c = min; --c >= 0;) {
                if (r > c) {
                    A.set(r, c, 0);
                }
            }
        }
        if (A.cols() < A.rows()) {
            final var range = new Range2d(
                new Index2d(0, min),
                new Index2d(A.rows() - min, A.cols())
            );
            A.view(View2d.of(range)).assign(0);
        }
    }

}
