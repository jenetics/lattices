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

import io.jenetics.linealgebra.matrix.DoubleMatrix1d;
import io.jenetics.linealgebra.matrix.DoubleMatrix2d;

public interface Blas {

    /**
     * Returns the sum of absolute values; {@code |x[0]| + |x[1]| + ... }. In
     * fact equivalent to
     * {@code x.aggregate(cern.jet.math.Functions.plus,
     * cern.jet.math.Functions.abs)}.
     *
     * @param x the first vector.
     */
    double dasum(final DoubleMatrix1d x);

    /**
     * Combined vector scaling; {@code y = y + alpha*x}. In fact equivalent to
     * {@code y.assign(x,cern.jet.math.Functions.plusMult(alpha))}.
     *
     * @param alpha a scale factor.
     * @param x the first source vector.
     * @param y the second source vector, this is also the vector where results
     * are stored.
     * @throws IllegalArgumentException {@code x.size() != y.size()}..
     */
    void daxpy(double alpha, DoubleMatrix1d x, DoubleMatrix1d y);

    /**
     * Combined matrix scaling; {@code B = B + alpha*A}. In fact equivalent to
     * {@code B.assign(A,cern.jet.math.Functions.plusMult(alpha))}.
     *
     * @param alpha a scale factor.
     * @param A the first source matrix.
     * @param B the second source matrix, this is also the matrix where results
     * are stored.
     * @throws IllegalArgumentException if
     * {@code A.columns() != B.columns() || A.rows() != B.rows()}.
     */
    void daxpy(double alpha, DoubleMatrix2d A, DoubleMatrix2d B);

    /**
     * Vector assignment (copying); {@code y = x}. In fact equivalent to
     * {@code y.assign(x)}.
     *
     * @param x the source vector.
     * @param y the destination vector.
     * @throws IllegalArgumentException {@code x.size() != y.size()}.
     */
    void dcopy(DoubleMatrix1d x, DoubleMatrix1d y);

    /**
     * Matrix assignment (copying); {@code B = A}. In fact equivalent to
     * {@code B.assign(A)}.
     *
     * @param A the source matrix.
     * @param B the destination matrix.
     * @throws IllegalArgumentException if
     * {@code A.columns() != B.columns() || A.rows() != B.rows()}.
     */
    void dcopy(DoubleMatrix2d A, DoubleMatrix2d B);

    /**
     * Returns the dot product of two vectors x and y, which is
     * {@code Sum(x[i]*y[i])}. In fact equivalent to {@code x.zDotProduct(y)}.
     *
     * @param x the first vector.
     * @param y the second vector.
     * @return the sum of products.
     * @throws IllegalArgumentException if {@code x.size() != y.size()}.
     */
    double ddot(DoubleMatrix1d x, DoubleMatrix1d y);

    /**
     * Generalized linear algebraic matrix-matrix multiply;
     * {@code C = alpha*A*B + beta*C}. In fact equivalent to
     * {@code A.zMult(B,C,alpha,beta,transposeA,transposeB)}. Note: Matrix shape
     * conformance is checked <i>after</i> potential transpositions.
     *
     * @param transposeA set this flag to indicate that the multiplication shall
     * be performed on A'.
     * @param transposeB set this flag to indicate that the multiplication shall
     * be performed on B'.
     * @param alpha a scale factor.
     * @param A the first source matrix.
     * @param B the second source matrix.
     * @param beta a scale factor.
     * @param C the third source matrix, this is also the matrix where results
     * are stored.
     * @throws IllegalArgumentException if {@code B.rows() != A.columns()}.
     * @throws IllegalArgumentException if
     * {@code C.rows() != A.rows() || C.columns() != B.columns()}.
     * @throws IllegalArgumentException if {@code A == C || B == C}.
     */
    void dgemm(boolean transposeA, boolean transposeB, double alpha, DoubleMatrix2d A, DoubleMatrix2d B, double beta, DoubleMatrix2d C);

