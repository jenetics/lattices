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

import static java.util.Objects.requireNonNull;
import static io.jenetics.linealgebra.blas.Algebra.isNonSingular;
import static io.jenetics.linealgebra.blas.Permutations.permuteRows;

import io.jenetics.linealgebra.grid.Range1d;
import io.jenetics.linealgebra.matrix.DoubleMatrix1d;
import io.jenetics.linealgebra.matrix.DoubleMatrix2d;

/**
 * Performs in place LU-decomposition.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
final class LU {

    private final DoubleMatrix2d lu;
    private final int pivsign;
    private final int[] pivot;
    private final boolean singular;

    private LU(
        final DoubleMatrix2d lu,
        final int pivsign,
        final int[] pivot
    ) {
        this.lu = requireNonNull(lu);
        this.pivsign = pivsign;
        this.pivot = requireNonNull(pivot);

        singular = !isNonSingular(lu);
    }

    public void solve(final DoubleMatrix2d B) {
        lu.requireRectangular();

        int m = lu.rows();
        int n = lu.cols();

        if (m*n == 0) {
            return;
        }

        if (B.rows() != m) {
            throw new IllegalArgumentException(
                "Matrix row dimensions must agree: %s != %s."
                    .formatted(B.rows(), m)
            );
        }
        if (singular) {
            throw new IllegalArgumentException("LU-matrix is singular.");
        }

        // Right hand side with pivoting
        permuteRows(B, this.pivot);

        // Precompute and cache some views to avoid regenerating them time
        // and again.
        final DoubleMatrix1d[] Brows = new DoubleMatrix1d[n];
        for (int k = 0; k < n; ++k) {
            Brows[k] = B.rowAt(k);
        }

        final var Browk = DoubleMatrix1d.DENSE_FACTORY.newInstance(B.cols());

        // Solve L*Y = B(piv,:)
        for (int k = 0; k < n; k++) {
            Browk.assign(Brows[k]);

            for (int i = k + 1; i < n; i++) {
                final double multiplier = -lu.get(i, k);
                if (Double.compare(multiplier, 0.0) != 0) {
                    Brows[i].assign(Browk, (a, b) -> a + multiplier*b);
                }
            }
        }

        // Solve U*B = Y;
        for (int k = n - 1; k >= 0; k--) {
            final double multiplier1 = 1.0/lu.get(k, k);
            Brows[k].assign(a -> a*multiplier1);
            Browk.assign(Brows[k]);

            for (int i = 0; i < k; i++) {
                final double multiplier2 = -lu.get(i, k);
                if (Double.compare(multiplier2, 0.0) != 0) {
                    Brows[i].assign(Browk, (a, b) -> a  + multiplier2*b);
                }
            }
        }
    }

    /**
     * Performs an in-place LU-decomposition of the given {@code matrix}.
     *
     * @param matrix the matrix to be decomposed
     */
    static LU decompose(final DoubleMatrix2d matrix) {
        requireNonNull(matrix);

        final int m = matrix.rows();
        final int n = matrix.cols();

        int pivsign = 1;
        final int[] piv = new int[m];
        for (int i = 0; i < m; ++i) {
            piv[i] = i;
        }

        if (m*n == 0) {
            return new LU(matrix, pivsign, piv);
        }

        final var rows = new DoubleMatrix1d[m];
        for (int i = 0; i < m; ++i) {
            rows[i] = matrix.rowAt(i);
        }

        final DoubleMatrix1d colj = matrix.colAt(0).like();
        for (int j = 0; j < n; ++j) {
            colj.assign(matrix.colAt(j));

            // Apply previous transformations.
            for (int i = 0; i < m; ++i) {
                int kmax = Math.min(i, j);
                double s = rows[i].dotProduct(colj, 0, kmax);
                double before = colj.get(i);
                double after = before - s;

                colj.set(i, after);
                matrix.set(i, j, after);
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

            final double jj = matrix.get(j, j);
            if (j < m && Double.compare(jj, 0.0) != 0) {
                final var multiplier = 1.0/jj;
                matrix
                    .colAt(j)
                    .view(new Range1d(j + 1, m - (j + 1)))
                    .assign(v -> v*multiplier);
            }
        }

        return new LU(matrix, pivsign, piv);
    }

}
