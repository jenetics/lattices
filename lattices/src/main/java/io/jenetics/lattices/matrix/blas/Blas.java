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

import io.jenetics.lattices.NumericalContext;
import io.jenetics.lattices.grid.Grids;
import io.jenetics.lattices.matrix.DoubleMatrix1d;
import io.jenetics.lattices.matrix.DoubleMatrix2d;

/**
 * Subset of the <a href="https://math.nist.gov/javanumerics/blas.html">BLAS</a>
 * (Basic Linear Algebra System). High quality "building block" routines for
 * performing basic vector and matrix operations. Because the BLAS are efficient,
 * portable, and widely available, they're commonly used in the development of
 * high quality linear algebra software.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
public interface Blas {

    /**
     * Default <em>sequential</em> implementation of the BLAS methods.
     */
    Blas DEFAULT = new Blas() {};

    /* *************************************************************************
     * Level 1
     * ************************************************************************/

    /**
     * Vector assignment (copying).
     * <pre>{@code
     * y[i] = x[i]
     * }</pre>
     *
     * @param x the source vector
     * @param y the destination vector
     * @throws IllegalArgumentException if {@code x.size() != y.size()}.
     */
    default void dcopy(final DoubleMatrix1d x, final DoubleMatrix1d y) {
        y.assign(x);
    }

    /**
     * Constructs a Givens plane rotation for {@code (a,b)}. Taken from the
     * LINPACK translation from FORTRAN to Java, interface slightly modified. In
     * the LINPACK listing DROTG is attributed to Jack Dongarra
     *
     * @param a rotational elimination parameter a.
     * @param b rotational elimination parameter b.
     * @param rotvec Must be at least of length 4. On output contains the values
     * {@code {a,b,c,s}}.
     */
    default void drotg(double a, double b, double[] rotvec) {
        double c, s, roe, scale, r, z, ra, rb;

        roe = b;

        if (Math.abs(a) > Math.abs(b)) {
            roe = a;
        }

        scale = Math.abs(a) + Math.abs(b);

        final var context = NumericalContext.get();
        if (context.isNotZero(scale)) {
            ra = a/scale;
            rb = b/scale;
            r = scale*Math.hypot(ra, rb);
            r = sign(1.0, roe) * r;
            c = a/r;
            s = b/r;
            z = 1.0;
            if (Math.abs(a) > Math.abs(b)) {
                z = s;
            }
            if ((Math.abs(b) >= Math.abs(a)) && (context.isNotZero(c))) {
                z = 1.0/c;
            }
        } else {
            c = 1.0;
            s = 0.0;
            r = 0.0;
            z = 0.0;
        }

        a = r;
        b = z;

        rotvec[0] = a;
        rotvec[1] = b;
        rotvec[2] = c;
        rotvec[3] = s;
    }

    private static double sign(double a, double b) {
        if (b < 0.0) {
            return -Math.abs(a);
        } else {
            return Math.abs(a);
        }
    }

    /**
     * Applies a givens plane rotation to (x, y);
     * <pre>{@code
     * x = c*x + s*y
     * y = c*y - s*x
     * }</pre>
     *
     * @param x the first vector.
     * @param y the second vector.
     * @param c the cosine of the angle of rotation.
     * @param s the sine of the angle of rotation.
     */
    default void drot(
        final DoubleMatrix1d x,
        final DoubleMatrix1d y,
        final double c,
        final double s
    ) {
        Grids.checkSameExtent(x, y);

        final var tmp = x.copy();

        x.assign(a -> c*a);
        x.assign(y, (a, b) -> Math.fma(b, s, a));

        y.assign(a -> c*a);
        y.assign(tmp, (a, b) -> -Math.fma(b, s, -a));
    }

    /**
     * Vector scaling.
     * <pre>{@code
     * x[i] = alpha*x[i]
     * }</pre>
     *
     * @param alpha a scale factor
     * @param x the first vector
     */
    default void dscal(final double alpha, final DoubleMatrix1d x) {
        x.assign(a -> a*alpha);
    }

    /**
     * Swaps the elements of two vectors, {@code y <==> x}.
     *
     * @param x the first vector
     * @param y the second vector
     * @throws IllegalArgumentException {@code x.size() != y.size()}.
     */
    default void dswap(final DoubleMatrix1d x, final DoubleMatrix1d y) {
        y.swap(x);
    }

    /**
     * Combined vector scaling.
     * <pre>{@code
     * y = y + alpha*x
     * }</pre>
     *
     * @param alpha a scale factor
     * @param x the first source vector
     * @param y the second source vector, this is also the vector where results
     *        are stored
     * @throws IllegalArgumentException if {@code x.size() != y.size()}
     */
    default void daxpy(final double alpha, final DoubleMatrix1d x, final DoubleMatrix1d y) {
        y.assign(x, (a, b) -> Math.fma(b, alpha, a));
    }

    /**
     * Returns the dot product of two vectors x and y, which is
     * <pre>{@code
     * Sum(x[i]*y[i])
     * }</pre>
     *
     * @param x the first vector
     * @param y the second vector
     * @return the sum of products
     * @throws IllegalArgumentException if {@code x.size() != y.size()}
     */
    default double ddot(DoubleMatrix1d x, DoubleMatrix1d y) {
        return x.dotProduct(y);
    }

    /**
     * Return the 2-norm.
     * <pre>{@code
     * sqrt(x[0]^2 + x[1]^2 + ...)
     * }</pre>
     *
     * @param x the vector.
     */
    default double dnrm2(final DoubleMatrix1d x) {
        return Math.sqrt(x.dotProduct(x));
    }

    /**
     * Returns the sum of absolute values of the input vector {@code x}.
     * <pre>{@code
     * |x[0]| + |x[1]| + ...
     * }</pre>
     *
     * @param x the input vector
     */
    default double dasum(final DoubleMatrix1d x) {
        return x.reduce(Double::sum, Math::abs);
    }

    /**
     * Returns the index of the largest absolute value.
     * <pre>{@code
     * i such that |x[i]| == max(|x[0]|,|x[1]|,...)
     * }</pre>
     *
     * @param x the vector to search through.
     * @return the index of the largest absolute value (-1 if x is empty).
     */
    default int idamax(final DoubleMatrix1d x) {
        int index = -1;
        if (x.size() > 0) {
            double max = Math.abs(x.get(0));
            index = 0;
            for (int i = 1; i < x.size(); ++i) {
                final var value = Math.abs(x.get(i));

                if (max < value) {
                    max = value;
                    index = i;
                }
            }
        }

        return index;
    }

    /* *************************************************************************
     * Level 2
     * ************************************************************************/

    /**
     * Generalized linear algebraic matrix-vector multiply;
     * {@code y = alpha*A*x + beta*y}. In fact equivalent to
     * {@code A.mult(x,y,alpha,beta,transposeA)}. Note: Matrix shape
     * conformance is checked <i>after</i> potential transpositions.
     *
     * @param transposeA set this flag to indicate that the multiplication shall
     *        be performed on {@code A}
     * @param alpha a scale factor.
     * @param A the source matrix.
     * @param x the first source vector.
     * @param beta a scale factor.
     * @param y the second source vector, this is also the vector where results
     * are stored.
     * @throws IllegalArgumentException if
     *         {@code A.columns() != x.size() || A.rows() != y.size())}
     */
    default void dgemv(
        final boolean transposeA,
        final double alpha,
        final DoubleMatrix2d A,
        final DoubleMatrix1d x,
        final double beta,
        final DoubleMatrix1d y
    ) {
        A.mult(x, y, alpha, beta, transposeA);
    }

    /**
     * Performs a rank 1 update.
     * <pre>{@code
     * A = A + alpha*x*y'
     * }</pre>
     * Example:
     * <pre>
     * A = { {6,5}, {7,6} }, x = {1,2}, y = {3,4}, alpha = 1 -->
     * A = { {9,9}, {13,14} }
     * </pre>
     *
     * @param alpha a scalar
     * @param x an m element vector
     * @param y an n element vector
     * @param A an m by n matrix
     */
    default void dger(
        final double alpha,
        final DoubleMatrix1d x,
        final DoubleMatrix1d y,
        final DoubleMatrix2d A
    ) {
        for (int i = 0; i < A.rows(); ++i) {
            final var multiplier = alpha*x.get(i);
            A.rowAt(i).assign(y, (a, b) -> Math.fma(b, multiplier, a));
        }
    }

    /**
     * Symmetric matrix-vector multiplication.
     * <pre>{@code
     * y = alpha*A*x + beta*y
     * }</pre>
     * Where alpha and beta are scalars, x and y are n element vectors and A is
     * an n by n symmetric matrix. A can be in upper or lower triangular
     * format.
     *
     * @param isUpperTriangular is A upper triangular or lower triangular part
     *        to be used
     * @param alpha scaling factor
     * @param A the source matrix
     * @param x the first source vector
     * @param beta scaling factor
     * @param y the second vector holding source and destination
     */
    default void dsymv(
        final boolean isUpperTriangular,
        final double alpha,
        DoubleMatrix2d A,
        final DoubleMatrix1d x,
        final double beta,
        final DoubleMatrix1d y
    ) {
        Grids.checkSquare(A);

        if (isUpperTriangular) {
            A = A.transpose();
        }

        if (A.rows() != x.size() || A.rows() != y.size()) {
            throw new IllegalArgumentException(
                A.extent() + ", " + x.extent() + ", " + y.extent()
            );
        }

        final DoubleMatrix1d tmp = x.like();
        for (int i = 0; i < A.rows(); i++) {
            double sum = 0;
            for (int j = 0; j <= i; j++) {
                sum = Math.fma(A.get(i, j), x.get(j), sum);
            }
            for (int j = i + 1; j < A.rows(); j++) {
                sum = Math.fma(A.get(j, i), x.get(j), sum);
            }
            tmp.set(i, alpha * sum + beta * y.get(i));
        }

        y.assign(tmp);
    }

    /**
     * Triangular matrix-vector multiplication; {@code x = A*x} or
     * {@code x = A'*x}. Where x is an n element vector and A is an n by n unit,
     * or non-unit, upper or lower triangular matrix.
     *
     * @param isUpperTriangular is A upper triangular or lower triangular?
     * @param transposeA set this flag to indicate that the multiplication shall
     * be performed on A'.
     * @param isUnitTriangular true --> A is assumed to be unit triangular;
     * false --> A is not assumed to be unit triangular
     * @param A the source matrix.
     * @param x the vector holding source and destination.
     */
    default void dtrmv(
        boolean isUpperTriangular,
        final boolean transposeA,
        final boolean isUnitTriangular,
        DoubleMatrix2d A,
        final DoubleMatrix1d x
    ) {
        Grids.checkSquare(A);

        if (transposeA) {
            A = A.transpose();
            isUpperTriangular = !isUpperTriangular;
        }

        if (A.rows() != x.size()) {
            throw new IllegalArgumentException(A.extent() + ", " + x.extent());
        }

        final DoubleMatrix1d b = x.like();
        final DoubleMatrix1d y = x.like();
        if (isUnitTriangular) {
            y.assign(1);
        } else {
            for (int i = 0; i < A.rows(); i++) {
                y.set(i, A.get(i, i));
            }
        }

        for (int i = 0; i < A.rows(); i++) {
            double sum = 0;
            if (!isUpperTriangular) {
                for (int j = 0; j < i; j++) {
                    sum = Math.fma(A.get(i, j), x.get(j), sum);
                }
                sum = Math.fma(y.get(i), x.get(i), sum);
            } else {
                sum = Math.fma(y.get(i), x.get(i), sum);
                for (int j = i + 1; j < A.rows(); j++) {
                    sum = Math.fma(A.get(i, j), x.get(j), sum);
                }
            }
            b.set(i, sum);
        }
        x.assign(b);
    }

    /* *************************************************************************
     * Level 3
     * ************************************************************************/

    /**
     * Matrix assignment (copying).
     * <pre>{@code
     * B[i, j] = A[i, j]
     * }</pre>
     *
     * @param A the source matrix
     * @param B the destination matrix
     * @throws IllegalArgumentException if {@code A.columns() != B.columns() ||
     *         A.rows() != B.rows()}
     */
    default void dcopy(final DoubleMatrix2d A, final DoubleMatrix2d B) {
        B.assign(A);
    }

    /**
     * Matrix scaling.
     * <pre>{
     *     A = alpha*A
     * }</pre>
     *
     * @param alpha a scale factor
     * @param A the matrix
     */
    default void dscal(final double alpha, final DoubleMatrix2d A) {
        A.assign(a -> a*alpha);
    }

    /**
     * Generalized linear algebraic matrix-matrix multiply.
     * <pre>{@code
     * C = alpha*A*B + beta*C
     * }</pre>
     *
     * @param transposeA set this flag to indicate that the multiplication shall
     *         be performed on A.
     * @param transposeB set this flag to indicate that the multiplication shall
     *        be performed on B.
     * @param alpha a scale factor
     * @param A the first source matrix
     * @param B the second source matrix
     * @param beta a scale factor
     * @param C the third source matrix, this is also the matrix where results
     *        are stored.
     * @throws IllegalArgumentException if {@code B.rows() != A.columns()} or
     *         {@code C.rows() != A.rows() || C.columns() != B.columns()} or
     *         {@code A == C || B == C}
     */
    default void dgemm(
        final boolean transposeA,
        final boolean transposeB,
        final double alpha,
        final DoubleMatrix2d A,
        final DoubleMatrix2d B,
        final double beta,
        final DoubleMatrix2d C
    ) {
        A.mult(B, C, alpha, beta, transposeA, transposeB);
    }

    /**
     * Combined matrix scaling.
     * <pre>{@code
     * B = B + alpha*A
     * }</pre>
     *
     * @param alpha a scale factor.
     * @param A the first source matrix.
     * @param B the second source matrix, this is also the matrix where results
     *          are stored.
     * @throws IllegalArgumentException if {@code A.columns() != B.columns() ||
     *         A.rows() != B.rows()}
     */
    default void daxpy(final double alpha, final DoubleMatrix2d A, final DoubleMatrix2d B) {
        B.assign(A, (a, b) -> Math.fma(alpha, b, a));
    }

    /**
     * Swaps the elements of two matrices: {@code B <==> A}.
     *
     * @param A the first matrix.
     * @param B the second matrix.
     * @throws IllegalArgumentException if
     *         {@code A.columns() != B.columns() || A.rows() != B.rows()}.
     */
    default void dswap(DoubleMatrix2d A, DoubleMatrix2d B) {
        A.swap(B);
    }

}
