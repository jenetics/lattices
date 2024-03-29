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

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static io.jenetics.lattices.grid.Structures.checkRectangular;

import io.jenetics.lattices.matrix.DoubleMatrix1d;
import io.jenetics.lattices.matrix.DoubleMatrix2d;

/**
 * Store the result of an <em>Singular value</em>-decomposition.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public final class SingularValue {

    // Arrays for internal storage of U and V.
    private double[][] U, V;

    // Array for internal storage of singular values.
    private double[] s;

    // Row and column dimensions.
    private int m, n;

    private SingularValue() {
    }

    private void init(DoubleMatrix2d Arg) {
        checkRectangular(Arg.extent());

        // Derived from LINPACK code.
        // Initialize.
        final double[][] A = new double[Arg.rows()][Arg.cols()];
        Arg.forEach((i, j) -> A[i][j] = Arg.get(i, j));

        m = Arg.rows();
        n = Arg.cols();
        int nu = min(m, n);
        s = new double[min(m + 1, n)];
        U = new double[m][nu];
        V = new double[n][n];
        double[] e = new double[n];
        double[] work = new double[m];
        boolean wantu = true;
        boolean wantv = true;

        // Reduce A to bi-diagonal form, storing the diagonal elements
        // in s and the super-diagonal elements in e.

        int nct = min(m - 1, n);
        int nrt = max(0, min(n - 2, m));
        for (int k = 0; k < max(nct, nrt); ++k) {
            if (k < nct) {

                // Compute the transformation for the k-th column and
                // place the k-th diagonal in s[k].
                // Compute 2-norm of k-th column without under/overflow.
                s[k] = 0;
                for (int i = k; i < m; ++i) {
                    s[k] = Math.hypot(s[k], A[i][k]);
                }
                if (s[k] != 0.0) {
                    if (A[k][k] < 0.0) {
                        s[k] = -s[k];
                    }
                    for (int i = k; i < m; ++i) {
                        A[i][k] /= s[k];
                    }
                    A[k][k] += 1.0;
                }
                s[k] = -s[k];
            }
            for (int j = k + 1; j < n; ++j) {
                if ((k < nct) & (s[k] != 0.0)) {
                    // Apply the transformation.
                    double t = 0;
                    for (int i = k; i < m; ++i) {
                        t = Math.fma(A[i][k], A[i][j], t);
                    }
                    t = -t/A[k][k];
                    for (int i = k; i < m; ++i) {
                        A[i][j] = Math.fma(t, A[i][k], A[i][j]);
                    }
                }

                // Place the k-th row of A into e for the
                // subsequent calculation of the row transformation.
                e[j] = A[k][j];
            }
            if (wantu && (k < nct)) {
                // Place the transformation in U for subsequent back
                // multiplication.
                for (int i = k; i < m; ++i) {
                    U[i][k] = A[i][k];
                }
            }
            if (k < nrt) {

                // Compute the k-th row transformation and place the
                // k-th super-diagonal in e[k].
                // Compute 2-norm without under/overflow.
                e[k] = 0;
                for (int i = k + 1; i < n; ++i) {
                    e[k] = Math.hypot(e[k], e[i]);
                }
                if (e[k] != 0.0) {
                    if (e[k + 1] < 0.0) {
                        e[k] = -e[k];
                    }
                    for (int i = k + 1; i < n; ++i) {
                        e[i] /= e[k];
                    }
                    e[k + 1] += 1.0;
                }
                e[k] = -e[k];
                if ((k + 1 < m) & (e[k] != 0.0)) {

                    // Apply the transformation.

                    for (int i = k + 1; i < m; ++i) {
                        work[i] = 0.0;
                    }
                    for (int j = k + 1; j < n; ++j) {
                        for (int i = k + 1; i < m; ++i) {
                            work[i] = Math.fma(e[j], A[i][j], work[i]);
                        }
                    }
                    for (int j = k + 1; j < n; ++j) {
                        double t = -e[j] / e[k + 1];
                        for (int i = k + 1; i < m; ++i) {
                            A[i][j] = Math.fma(t, work[i], A[i][j]);
                        }
                    }
                }
                if (wantv) {
                    // Place the transformation in V for subsequent
                    // back multiplication.
                    for (int i = k + 1; i < n; ++i) {
                        V[i][k] = e[i];
                    }
                }
            }
        }

        // Set up the final bidiagonal matrix or order p.

        int p = min(n, m + 1);
        if (nct < n) {
            s[nct] = A[nct][nct];
        }
        if (m < p) {
            s[p - 1] = 0.0;
        }
        if (nrt + 1 < p) {
            e[nrt] = A[nrt][p - 1];
        }
        e[p - 1] = 0.0;

        // If required, generate U.

        if (wantu) {
            for (int j = nct; j < nu; ++j) {
                for (int i = 0; i < m; ++i) {
                    U[i][j] = 0.0;
                }
                U[j][j] = 1.0;
            }
            for (int k = nct - 1; k >= 0; --k) {
                if (s[k] != 0.0) {
                    for (int j = k + 1; j < nu; ++j) {
                        double t = 0;
                        for (int i = k; i < m; ++i) {
                            t = Math.fma(U[i][k], U[i][j], t);
                        }
                        t = -t / U[k][k];
                        for (int i = k; i < m; ++i) {
                            U[i][j] = Math.fma(t, U[i][k], U[i][j]);
                        }
                    }
                    for (int i = k; i < m; ++i) {
                        U[i][k] = -U[i][k];
                    }
                    U[k][k] = 1.0 + U[k][k];
                    for (int i = 0; i < k - 1; ++i) {
                        U[i][k] = 0.0;
                    }
                } else {
                    for (int i = 0; i < m; ++i) {
                        U[i][k] = 0.0;
                    }
                    U[k][k] = 1.0;
                }
            }
        }

        // If required, generate V.
        if (wantv) {
            for (int k = n - 1; k >= 0; --k) {
                if ((k < nrt) & (e[k] != 0.0)) {
                    for (int j = k + 1; j < nu; ++j) {
                        double t = 0;
                        for (int i = k + 1; i < n; ++i) {
                            t = Math.fma(V[i][k], V[i][j], t);
                        }
                        t = -t / V[k + 1][k];
                        for (int i = k + 1; i < n; i++) {
                            V[i][j] = Math.fma(t, V[i][k], V[i][j]);
                        }
                    }
                }
                for (int i = 0; i < n; ++i) {
                    V[i][k] = 0.0;
                }
                V[k][k] = 1.0;
            }
        }

        // Main iteration loop for the singular values.
        int pp = p - 1;
        int iter = 0;
        double eps = Math.pow(2.0, -52.0);
        while (p > 0) {
            int k, kase;

            // Here is where a test for too many iterations would go.

            // This section of the program inspects for
            // negligible elements in the s and e arrays.  On
            //  completion, the variables kase and k are set as follows.

            // case = 1     if s(p) and e[k-1] are negligible and k<p
            // case = 2     if s(k) is negligible and k<p
            // case = 3     if e[k-1] is negligible, k<p, and
            //              s(k), ..., s(p) are not negligible (qr step).
            // case = 4     if e(p-1) is negligible (convergence).

            for (k = p - 2; k >= -1; --k) {
                if (k == -1) {
                    break;
                }
                if (abs(e[k]) <= eps * (abs(s[k]) + abs(s[k + 1]))) {
                    e[k] = 0.0;
                    break;
                }
            }
            if (k == p - 2) {
                kase = 4;
            } else {
                int ks;
                for (ks = p - 1; ks >= k; ks--) {
                    if (ks == k) {
                        break;
                    }
                    double t = (ks != p ? abs(e[ks]) : 0.0) +
                        (ks != k + 1 ? abs(e[ks - 1]) : 0.0);
                    if (abs(s[ks]) <= eps * t) {
                        s[ks] = 0.0;
                        break;
                    }
                }
                if (ks == k) {
                    kase = 3;
                } else if (ks == p - 1) {
                    kase = 1;
                } else {
                    kase = 2;
                    k = ks;
                }
            }
            k++;

            // Perform the task indicated by kase.
            switch (kase) {
                // Deflate negligible s(p).
                case 1 -> {
                    double f = e[p - 2];
                    e[p - 2] = 0.0;
                    for (int j = p - 2; j >= k; --j) {
                        double t = Math.hypot(s[j], f);
                        double cs = s[j] / t;
                        double sn = f / t;
                        s[j] = t;
                        if (j != k) {
                            f = -sn * e[j - 1];
                            e[j - 1] = cs * e[j - 1];
                        }
                        if (wantv) {
                            for (int i = 0; i < n; ++i) {
                                t = cs * V[i][j] + sn * V[i][p - 1];
                                V[i][p - 1] = -sn * V[i][j] + cs * V[i][p - 1];
                                V[i][j] = t;
                            }
                        }
                    }
                }

                // Split at negligible s(k).
                case 2 -> {
                    double f = e[k - 1];
                    e[k - 1] = 0.0;
                    for (int j = k; j < p; ++j) {
                        double t = Math.hypot(s[j], f);
                        double cs = s[j] / t;
                        double sn = f / t;
                        s[j] = t;
                        f = -sn * e[j];
                        e[j] = cs * e[j];
                        if (wantu) {
                            for (int i = 0; i < m; ++i) {
                                t = cs * U[i][j] + sn * U[i][k - 1];
                                U[i][k - 1] = -sn * U[i][j] + cs * U[i][k - 1];
                                U[i][j] = t;
                            }
                        }
                    }
                }

                // Perform one qr step.
                case 3 -> {
                    // Calculate the shift.
                    double scale = max(max(max(max(abs(s[p - 1]), abs(s[p - 2])), abs(e[p - 2])), abs(s[k])), abs(e[k]));
                    double sp = s[p - 1] / scale;
                    double spm1 = s[p - 2] / scale;
                    double epm1 = e[p - 2] / scale;
                    double sk = s[k] / scale;
                    double ek = e[k] / scale;
                    double b = ((spm1 + sp) * (spm1 - sp) + epm1 * epm1) / 2.0;
                    double c = (sp * epm1) * (sp * epm1);
                    double shift = 0.0;
                    if ((b != 0.0) | (c != 0.0)) {
                        shift = Math.sqrt(Math.fma(b, b, c));
                        if (b < 0.0) {
                            shift = -shift;
                        }
                        shift = c / (b + shift);
                    }
                    double f = (sk + sp) * (sk - sp) + shift;
                    double g = sk * ek;

                    // Chase zeros.
                    for (int j = k; j < p - 1; ++j) {
                        double t = Math.hypot(f, g);
                        double cs = f / t;
                        double sn = g / t;
                        if (j != k) {
                            e[j - 1] = t;
                        }
                        f = cs * s[j] + sn * e[j];
                        e[j] = cs * e[j] - sn * s[j];
                        g = sn * s[j + 1];
                        s[j + 1] = cs * s[j + 1];
                        if (wantv) {
                            for (int i = 0; i < n; ++i) {
                                t = cs * V[i][j] + sn * V[i][j + 1];
                                V[i][j + 1] = -sn * V[i][j] + cs * V[i][j + 1];
                                V[i][j] = t;
                            }
                        }
                        t = Math.hypot(f, g);
                        cs = f / t;
                        sn = g / t;
                        s[j] = t;
                        f = cs * e[j] + sn * s[j + 1];
                        s[j + 1] = -sn * e[j] + cs * s[j + 1];
                        g = sn * e[j + 1];
                        e[j + 1] = cs * e[j + 1];
                        if (wantu && (j < m - 1)) {
                            for (int i = 0; i < m; ++i) {
                                t = cs * U[i][j] + sn * U[i][j + 1];
                                U[i][j + 1] = -sn * U[i][j] + cs * U[i][j + 1];
                                U[i][j] = t;
                            }
                        }
                    }
                    e[p - 2] = f;
                    iter = iter + 1;
                }

                // Convergence.
                case 4 -> {
                    // Make the singular values positive.
                    if (s[k] <= 0.0) {
                        s[k] = (s[k] < 0.0 ? -s[k] : 0.0);
                        if (wantv) {
                            for (int i = 0; i <= pp; ++i) {
                                V[i][k] = -V[i][k];
                            }
                        }
                    }

                    // Order the singular values.
                    while (k < pp) {
                        if (s[k] >= s[k + 1]) {
                            break;
                        }
                        double t = s[k];
                        s[k] = s[k + 1];
                        s[k + 1] = t;
                        if (wantv && (k < n - 1)) {
                            for (int i = 0; i < n; ++i) {
                                t = V[i][k + 1];
                                V[i][k + 1] = V[i][k];
                                V[i][k] = t;
                            }
                        }
                        if (wantu && (k < m - 1)) {
                            for (int i = 0; i < m; ++i) {
                                t = U[i][k + 1];
                                U[i][k + 1] = U[i][k];
                                U[i][k] = t;
                            }
                        }
                        k++;
                    }
                    iter = 0;
                    p--;
                }
            }
        }
    }

    /**
     * Returns the diagonal matrix of singular values.
     *
     * @return S
     */
    public DoubleMatrix2d S() {
        final var S = DoubleMatrix2d.DENSE.create(n, n);
        for (int i = 0; i < n; ++i) {
            S.set(i, i, s[i]);
        }

        return S;
    }

    /**
     * Return the left singular vectors {@code U}.
     *
     * @return {@code U}
     */
    public DoubleMatrix2d U() {
        final var U = DoubleMatrix2d.DENSE.create(m, min(m, n));
        U.assign(this.U);
        return U;
    }

    /**
     * Return the right singular vectors {@code V}.
     *
     * @return {@code U}
     */
    public DoubleMatrix2d V() {
        final var V = DoubleMatrix2d.DENSE.create(n, n);
        V.assign(this.V);
        return V;
    }

    /**
     * Return the diagonal of {@code S}, which is a one-dimensional array of
     * singular values.
     *
     * @return diagonal of {@code S}
     */
    public DoubleMatrix1d values() {
        final var sv = DoubleMatrix1d.DENSE.create(s.length);
        sv.assign(s);
        return sv;
    }

    /**
     * Return the two norm, which is {@code max(S)}.
     */
    public double norm2() {
        return s[0];
    }

    /**
     * Return the effective numerical matrix rank, which is the number of
     * non-negligible singular values.
     */
    public int rank() {
        double eps = Math.pow(2.0, -52.0);
        double tol = max(m, n)*s[0]*eps;
        int r = 0;
        for (double v : s) {
            if (v > tol) {
                r++;
            }
        }
        return r;
    }

    /**
     * Return the two norm condition number: {@code max(S) / min(S)}.
     */
    public double cond() {
        return s[0]/s[Math.min(m, n) - 1];
    }

    /**
     * Performs an <em>Singular value</em>-decomposition of the given matrix
     * {@code A}.
     *
     * @param A the square matrix to be decomposed
     * @return the <em>Eigenvalue</em>-decomposition of the given matrix {@code A}
     * @throws IllegalArgumentException if {@code A} is not square
     */
    public static SingularValue decompose(DoubleMatrix2d A) {
        final var singular = new SingularValue();
        singular.init(A);
        return singular;
    }

}
