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

import static io.jenetics.lattices.matrix.Matrices.isSymmetric;
import static io.jenetics.lattices.structure.Structures.checkSquare;

import io.jenetics.lattices.matrix.DoubleMatrix1d;
import io.jenetics.lattices.matrix.DoubleMatrix2d;
import io.jenetics.lattices.matrix.NumericalContext;

/**
 * Store the result of an <em>Eigenvalue</em>-decomposition.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public final class Eigenvalue {

    private final NumericalContext context = NumericalContext.get();

    // Row and column dimension (square matrix).
    private int n;

    // Arrays for internal storage of eigenvalues.
    private double[] d, e;

    // Array for internal storage of eigenvectors.
    private DoubleMatrix2d V;

    // Array for internal storage of non-symmetric Hessenberg form.
    private double[][] H;

    // Working storage for non-symmetric algorithm.
    private double[] ort;

    // Complex scalar division.
    private double cdivr, cdivi;

    private Eigenvalue() {
    }

    private void init(DoubleMatrix2d A) {
        checkSquare(A.extent());

        n = A.cols();
        d = new double[n];
        e = new double[n];

        if (isSymmetric(A)) {
            V = A.copy();

            // Tri-diagonalize.
            tred2();

            // Diagonalize.
            tql2();

        } else {
            V = DoubleMatrix2d.DENSE.create(n, n);
            H = new double[n][n];
            ort = new double[n];

            A.forEach((i, j) -> H[i][j] = A.get(i, j));

            // Reduce to Hessenberg form.
            orthes();

            // Reduce Hessenberg to real Schur form.
            hqr2();
        }
    }

    /*
     * Symmetric Householder reduction to tri-diagonal form.
     *
     * This is derived from the Algol procedures tred2 by Bowdler, Martin,
     * Reinsch, and Wilkinson, Handbook for Auto. Comp., Vol.ii-Linear
     * Algebra, and the corresponding Fortran subroutine in EISPACK.
     */
    private void tred2() {
        for (int j = 0; j < n; ++j) {
            d[j] = V.get(n - 1, j);
        }

        // Householder reduction to tri-diagonal form.
        for (int i = n - 1; i > 0; --i) {
            // Scale to avoid under/overflow.

            double scale = 0.0;
            double h = 0.0;
            for (int k = 0; k < i; ++k) {
                scale = scale + Math.abs(d[k]);
            }

            if (context.isZero(scale)) {
                e[i] = d[i - 1];
                for (int j = 0; j < i; ++j) {
                    d[j] = V.get(i - 1, j);
                    V.set(i, j, 0);
                    V.set(j, i, 0);
                }
            } else {
                // Generate Householder vector.
                for (int k = 0; k < i; ++k) {
                    d[k] /= scale;
                    //h += d[k]*d[k];
                    h = Math.fma(d[k], d[k], h);
                }
                double f = d[i - 1];
                double g = Math.sqrt(h);
                if (context.isGreaterZero(f)) {
                    g = -g;
                }
                e[i] = scale*g;
                //h = h - f * g;
                h = -Math.fma(f, g, -h);
                d[i - 1] = f - g;
                for (int j = 0; j < i; ++j) {
                    e[j] = 0.0;
                }

                // Apply similarity transformation to remaining columns.
                for (int j = 0; j < i; ++j) {
                    f = d[j];
                    V.set(j, i, f);
                    //g = e[j] + V.get(j, j)*f;
                    g = Math.fma(V.get(j, j), f, e[j]);
                    for (int k = j + 1; k <= i - 1; ++k) {
                        //g += V.get(k, j)*d[k];
                        g = Math.fma(V.get(k, j), d[k], g);
                        //e[k] += V.get(k, j)*f;
                        e[k] = Math.fma(V.get(k, j), f, e[k]);
                    }
                    e[j] = g;
                }
                f = 0.0;
                for (int j = 0; j < i; ++j) {
                    e[j] /= h;
                    //f += e[j] * d[j];
                    f = Math.fma(e[j], d[j], f);
                }
                double hh = f / (h + h);
                for (int j = 0; j < i; ++j) {
                    e[j] -= hh*d[j];
                }
                for (int j = 0; j < i; ++j) {
                    f = d[j];
                    g = e[j];
                    for (int k = j; k <= i - 1; ++k) {
                        V.set(k, j, V.get(k, j) - (f*e[k] + g*d[k]));
                    }
                    d[j] = V.get(i - 1, j);
                    V.set(i, j, 0);
                }
            }
            d[i] = h;
        }

        // Accumulate transformations.
        for (int i = 0; i < n - 1; ++i) {
            V.set(n - 1, i, V.get(i, i));
            V.set(i, i, 1);
            double h = d[i + 1];
            if (context.isNotZero(h)) {
                for (int k = 0; k <= i; ++k) {
                    d[k] = V.get(k, i + 1)/h;
                }
                for (int j = 0; j <= i; ++j) {
                    double g = 0.0;
                    for (int k = 0; k <= i; ++k) {
                        //g += V.get(k, i + 1)*V.get(k, j);
                        g = Math.fma(V.get(k, i + 1), V.get(k, j), g);
                    }
                    for (int k = 0; k <= i; k++) {
                        //V.set(k, j, V.get(k, j) - g*d[k]);
                        V.set(k, j, -Math.fma(g, d[k], -V.get(k, j)));
                    }
                }
            }
            for (int k = 0; k <= i; k++) {
                V.set(k, i + 1, 0);
            }
        }
        for (int j = 0; j < n; ++j) {
            d[j] = V.get(n - 1, j);
            V.set(n - 1, j, 0);
        }
        V.set(n - 1, n - 1, 1);
        e[0] = 0.0;
    }

    /*
     * Symmetric tri-diagonal QL algorithm.
     *
     * This is derived from the Algol procedures tql2, by Bowdler, Martin,
     * Reinsch, and Wilkinson, Handbook for Auto. Comp., Vol.ii-Linear Algebra,
     * and the corresponding Fortran subroutine in EISPACK.
     */
    private void tql2() {
        for (int i = 1; i < n; ++i) {
            e[i - 1] = e[i];
        }
        e[n - 1] = 0.0;

        double f = 0.0;
        double tst1 = 0.0;
        double eps = Math.pow(2.0, -52.0);
        for (int l = 0; l < n; ++l) {

            // Find small sub-diagonal element
            tst1 = Math.max(tst1, Math.abs(d[l]) + Math.abs(e[l]));
            int m = l;
            while (m < n) {
                if (Math.abs(e[m]) <= eps*tst1) {
                    break;
                }
                ++m;
            }

            // If m == l, d[l] is an eigenvalue,
            // otherwise, iterate.
            if (m > l) {
                int iter = 0;
                do {
                    // (Could check iteration count here.)
                    iter = iter + 1;

                    // Compute implicit shift
                    double g = d[l];
                    double p = (d[l + 1] - g)/(2.0*e[l]);
                    double r = Math.hypot(p, 1.0);
                    if (p < 0) {
                        r = -r;
                    }
                    d[l] = e[l]/(p + r);
                    d[l + 1] = e[l]*(p + r);
                    double dl1 = d[l + 1];
                    double h = g - d[l];
                    for (int i = l + 2; i < n; i++) {
                        d[i] -= h;
                    }
                    f = f + h;

                    // Implicit QL transformation.
                    p = d[m];
                    double c = 1.0;
                    double c2 = c;
                    double c3 = c;
                    double el1 = e[l + 1];
                    double s = 0.0;
                    double s2 = 0.0;
                    for (int i = m - 1; i >= l; i--) {
                        c3 = c2;
                        c2 = c;
                        s2 = s;
                        g = c*e[i];
                        h = c*p;
                        r = Math.hypot(p, e[i]);
                        e[i + 1] = s*r;
                        s = e[i]/r;
                        c = p/r;
                        p = c*d[i] - s*g;
                        d[i + 1] = h + s*(c*g + s*d[i]);

                        // Accumulate transformation.
                        for (int k = 0; k < n; ++k) {
                            h = V.get(k, i + 1);
                            V.set(k, i + 1, s*V.get(k, i) + c*h);
                            V.set(k, i, c*V.get(k, i) - s*h);
                        }
                    }
                    p = -s*s2*c3*el1*e[l]/dl1;
                    e[l] = s*p;
                    d[l] = c*p;

                    // Check for convergence.
                } while (Math.abs(e[l]) > eps*tst1);
            }
            d[l] = d[l] + f;
            e[l] = 0.0;
        }

        // Sort eigenvalues and corresponding vectors.

        for (int i = 0; i < n - 1; ++i) {
            int k = i;
            double p = d[i];
            for (int j = i + 1; j < n; ++j) {
                if (d[j] < p) {
                    k = j;
                    p = d[j];
                }
            }
            if (k != i) {
                d[k] = d[i];
                d[i] = p;
                for (int j = 0; j < n; ++j) {
                    p = V.get(j, i);
                    V.set(j, i, V.get(j, k));
                    V.set(j, k, p);
                }
            }
        }
    }

    /*
     * Non-symmetric reduction to Hessenberg form.
     *
     * This is derived from the Algol procedures orthes and ortran, by Martin
     * and Wilkinson, Handbook for Auto. Comp., Vol.ii-Linear Algebra, and the
     * corresponding Fortran subroutines in EISPACK.
     */
    private void orthes() {
        int low = 0;
        int high = n - 1;

        for (int m = low + 1; m <= high - 1; ++m) {
            // Scale column.
            double scale = 0.0;
            for (int i = m; i <= high; ++i) {
                scale += Math.abs(H[i][m - 1]);
            }
            if (context.isNotZero(scale)) {
                // Compute Householder transformation.
                double h = 0.0;
                for (int i = high; i >= m; i--) {
                    ort[i] = H[i][m - 1]/scale;
                    h = Math.fma(ort[i], ort[i], h);
                }
                double g = Math.sqrt(h);
                if (ort[m] > 0) {
                    g = -g;
                }
                h = -Math.fma(ort[m], g, -h);
                ort[m] = ort[m] - g;

                // Apply Householder similarity transformation
                // H = (I-u*u'/h)*H*(I-u*u')/h)

                for (int j = m; j < n; ++j) {
                    double f = 0.0;
                    for (int i = high; i >= m; i--) {
                        f = Math.fma(ort[i], H[i][j], f);
                    }
                    f = f/h;
                    for (int i = m; i <= high; ++i) {
                        H[i][j] = -Math.fma(f, ort[i], -H[i][j]);
                    }
                }

                for (int i = 0; i <= high; ++i) {
                    double f = 0.0;
                    for (int j = high; j >= m; j--) {
                        f = Math.fma(ort[j], H[i][j], f);
                    }
                    f = f/h;
                    for (int j = m; j <= high; ++j) {
                        H[i][j] = -Math.fma(f, ort[j], -H[i][j]);
                    }
                }
                ort[m] = scale*ort[m];
                H[m][m - 1] = scale*g;
            }
        }

        // Accumulate transformations (Algol's ortran).
        V.forEach((i, j) -> V.set(i, j, (i == j ? 1.0 : 0.0)));

        for (int m = high - 1; m >= low + 1; m--) {
            if (H[m][m - 1] != 0.0) {
                for (int i = m + 1; i <= high; ++i) {
                    ort[i] = H[i][m - 1];
                }
                for (int j = m; j <= high; ++j) {
                    double g = 0.0;
                    for (int i = m; i <= high; ++i) {
                        g = Math.fma(ort[i], V.get(i, j), g);
                    }
                    // Double division avoids possible underflow
                    g = (g / ort[m]) / H[m][m - 1];
                    for (int i = m; i <= high; i++) {
                        V.set(i, j, Math.fma(g, ort[i], V.get(i, j)));
                    }
                }
            }
        }
    }

    /*
     * Non-symmetric reduction from Hessenberg to real Schur form.
     *
     * This is derived from the Algol procedures orthes and ortran, by Martin
     * and Wilkinson, Handbook for Auto. Comp., Vol.ii-Linear Algebra, and the
     * corresponding Fortran subroutines in EISPACK.
     */
    private void hqr2() {
        // Initialize

        final int nn = this.n;
        int n = nn - 1;
        int low = 0;
        int high = nn - 1;
        double eps = Math.pow(2.0, -52.0);
        double exshift = 0.0;
        double p = 0, q = 0, r = 0, s = 0, z = 0, t, w, x, y;

        // Store roots isolated by balanc and compute matrix norm

        double norm = 0.0;
        for (int i = 0; i < nn; ++i) {
            if (i < low || i > high) {
                d[i] = H[i][i];
                e[i] = 0.0;
            }
            for (int j = Math.max(i - 1, 0); j < nn; ++j) {
                norm += Math.abs(H[i][j]);
            }
        }

        // Outer loop over eigenvalue index

        int iter = 0;
        while (n >= low) {
            // Look for single small sub-diagonal element

            int l = n;
            while (l > low) {
                s = Math.abs(H[l - 1][l - 1]) + Math.abs(H[l][l]);
                if (s == 0.0) {
                    s = norm;
                }
                if (Math.abs(H[l][l - 1]) < eps*s) {
                    break;
                }
                l--;
            }

            // Check for convergence
            // One root found

            if (l == n) {
                H[n][n] = H[n][n] + exshift;
                d[n] = H[n][n];
                e[n] = 0.0;
                n--;
                iter = 0;

                // Two roots found

            } else if (l == n - 1) {
                w = H[n][n - 1]*H[n - 1][n];
                p = (H[n - 1][n - 1] - H[n][n])/2.0;
                q = Math.fma(p, p,w);
                z = Math.sqrt(Math.abs(q));
                H[n][n] = H[n][n] + exshift;
                H[n - 1][n - 1] = H[n - 1][n - 1] + exshift;
                x = H[n][n];

                // Real pair

                if (q >= 0) {
                    if (p >= 0) {
                        z = p + z;
                    } else {
                        z = p - z;
                    }
                    d[n - 1] = x + z;
                    d[n] = d[n - 1];
                    if (z != 0.0) {
                        d[n] = x - w / z;
                    }
                    e[n - 1] = 0.0;
                    e[n] = 0.0;
                    x = H[n][n - 1];
                    s = Math.abs(x) + Math.abs(z);
                    p = x / s;
                    q = z / s;
                    r = Math.hypot(p, q);
                    p = p / r;
                    q = q / r;

                    // Row modification

                    for (int j = n - 1; j < nn; ++j) {
                        z = H[n - 1][j];
                        H[n - 1][j] = q*z + p*H[n][j];
                        H[n][j] = q*H[n][j] - p*z;
                    }

                    // Column modification

                    for (int i = 0; i <= n; ++i) {
                        z = H[i][n - 1];
                        H[i][n - 1] = q*z + p*H[i][n];
                        H[i][n] = q*H[i][n] - p*z;
                    }

                    // Accumulate transformations

                    for (int i = low; i <= high; ++i) {
                        z = V.get(i, n - 1);
                        V.set(i, n - 1, q*z + p*V.get(i, n));
                        V.set(i, n, q*V.get(i, n) - p*z);
                    }

                    // Complex pair
                } else {
                    d[n - 1] = x + p;
                    d[n] = x + p;
                    e[n - 1] = z;
                    e[n] = -z;
                }
                n = n - 2;
                iter = 0;

                // No convergence yet
            } else {

                // Form shift

                x = H[n][n];
                y = 0.0;
                w = 0.0;
                if (l < n) {
                    y = H[n - 1][n - 1];
                    w = H[n][n - 1] * H[n - 1][n];
                }

                // Wilkinson's original ad hoc shift

                if (iter == 10) {
                    exshift += x;
                    for (int i = low; i <= n; ++i) {
                        H[i][i] -= x;
                    }
                    s = Math.abs(H[n][n - 1]) + Math.abs(H[n - 1][n - 2]);
                    x = y = 0.75*s;
                    w = -0.4375*s*s;
                }

                // MATLAB's new ad hoc shift

                if (iter == 30) {
                    s = (y - x) / 2.0;
                    s = s*s + w;
                    if (s > 0) {
                        s = Math.sqrt(s);
                        if (y < x) {
                            s = -s;
                        }
                        s = x - w / ((y - x) / 2.0 + s);
                        for (int i = low; i <= n; ++i) {
                            H[i][i] -= s;
                        }
                        exshift += s;
                        x = y = w = 0.964;
                    }
                }

                iter = iter + 1;   // (Could check iteration count here.)

                // Look for two consecutive small sub-diagonal elements

                int m = n - 2;
                while (m >= l) {
                    z = H[m][m];
                    r = x - z;
                    s = y - z;
                    p = (r*s - w) / H[m + 1][m] + H[m][m + 1];
                    q = H[m + 1][m + 1] - z - r - s;
                    r = H[m + 2][m + 1];
                    s = Math.abs(p) + Math.abs(q) + Math.abs(r);
                    p = p / s;
                    q = q / s;
                    r = r / s;
                    if (m == l) {
                        break;
                    }
                    if (Math.abs(H[m][m - 1]) * (Math.abs(q) + Math.abs(r)) <
                        eps * (Math.abs(p) * (Math.abs(H[m - 1][m - 1]) + Math.abs(z) +
                            Math.abs(H[m + 1][m + 1]))))
                    {
                        break;
                    }
                    m--;
                }

                for (int i = m + 2; i <= n; ++i) {
                    H[i][i - 2] = 0.0;
                    if (i > m + 2) {
                        H[i][i - 3] = 0.0;
                    }
                }

                // Double QR step involving rows l:n and columns m:n

                for (int k = m; k <= n - 1; ++k) {
                    boolean notlast = (k != n - 1);
                    if (k != m) {
                        p = H[k][k - 1];
                        q = H[k + 1][k - 1];
                        r = (notlast ? H[k + 2][k - 1] : 0.0);
                        x = Math.abs(p) + Math.abs(q) + Math.abs(r);
                        if (x != 0.0) {
                            p = p / x;
                            q = q / x;
                            r = r / x;
                        }
                    }
                    if (x == 0.0) {
                        break;
                    }
                    s = Math.sqrt(p*p + q*q + r*r);
                    if (p < 0) {
                        s = -s;
                    }
                    if (s != 0) {
                        if (k != m) {
                            H[k][k - 1] = -s * x;
                        } else if (l != m) {
                            H[k][k - 1] = -H[k][k - 1];
                        }
                        p = p + s;
                        x = p / s;
                        y = q / s;
                        z = r / s;
                        q = q / p;
                        r = r / p;

                        // Row modification

                        for (int j = k; j < nn; j++) {
                            p = H[k][j] + q * H[k + 1][j];
                            if (notlast) {
                                p = p + r * H[k + 2][j];
                                H[k + 2][j] = H[k + 2][j] - p * z;
                            }
                            H[k][j] = H[k][j] - p * x;
                            H[k + 1][j] = H[k + 1][j] - p * y;
                        }

                        // Column modification

                        for (int i = 0; i <= Math.min(n, k + 3); i++) {
                            p = x * H[i][k] + y * H[i][k + 1];
                            if (notlast) {
                                p = p + z * H[i][k + 2];
                                H[i][k + 2] = H[i][k + 2] - p * r;
                            }
                            H[i][k] = H[i][k] - p;
                            H[i][k + 1] = H[i][k + 1] - p * q;
                        }

                        // Accumulate transformations

                        for (int i = low; i <= high; i++) {
                            p = x*V.get(i, k) + y*V.get(i, k + 1);
                            if (notlast) {
                                p = p + z*V.get(i, k + 2);
                                V.set(i, k + 2, V.get(i, k + 2) - p*r);
                            }
                            V.set(i, k, V.get(i, k) - p);
                            V.set(i, k + 1, V.get(i, k + 1) - p*q);
                        }
                    }  // (s != 0)
                }  // k loop
            }  // check convergence
        }  // while (n >= low)

        // Backsubstitute to find vectors of upper triangular form

        if (norm == 0.0) {
            return;
        }

        for (n = nn - 1; n >= 0; n--) {
            p = d[n];
            q = e[n];

            // Real vector

            if (q == 0) {
                int l = n;
                H[n][n] = 1.0;
                for (int i = n - 1; i >= 0; i--) {
                    w = H[i][i] - p;
                    r = 0.0;
                    for (int j = l; j <= n; j++) {
                        r = Math.fma(H[i][j], H[j][n], r);
                    }
                    if (e[i] < 0.0) {
                        z = w;
                        s = r;
                    } else {
                        l = i;
                        if (e[i] == 0.0) {
                            if (w != 0.0) {
                                H[i][n] = -r / w;
                            } else {
                                H[i][n] = -r / (eps * norm);
                            }

                            // Solve real equations

                        } else {
                            x = H[i][i + 1];
                            y = H[i + 1][i];
                            q = (d[i] - p) * (d[i] - p) + e[i] * e[i];
                            t = (x * s - z * r) / q;
                            H[i][n] = t;
                            if (Math.abs(x) > Math.abs(z)) {
                                H[i + 1][n] = (-r - w * t) / x;
                            } else {
                                H[i + 1][n] = (-s - y * t) / z;
                            }
                        }

                        // Overflow control

                        t = Math.abs(H[i][n]);
                        if ((eps * t) * t > 1) {
                            for (int j = i; j <= n; j++) {
                                H[j][n] = H[j][n] / t;
                            }
                        }
                    }
                }

                // Complex vector

            } else if (q < 0) {
                int l = n - 1;

                // Last vector component imaginary so matrix is triangular

                if (Math.abs(H[n][n - 1]) > Math.abs(H[n - 1][n])) {
                    H[n - 1][n - 1] = q / H[n][n - 1];
                    H[n - 1][n] = -(H[n][n] - p) / H[n][n - 1];
                } else {
                    cdiv(0.0, -H[n - 1][n], H[n - 1][n - 1] - p, q);
                    H[n - 1][n - 1] = cdivr;
                    H[n - 1][n] = cdivi;
                }
                H[n][n - 1] = 0.0;
                H[n][n] = 1.0;
                for (int i = n - 2; i >= 0; i--) {
                    double ra, sa, vr, vi;
                    ra = 0.0;
                    sa = 0.0;
                    for (int j = l; j <= n; j++) {
                        ra = Math.fma(H[i][j], H[j][n - 1], ra);
                        sa = Math.fma(H[i][j], H[j][n], sa);
                    }
                    w = H[i][i] - p;

                    if (e[i] < 0.0) {
                        z = w;
                        r = ra;
                        s = sa;
                    } else {
                        l = i;
                        if (e[i] == 0) {
                            cdiv(-ra, -sa, w, q);
                            H[i][n - 1] = cdivr;
                            H[i][n] = cdivi;
                        } else {

                            // Solve complex equations

                            x = H[i][i + 1];
                            y = H[i + 1][i];
                            vr = (d[i] - p) * (d[i] - p) + e[i] * e[i] - q * q;
                            vi = (d[i] - p) * 2.0 * q;
                            if (vr == 0.0 & vi == 0.0) {
                                vr = eps * norm * (Math.abs(w) + Math.abs(q) +
                                    Math.abs(x) + Math.abs(y) + Math.abs(z));
                            }
                            cdiv(x * r - z * ra + q * sa, x * s - z * sa - q * ra, vr, vi);
                            H[i][n - 1] = cdivr;
                            H[i][n] = cdivi;
                            if (Math.abs(x) > (Math.abs(z) + Math.abs(q))) {
                                H[i + 1][n - 1] = (-ra - w * H[i][n - 1] + q * H[i][n]) / x;
                                H[i + 1][n] = (-sa - w * H[i][n] - q * H[i][n - 1]) / x;
                            } else {
                                cdiv(-r - y * H[i][n - 1], -s - y * H[i][n], z, q);
                                H[i + 1][n - 1] = cdivr;
                                H[i + 1][n] = cdivi;
                            }
                        }

                        // Overflow control

                        t = Math.max(Math.abs(H[i][n - 1]), Math.abs(H[i][n]));
                        if ((eps * t) * t > 1) {
                            for (int j = i; j <= n; j++) {
                                H[j][n - 1] = H[j][n - 1] / t;
                                H[j][n] = H[j][n] / t;
                            }
                        }
                    }
                }
            }
        }

        // Vectors of isolated roots

        for (int i = 0; i < nn; i++) {
            if (i < low | i > high) {
                for (int j = i; j < nn; j++) {
                    V.set(i, j, H[i][j]);
                }
            }
        }

        // Back transformation to get eigenvectors of original matrix

        for (int j = nn - 1; j >= low; j--) {
            for (int i = low; i <= high; i++) {
                z = 0.0;
                for (int k = low; k <= Math.min(j, high); k++) {
                    z = Math.fma(V.get(i, k), H[k][j], z);
                }
                V.set(i, j, z);
            }
        }
    }

    private void cdiv(double xr, double xi, double yr, double yi) {
        double r, d;
        if (Math.abs(yr) > Math.abs(yi)) {
            r = yi/yr;
            d = Math.fma(r, yi, yr);
            //cdivr = (xr + r * xi) / d;
            cdivr = Math.fma(r, xi, xr)/d;
            //cdivi = (xi - r * xr) / d;
            cdivi = -Math.fma(r, xr, -xi)/d;
        } else {
            r = yr/yi;
            d = Math.fma(r, yr, yi);
            //cdivr = (r * xr + xi) / d;
            cdivr = Math.fma(r, xr, xi)/d;
            //cdivi = (r * xi - xr) / d;
            cdivi = Math.fma(r, xi, -xr)/d;
        }
    }

    /**
     * Return the block diagonal eigenvalue matrix, {@code D}.
     *
     * @return {@code D}
     */
    public DoubleMatrix2d D() {
        final var D = DoubleMatrix2d.DENSE.create(n, n);

        for (int i = 0; i < n; ++i) {
            D.set(i, i, d[i]);
            if (e[i] > 0) {
                D.set(i, i + 1, e[i]);
            } else if (e[i] < 0) {
                D.set(i, i - 1, e[i]);
            }
        }

        return D;
    }

    /**
     * Return the eigenvector matrix, {@code V}.
     *
     * @return {@code V}
     */
    public DoubleMatrix2d V() {
        return V.copy();
    }

    /**
     * Return the imaginary parts of the eigenvalues.
     *
     * @return the imaginary parts of the eigenvalues
     */
    public DoubleMatrix1d imagEigenvalues() {
        final var result = DoubleMatrix1d.DENSE.create(e.length);
        result.assign(e);
        return result;
    }

    /**
     * Return the real parts of the eigenvalues.
     *
     * @return the real parts of the eigenvalues
     */
    public DoubleMatrix1d realEigenvalues() {
        final var result = DoubleMatrix1d.DENSE.create(d.length);
        result.assign(d);
        return result;
    }

    /**
     * Performs an <em>Eigenvalue</em>-decomposition of the given matrix
     * {@code A}.
     *
     * @param A the square matrix to be decomposed
     * @return the <em>Eigenvalue</em>-decomposition of the given matrix {@code A}
     * @throws IllegalArgumentException if {@code A} is not square
     */
    public static Eigenvalue decompose(DoubleMatrix2d A) {
        final var eigen = new Eigenvalue();
        eigen.init(A);
        return eigen;
    }

}
