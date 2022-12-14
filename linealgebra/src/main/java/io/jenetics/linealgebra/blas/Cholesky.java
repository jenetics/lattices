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
import static io.jenetics.linealgebra.grid.Grids.checkRectangular;

import io.jenetics.linealgebra.NumericalContext;
import io.jenetics.linealgebra.matrix.DoubleMatrix1d;
import io.jenetics.linealgebra.matrix.DoubleMatrix2d;

/**
 * Store the result of a <em>Cholesky</em>-decomposition.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
public class Cholesky {

    private final DoubleMatrix2d L;
    private final boolean symmetricPositiveDefinite;

    private final NumericalContext context;

    private Cholesky(
        final DoubleMatrix2d L,
        final boolean symmetricPositiveDefinite,
        final NumericalContext context
    ) {
        this.L = requireNonNull(L);
        this.symmetricPositiveDefinite = symmetricPositiveDefinite;

        this.context = requireNonNull(context);
    }

    /**
     * Returns the triangular factor {@code L}.
     *
     * @return {@code L}
     */
    public DoubleMatrix2d L() {
        return L;
    }

    /**
     * Return {@code true} if the matrix {@code A} is symmetric and positive
     * definite.
     *
     * @return {@code true} if {@code A}is symmetric and positive definite
     *         {@code false} otherwise
     */
    public boolean isSymmetricPositiveDefinite() {
        return symmetricPositiveDefinite;
    }

    /**
     * Solves <tt>A*X = B</tt>; returns <tt>X</tt>.
     *
     * @param B A Matrix with as many rows as <tt>A</tt> and any number of columns.
     * @return <tt>X</tt> so that <tt>L*L'*X = B</tt>.
     * @throws IllegalArgumentException if <tt>B.rows() != A.rows()</tt>.
     * @throws IllegalArgumentException if <tt>!isSymmetricPositiveDefinite()</tt>.
     */
    public DoubleMatrix2d solve(final DoubleMatrix2d B) {
        final var X = B.copy();

        for (int c = 0; c < B.cols(); ++c) {
            // Solve L*Y = B;
            for (int i = 0; i < L.rows(); i++) {
                double sum = B.get(i, c);
                for (int k = i - 1; k >= 0; k--) {
                    sum = -Math.fma(L.get(i, k), X.get(k, c), -sum);
                }
                X.set(i, c, sum/L.get(i, i));
            }

            // Solve L'*X = Y;
            for (int i = L.rows() - 1; i >= 0; i--) {
                double sum = X.get(i, c);
                for (int k = i + 1; k < L.rows(); k++) {
                    sum = -Math.fma(L.get(k, i), X.get(k, c), -sum);
                }
                X.set(i, c, sum/L.get(i, i));
            }
        }

        return X;
    }

    /**
     * Performs a <em>Cholesky</em>-decomposition of the symmetric and positive
     * definite matrix {@code A}.
     *
     * @param A the matrix to be decomposed
     * @return Structure to access {@code L} and
     *         {@code isSymmetricPositiveDefinite} flag
     * @throws IllegalArgumentException if {@code A.rows() < A.cols()}
     */
    public static Cholesky decompose(final DoubleMatrix2d A) {
        checkRectangular(A);

        final var context = NumericalContext.get();

        final var n = A.rows();
        final var L = A.like(n, n);
        var isSymmetricPositiveDefinite = A.cols() == n;

        final var Lrows = new DoubleMatrix1d[n];
        for (int j = 0; j < A.rows(); j++) {
            Lrows[j] = L.rowAt(j);
        }

        // Main loop.
        for (int j = 0; j < n; j++) {
            double d = 0.0;
            for (int k = 0; k < j; k++) {
                double s = Lrows[k].dotProduct(Lrows[j], 0, k);

                s = (A.get(j, k) - s)/L.get(k, k);
                Lrows[j].set(k, s);
                d = Math.fma(s, s, d);
                isSymmetricPositiveDefinite = isSymmetricPositiveDefinite &&
                    context.equals(A.get(k, j), A.get(j, k));
            }
            d = A.get(j, j) - d;
            isSymmetricPositiveDefinite = isSymmetricPositiveDefinite &&
                context.isGreaterZero(d);
            L.set(j, j, Math.sqrt(Math.max(d, 0.0)));

            for (int k = j + 1; k < n; k++) {
                L.set(j, k, 0.0);
            }
        }

        return new Cholesky(L, isSymmetricPositiveDefinite, context);
    }

}