    /**
     * Generalized linear algebraic matrix-vector multiply;
     * {@code y = alpha*A*x + beta*y}. In fact equivalent to
     * {@code A.zMult(x,y,alpha,beta,transposeA)}. Note: Matrix shape
     * conformance is checked <i>after</i> potential transpositions.
     *
     * @param transposeA set this flag to indicate that the multiplication shall
     * be performed on A'.
     * @param alpha a scale factor.
     * @param A the source matrix.
     * @param x the first source vector.
     * @param beta a scale factor.
     * @param y the second source vector, this is also the vector where results
     * are stored.
     * @throws IllegalArgumentException
     * {@code A.columns() != x.size() || A.rows() != y.size())}..
     */
    void dgemv(boolean transposeA, double alpha, DoubleMatrix2d A, DoubleMatrix1d x, double beta, DoubleMatrix1d y);

    /**
     * Performs a rank 1 update; {@code A = A + alpha*x*y'}. Example:
     * <pre>
     * A = { {6,5}, {7,6} }, x = {1,2}, y = {3,4}, alpha = 1 -->
     * A = { {9,9}, {13,14} }
     * </pre>
     *
     * @param alpha a scalar.
     * @param x an m element vector.
     * @param y an n element vector.
     * @param A an m by n matrix.
     */
    void dger(double alpha, DoubleMatrix1d x, DoubleMatrix1d y, DoubleMatrix2d A);

    /**
     * Return the 2-norm; {@code sqrt(x[0]^2 + x[1]^2 + ...)}. In fact
     * equivalent to {@code Math.sqrt(Algebra.DEFAULT.norm2(x))}.
     *
     * @param x the vector.
     */
    double dnrm2(DoubleMatrix1d x);

    /**
     * Applies a givens plane rotation to (x,y);
     * {@code x = c*x + s*y; y = c*y - s*x}.
     *
     * @param x the first vector.
     * @param y the second vector.
     * @param c the cosine of the angle of rotation.
     * @param s the sine of the angle of rotation.
     */
    void drot(DoubleMatrix1d x, DoubleMatrix1d y, double c, double s);

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
    void drotg(double a, double b, double[] rotvec);

    /**
     * Vector scaling; {@code x = alpha*x}. In fact equivalent to
     * {@code x.assign(cern.jet.math.Functions.mult(alpha))}.
     *
     * @param alpha a scale factor.
     * @param x the first vector.
     */
    void dscal(double alpha, DoubleMatrix1d x);

    /**
     * Matrix scaling; {@code A = alpha*A}. In fact equivalent to
     * {@code A.assign(cern.jet.math.Functions.mult(alpha))}.
     *
     * @param alpha a scale factor.
     * @param A the matrix.
     */
    void dscal(double alpha, DoubleMatrix2d A);

    /**
     * Swaps the elements of two vectors; {@code y <==> x}. In fact equivalent
     * to {@code y.swap(x)}.
     *
     * @param x the first vector.
     * @param y the second vector.
     * @throws IllegalArgumentException {@code x.size() != y.size()}.
     */
    void dswap(DoubleMatrix1d x, DoubleMatrix1d y);

    /**
     * Swaps the elements of two matrices; {@code B <==> A}.
     *
     * @param A the first matrix.
     * @param B the second matrix.
     * @throws IllegalArgumentException if
     * {@code A.columns() != B.columns() || A.rows() != B.rows()}.
     */
    void dswap(DoubleMatrix2d A, DoubleMatrix2d B);

    /**
     * Symmetric matrix-vector multiplication; {@code y = alpha*A*x + beta*y}.
     * Where alpha and beta are scalars, x and y are n element vectors and A is
     * an n by n symmetric matrix. A can be in upper or lower triangular
     * format.
     *
     * @param isUpperTriangular is A upper triangular or lower triangular part
     * to be used?
     * @param alpha scaling factor.
     * @param A the source matrix.
     * @param x the first source vector.
     * @param beta scaling factor.
     * @param y the second vector holding source and destination.
     */
    void dsymv(boolean isUpperTriangular, double alpha, DoubleMatrix2d A, DoubleMatrix1d x, double beta, DoubleMatrix1d y);

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
    void dtrmv(boolean isUpperTriangular, boolean transposeA, boolean isUnitTriangular, DoubleMatrix2d A, DoubleMatrix1d x);

    /**
     * Returns the index of largest absolute value;
     * {@code i such that |x[i]| == max(|x[0]|,|x[1]|,...).}.
     *
     * @param x the vector to search through.
     * @return the index of largest absolute value (-1 if x is empty).
     */
    int idamax(DoubleMatrix1d x);

}
