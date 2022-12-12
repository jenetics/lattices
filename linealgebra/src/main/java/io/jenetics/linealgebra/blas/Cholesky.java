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


import io.jenetics.linealgebra.matrix.DoubleMatrix1d;
import io.jenetics.linealgebra.matrix.DoubleMatrix2d;

/**
 * Performs in place Cholesky-decomposition.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
public class Cholesky {

    private final DoubleMatrix2d L;
    private final boolean isSymmetricPositiveDefinite;

    public Cholesky(
        final DoubleMatrix2d L,
        final boolean isSymmetricPositiveDefinite
    ) {
        this.L = requireNonNull(L);
        this.isSymmetricPositiveDefinite = isSymmetricPositiveDefinite;
    }

    public DoubleMatrix2d L() {
        return L;
    }

    /**
     * Solves <tt>A*X = B</tt>; returns <tt>X</tt>.
     *
     * @param B A Matrix with as many rows as <tt>A</tt> and any number of columns.
     * @return <tt>X</tt> so that <tt>L*L'*X = B</tt>.
     * @throws IllegalArgumentException if <tt>B.rows() != A.rows()</tt>.
     * @throws IllegalArgumentException if <tt>!isSymmetricPositiveDefinite()</tt>.
     */
    public DoubleMatrix2d solve(DoubleMatrix2d B) {
        // Copy right hand side.
        DoubleMatrix2d X = B.copy();
        int nx = B.cols();

        // fix by MG Ferreira <mgf@webmail.co.za>
        // old code is in method xxxSolveBuggy()
        for (int c = 0; c < nx; c++) {
            // Solve L*Y = B;
            for (int i = 0; i < L.rows(); i++) {
                double sum = B.get(i, c);
                for (int k = i - 1; k >= 0; k--) {
                    sum -= L.get(i, k)*X.get(k, c);
                }
                X.set(i, c, sum / L.get(i, i));
            }

            // Solve L'*X = Y;
            for (int i = L.rows() - 1; i >= 0; i--) {
                double sum = X.get(i, c);
                for (int k = i + 1; k < L.rows(); k++) {
                    sum -= L.get(k, i) * X.get(k, c);
                }
                X.set(i, c, sum/L.get(i, i));
            }
        }

        return X;
    }

    public static Cholesky decompose(final DoubleMatrix2d A) {
        A.requireRectangular();
        // Initialize.
        //double[][] A = Arg.getArray();

        final var n = A.rows();
        //L = new double[n][n];
        final var L = A.like(n, n);
        var isSymmetricPositiveDefinite = A.cols() == n;

        //precompute and cache some views to avoid regenerating them time and again
        final var Lrows = new DoubleMatrix1d[n];
        for (int j = 0; j < A.rows(); j++) {
            Lrows[j] = L.rowAt(j);
        }

        // Main loop.
        for (int j = 0; j < n; j++) {
            //double[] Lrowj = L[j];
            //DoubleMatrix1D Lrowj = L.viewRow(j);
            double d = 0.0;
            for (int k = 0; k < j; k++) {
                //double[] Lrowk = L[k];
                double s = Lrows[k].dotProduct(Lrows[j], 0, k);
			/*
			DoubleMatrix1D Lrowk = L.viewRow(k);
			double s = 0.0;
			for (int i = 0; i < k; i++) {
			   s += Lrowk.getQuick(i)*Lrowj.getQuick(i);
			}
			*/
                s = (A.get(j, k) - s)/L.get(k, k);
                Lrows[j].set(k, s);
                d = d + s * s;
                isSymmetricPositiveDefinite = isSymmetricPositiveDefinite &&
                    (A.get(k, j) == A.get(j, k));
            }
            d = A.get(j, j) - d;
            isSymmetricPositiveDefinite = isSymmetricPositiveDefinite && (d > 0.0);
            L.set(j, j, Math.sqrt(Math.max(d, 0.0)));

            for (int k = j + 1; k < n; k++) {
                L.set(j, k, 0.0);
            }
        }

        return new Cholesky(L, isSymmetricPositiveDefinite);
    }

}
