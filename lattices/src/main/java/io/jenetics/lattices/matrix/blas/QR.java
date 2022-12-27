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
package io.jenetics.lattices.matrix.blas;

import static java.util.Objects.requireNonNull;
import static io.jenetics.lattices.grid.Grids.checkRectangular;

import io.jenetics.lattices.NumericalContext;
import io.jenetics.lattices.matrix.DoubleMatrix1d;
import io.jenetics.lattices.matrix.DoubleMatrix2d;
import io.jenetics.lattices.structure.Extent1d;
import io.jenetics.lattices.structure.Extent2d;
import io.jenetics.lattices.structure.Index1d;
import io.jenetics.lattices.structure.Range1d;
import io.jenetics.lattices.structure.View2d;

/**
 * Store the result of a <em>QR</em>-decomposition.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public final class QR {

    private final DoubleMatrix2d QR;
    private final DoubleMatrix1d rdiag;

    private final NumericalContext context;

    private QR(
        final DoubleMatrix2d QR,
        final DoubleMatrix1d rdiag,
        final NumericalContext context
    ) {
        this.QR = requireNonNull(QR);
        this.rdiag = requireNonNull(rdiag);

        this.context = requireNonNull(context);
    }

    /**
     * Return a copy of the {@code QR} matrix.
     *
     * @return a copy of the {@code QR} matrix
     */
    public DoubleMatrix2d QR() {
        return QR.copy();
    }

    /**
     * Returns the <em>Householder</em> vectors {@code H}.
     *
     * @return the lower trapezoidal matrix whose columns define the householder
     *         reflections
     */
    public DoubleMatrix2d H() {
        final var A = QR.copy();
        A.forEach((r, c) -> {
            if (r < c) {
                A.set(r, c, 0);
            }
        });

        return A;
    }

    /**
     * Generates and returns the (economy-sized) orthogonal factor {@code Q}.
     *
     * @return {@code Q}
     */
    public DoubleMatrix2d Q() {
        final DoubleMatrix2d Q = QR.like();
        for (int k = QR.cols() - 1; k >= 0; k--) {
            final DoubleMatrix1d QRcolk = QR.colAt(k)
                .view(new Range1d(new Index1d(k), new Extent1d(QR.rows() - k)));

            Q.set(k, k, 1);
            for (int j = k; j < QR.cols(); ++j) {
                if (context.isNotZero(QR.get(k, k))) {
                    final var Qcolj = Q.colAt(j)
                        .view(new Range1d(new Index1d(k), new Extent1d(QR.rows() - k)));

                    double s = -QRcolk.dotProduct(Qcolj)/QR.get(k, k);
                    Qcolj.assign(QRcolk, (a, b) -> Math.fma(b, s, a));
                }
            }
        }
        return Q;
    }

    /**
     * Returns the upper triangular factor {@code R}.
     *
     * @return {@code R}
     */
    public DoubleMatrix2d R() {
        final var R = QR.like(new Extent2d(QR.cols(), QR.cols()));
        for (int i = 0; i < QR.cols(); ++i) {
            for (int j = 0; j < QR.cols(); ++j) {
                if (i < j) {
                    R.set(i, j, QR.get(i, j));
                } else if (i == j) {
                    R.set(i, j, rdiag.get(i));
                } else {
                    R.set(i, j, 0);
                }
            }
        }

        return R;
    }

    /**
     * Solves {@code A*X = B}.
     *
     * @param B a matrix with as many rows as {@code A} and any number of
     *        columns
     * @return {@code X} that minimizes the two norm of {@code Q*R*X - B}
     * @throws IllegalArgumentException if {@code B.rows() != A.rows()} or
     *         {@code !hasFullRank()} ({@code A} is rank deficient)
     */
    public DoubleMatrix2d solve(final DoubleMatrix2d B) {
        if (B.rows() != QR.rows()) {
            throw new IllegalArgumentException(
                "Matrix row dimensions must agree: %s != %s."
                    .formatted(B.extent(), QR.extent())
            );
        }
        if (!hasFullRank()) {
            throw new IllegalArgumentException("Matrix is rank deficient.");
        }

        // Copy right hand side
        final var X = B.copy();

        // Compute Y = transpose(Q)*B
        for (int k = 0; k < QR.cols(); ++k) {
            for (int j = 0; j < B.cols(); ++j) {
                double s = 0.0;
                for (int i = k; i < QR.rows(); ++i) {
                    s = Math.fma(QR.get(i, k), X.get(i, j), s);
                }
                s = -s/QR.get(k, k);
                for (int i = k; i < QR.rows(); ++i) {
                    X.set(
                        i, j,
                        Math.fma(s, QR.get(i, k), X.get(i, j))
                    );
                }
            }
        }

        // Solve R*X = Y;
        for (int k = QR.cols() - 1; k >= 0; k--) {
            for (int j = 0; j < B.cols(); ++j) {
                X.set(k, j, X.get(k, j)/rdiag.get(k));
            }
            for (int i = 0; i < k; i++) {
                for (int j = 0; j < B.cols(); ++j) {
                    X.set(
                        i, j,
                        -Math.fma(X.get(k, j), QR.get(i, k), -X.get(i, j))
                    );
                }
            }
        }
        return X.create(View2d.of(new Extent2d(QR.cols(), B.cols())));
    }

    /**
     * Returns whether the matrix {@code A} has full rank.
     *
     * @return true if {@code R}, and hence {@code A}, has full rank
     */
    public boolean hasFullRank() {
        for (int j = 0; j < QR.cols(); ++j) {
            if (context.isZero(rdiag.get(j))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Performs an <em>QR</em>-decomposition of the given matrix {@code A},
     * computed by Householder reflections.
     *
     * @param A the matrix to be decomposed
     * @return the <em>QR</em>-decomposition of the given matrix {@code A}
     * @throws IllegalArgumentException if {@code A.rows() < A.cols()}
     */
    public static QR decompose(final DoubleMatrix2d A) {
        checkRectangular(A);

        final var qr = A.copy();

        final var m = qr.rows();
        final var n = qr.cols();
        final var Rdiag = qr.colAt(0).like();

        final var QRcolumnsPart = new DoubleMatrix1d[n];
        for (int k = 0; k < n; ++k) {
            QRcolumnsPart[k] = qr.colAt(k)
                .view(new Range1d(new Index1d(k), new Extent1d(m - k)));
        }

        // Main loop.
        for (int k = 0; k < n; ++k) {
            double nrm = 0;
            for (int i = k; i < m; ++i) {
                nrm = Math.hypot(nrm, qr.get(i, k));
            }

            if (nrm != 0.0) {
                // Form k-th Householder vector.
                if (qr.get(k, k) < 0) {
                    nrm = -nrm;
                }
                final var divisor = 1.0/nrm;
                QRcolumnsPart[k].assign(x -> x*divisor);

                qr.set(k, k, qr.get(k, k) + 1);

                // Apply transformation to remaining columns.
                for (int j = k + 1; j < n; ++j) {
                    final DoubleMatrix1d QRcolj = qr.colAt(j)
                        .view(new Range1d(new Index1d(k), new Extent1d(m - k)));

                    double s = QRcolumnsPart[k].dotProduct(QRcolj);
                    s = -s/qr.get(k, k);
                    for (int i = k; i < m; ++i) {
                        qr.set(
                            i, j,
                            Math.fma(s, qr.get(i, k), qr.get(i, j))
                        );
                    }
                }
            }
            Rdiag.set(k, -nrm);
        }

        return new QR(qr, Rdiag, NumericalContext.get());
    }

}
