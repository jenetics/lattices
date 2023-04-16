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
import static io.jenetics.lattices.structure.Structures.checkRectangular;

import io.jenetics.lattices.matrix.DoubleMatrix1d;
import io.jenetics.lattices.matrix.DoubleMatrix2d;
import io.jenetics.lattices.matrix.NumericalContext;
import io.jenetics.lattices.structure.Extent2d;

/**
 * Store the result of a <em>Cholesky</em>-decomposition.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public final class Cholesky implements Solver {

    private final DoubleMatrix2d L;
    private final boolean symmetricPositiveDefinite;

    private final NumericalContext context;

    private Cholesky(
        DoubleMatrix2d L,
        boolean symmetricPositiveDefinite,
        NumericalContext context
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
        return L.copy();
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
     * Solves {@code A*X = B} and returns {@code X}.
     *
     * @param B a matrix with as many rows as {@code A} and any number of columns
     * @return {@code X} so that {@code L*L'*X = B}
     * @throws IllegalArgumentException if {@code B.rows() != A.rows()} or
     *         {@code !isSymmetricPositiveDefinite()}
     */
    @Override
    public DoubleMatrix2d solve(DoubleMatrix2d B) {
        final var X = B.copy();

        for (int c = 0; c < B.cols(); ++c) {
            // Solve L*Y = B;
            for (int i = 0; i < L.rows(); ++i) {
                double sum = B.get(i, c);
                for (int k = i - 1; k >= 0; --k) {
                    sum = -Math.fma(L.get(i, k), X.get(k, c), -sum);
                }
                X.set(i, c, sum/L.get(i, i));
            }

            // Solve L'*X = Y;
            for (int i = L.rows() - 1; i >= 0; --i) {
                double sum = X.get(i, c);
                for (int k = i + 1; k < L.rows(); ++k) {
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
    public static Cholesky decompose(DoubleMatrix2d A) {
        checkRectangular(A.extent());

        final var context = NumericalContext.get();

        final var n = A.rows();
        final var L = A.like(new Extent2d(n, n));
        var isSymmetricPositiveDefinite = A.cols() == n;

        final var L_rows = new DoubleMatrix1d[n];
        for (int j = 0; j < A.rows(); j++) {
            L_rows[j] = L.rowAt(j);
        }

        // Main loop.
        for (int j = 0; j < n; ++j) {
            double d = 0.0;
            for (int k = 0; k < j; ++k) {
                double s = L_rows[k].dotProduct(L_rows[j], 0, k);

                s = (A.get(j, k) - s)/L.get(k, k);
                L_rows[j].set(k, s);
                d = Math.fma(s, s, d);
                isSymmetricPositiveDefinite =
                    isSymmetricPositiveDefinite &&
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
