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

import static io.jenetics.linealgebra.matrix.Matrices.checkSquare;
import static io.jenetics.linealgebra.matrix.Matrices.isDiagonal;
import static io.jenetics.linealgebra.matrix.Matrices.isSquare;

import io.jenetics.linealgebra.NumericalContext;
import io.jenetics.linealgebra.matrix.DoubleMatrix1d;
import io.jenetics.linealgebra.matrix.DoubleMatrix2d;

/**
 * Linear algebraic matrix operations.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
public final class Algebra {

    private Algebra() {
    }

    /**
     * Calculate the one-norm of vector {@code x}, which is defined as
     * {@code Sum(abs(x[i]))}
     *
     * @param x the vector for which to calculate the one-norm
     * @return the one-norm of {@code x}
     */
    public static double norm1(final DoubleMatrix1d x) {
        if (x.size() == 0) {
            return 0;
        } else {
            return x.reduce(Double::sum, Math::abs);
        }
    }

    /**
     * Calculate the one-norm of matrix {@code A}, which is defined as the
     * maximum absolute column sum.
     *
     * @param A the matrix for which to calculate the one-norm
     * @return the one-norm of {@code A}
     */
    public static double norm1(final DoubleMatrix2d A) {
        double max = 0;
        for (int c = A.cols(); --c >= 0;) {
            max = Math.max(max, norm1(A.colAt(c)));
        }
        return max;
    }

    /**
     * Returns the sum of the diagonal elements of matrix {@code A}:
     * {@code Sum(A[i,i])}.
     */
    public static double trace(final DoubleMatrix2d A) {
        double sum = 0;
        for (int i = Math.min(A.rows(), A.cols()); --i >= 0; ) {
            sum += A.get(i, i);
        }
        return sum;
    }

    /**
     * Returns the determinant of matrix {@code A}.
     *
     * @return the determinant.
     * @throws IllegalArgumentException if the matrix {@code A} is not square
     */
    public static double det(final DoubleMatrix2d A) {
        return LU.decompose(A).det();
    }

    /**
     * Returns the condition of matrix <tt>A</tt>, which is the ratio of largest
     * to the smallest singular value.
     */
    public static double cond(final DoubleMatrix2d A) {
        return SingularValue.decompose(A).cond();
    }

    /**
     * Solves {@code A*X = B}.
     *
     * @param A the matrix {@code A}
     * @param B the matrix {@code B}
     * @return {@code X}, new independent matrix of the solution if {@code A} is
     *         square, least squares solution otherwise.
     */
    public static DoubleMatrix2d solve(
        final DoubleMatrix2d A,
        final DoubleMatrix2d B
    ) {
        return A.rows() == A.cols()
            ? LU.decompose(A).solve(B)
            : QR.decompose(A).solve(B);
    }

    /**
     * Returns the inverse or pseudo-inverse of matrix {@code A}.
     *
     * @param A the matrix to invert
     * @return a new independent matrix. inverse(matrix) if the matrix is
     *         square, pseudo-inverse otherwise.
     */
    public static DoubleMatrix2d inverse(final DoubleMatrix2d A) {
        if (isSquare(A) && isDiagonal(A)) {
            final var inv = A.copy();
            if (!diagonalInverse(inv)) {
                throw new IllegalArgumentException("A is singular.");
            }

            return inv;
        } else {
            final var identity = A.like(A.rows(), A.rows());
            for (int i = A.rows(); --i >= 0;) {
                identity.set(i, i, 1.0);
            }

            return solve(A, identity);
        }
    }

    private static boolean diagonalInverse(final DoubleMatrix2d A) {
        checkSquare(A);

        final NumericalContext context = NumericalContext.instance();
        boolean nonSingular = true;
        for (int i = A.rows(); --i >= 0; ) {
            double v = A.get(i, i);
            nonSingular &= context.isNotZero(v);
            A.set(i, i, 1.0/v);
        }

        return nonSingular;
    }

}
