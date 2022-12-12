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

import io.jenetics.linealgebra.grid.Extent2d;
import io.jenetics.linealgebra.grid.Range1d;
import io.jenetics.linealgebra.matrix.DoubleMatrix1d;
import io.jenetics.linealgebra.matrix.DoubleMatrix2d;

/**
 * Performs in place QR-decomposition.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
public final class QR {

    private final DoubleMatrix2d qr;
    private final DoubleMatrix1d rdiag;

    private QR(final DoubleMatrix2d qr, final DoubleMatrix1d rdiag) {
        this.qr = requireNonNull(qr);
        this.rdiag = requireNonNull(rdiag);
    }

    public DoubleMatrix2d solve(final DoubleMatrix2d B) {
        if (B.rows() != qr.rows()) {
            throw new IllegalArgumentException(
                "Matrix row dimensions must agree: %s != %s."
                    .formatted(B.extent(), qr.extent())
            );
        }
        if (!hasFullRank()) {
            throw new IllegalArgumentException("Matrix is rank deficient.");
        }

        // Copy right hand side
        final DoubleMatrix2d X = B.copy();

        // Compute Y = transpose(Q)*B
        for (int k = 0; k < qr.cols(); k++) {
            for (int j = 0; j < B.cols(); j++) {
                double s = 0.0;
                for (int i = k; i < qr.rows(); i++) {
                    //s += qr.get(i, k)*X.get(i, j);
                    s = Math.fma(qr.get(i, k), X.get(i, j), s);
                }
                s = -s/qr.get(k, k);
                for (int i = k; i < qr.rows(); i++) {
                    //X.set(i, j, X.get(i, j) + s*qr.get(i, k));
                    X.set(
                        i, j,
                        Math.fma(s, qr.get(i, k), X.get(i, j))
                    );
                }
            }
        }

        // Solve R*X = Y;
        for (int k = qr.cols() - 1; k >= 0; k--) {
            for (int j = 0; j < B.cols(); j++) {
                X.set(k, j, X.get(k, j)/rdiag.get(k));
            }
            for (int i = 0; i < k; i++) {
                for (int j = 0; j < B.cols(); j++) {
                    //X.set(i, j, X.get(i, j) - X.get(k, j)*qr.get(i, k));
                    X.set(
                        i, j,
                        -Math.fma(X.get(k, j), qr.get(i, k), -X.get(i, j))
                    );
                }
            }
        }
        return X.view(new Extent2d(qr.cols(), B.cols()));
    }

    /**
     * Returns whether the matrix <tt>A</tt> has full rank.
     *
     * @return true if <tt>R</tt>, and hence <tt>A</tt>, has full rank.
     */
    public boolean hasFullRank() {
        for (int j = 0; j < qr.cols(); j++) {
            if (rdiag.get(j) == 0) {
                return false;
            }
        }
        return true;
    }

    public static QR decompose(final DoubleMatrix2d matrix) {
        matrix.requireRectangular();

        final var m = matrix.rows();
        final var n = matrix.cols();
        final var Rdiag = matrix.colAt(0).like();

        final var QRcolumnsPart = new DoubleMatrix1d[n];
        for (int k = 0; k < n; k++) {
            QRcolumnsPart[k] = matrix.colAt(k).view(new Range1d(k, m - k));
        }

        // Main loop.
        for (int k = 0; k < n; k++) {
            double nrm = 0;
            for (int i = k; i < m; i++) {
                nrm = Math.hypot(nrm, matrix.get(i, k));
            }

            if (nrm != 0.0) {
                // Form k-th Householder vector.
                if (matrix.get(k, k) < 0) {
                    nrm = -nrm;
                }
                final var divisor = 1.0/nrm;
                QRcolumnsPart[k].assign(x -> x*divisor);

                matrix.set(k, k, matrix.get(k, k) + 1);

                // Apply transformation to remaining columns.
                for (int j = k + 1; j < n; j++) {
                    final DoubleMatrix1d QRcolj = matrix.colAt(j)
                        .view(new Range1d(k, m - k));

                    double s = QRcolumnsPart[k].dotProduct(QRcolj);
                    s = -s/matrix.get(k, k);
                    for (int i = k; i < m; i++) {
                        //matrix.set(i, j, matrix.get(i, j) + s*matrix.get(i, k));
                        matrix.set(
                            i, j,
                            Math.fma(s, matrix.get(i, k), matrix.get(i, j))
                        );
                    }
                }
            }
            Rdiag.set(k, -nrm);
        }

        return new QR(matrix, Rdiag);
    }

}
