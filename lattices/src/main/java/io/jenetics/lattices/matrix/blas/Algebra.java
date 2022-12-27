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

import static io.jenetics.lattices.grid.Grids.checkSquare;
import static io.jenetics.lattices.grid.Grids.isSquare;
import static io.jenetics.lattices.matrix.Matrices.isDiagonal;

import io.jenetics.lattices.NumericalContext;
import io.jenetics.lattices.matrix.DoubleMatrix1d;
import io.jenetics.lattices.matrix.DoubleMatrix2d;
import io.jenetics.lattices.structure.Extent2d;

/**
 * Linear algebraic matrix operations.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
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
        return x.size() == 0 ? 0 : x.reduce(Double::sum, Math::abs);
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
     * Returns the two-norm (aka <em>euclidean norm</em>) of vector {@code x}.
     * This is equivalent to {@code mult(x,x)}.
     *
     * @param x the vector for which to calculate the two-norm
     * @return the two-norm of the given vector {@code x}
     */
    public static double norm2(final DoubleMatrix1d x) {
        return x.dotProduct(x);
    }

    /**
     * Returns the two-norm of matrix {@code A}. This is the maximum singular
     * value obtained from {@link SingularValue#decompose(DoubleMatrix2d)}.
     *
     * @param A the matrix from which to calculate the two-norm
     * @return the two-norm of the given matrix {@code A}
     */
    public static double norm2(final DoubleMatrix2d A) {
        return SingularValue.decompose(A).norm2();
    }

    /**
     * Returns the infinity-norm of vector {@code x}. This is defined as
     * {@code Max(abs(x[i]))}.
     *
     * @param x the vector for calculating the infinity-norm
     * @return the infinity-norm of the given vector
     */
    public static double normInf(final DoubleMatrix1d x) {
        if (x.size() == 0) {
            return 0;
        } else {
            return x.reduce(Math::max, Math::abs);
        }
    }

    /**
     * Returns the infinity-norm of matrix {@code A}. This is the maximum
     * absolute row sum.
     *
     * @param A the matrix from which to calculate the infinity-norm
     * @return the infinity-norm of the given matrix
     */
    public static double normInf(final DoubleMatrix2d A) {
        double max = 0;
        for (int row = A.rows(); --row >= 0;) {
            max = Math.max(max, norm1(A.rowAt(row)));
        }
        return max;
    }

    /**
     * Returns the sum of the diagonal elements of matrix {@code A}:
     * {@code Sum(A[i,i])}.
     *
     * @param A the matrix for which to calculate the trace
     * @return the trace of the matrix
     */
    public static double trace(final DoubleMatrix2d A) {
        double sum = 0;
        for (int i = Math.min(A.rows(), A.cols()); --i >= 0;) {
            sum += A.get(i, i);
        }
        return sum;
    }

    /**
     * Returns the determinant of matrix {@code A}.
     *
     * @param A the matrix for which to calculate the determinante
     * @return the determinant.
     * @throws IllegalArgumentException if the matrix {@code A} is not square
     */
    public static double det(final DoubleMatrix2d A) {
        return LU.decompose(A).det();
    }

    /**
     * Returns the condition of matrix {@code A}, which is the ratio of largest
     * to the smallest singular value.
     *
     * @param A the matrix for which to calculate the one-norm
     * @return the condition for the given matrix
     */
    public static double cond(final DoubleMatrix2d A) {
        return SingularValue.decompose(A).cond();
    }

    /**
     * Return the effective numerical matrix rank, which is the number of
     * non-negligible singular values.
     *
     * @param A the matrix for which to calculate the rank
     * @return the rank for the given matrix
     */
    public int rank(final DoubleMatrix2d A) {
        return SingularValue.decompose(A).rank();
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
     * @throws IllegalArgumentException if the given matrix is singular
     */
    public static DoubleMatrix2d inverse(final DoubleMatrix2d A) {
        if (isSquare(A) && isDiagonal(A)) {
            final var inv = A.copy();
            if (!diagonalInverse(inv)) {
                throw new IllegalArgumentException("Matrix to invert is singular.");
            }

            return inv;
        } else {
            final var identity = A.like(new Extent2d(A.rows(), A.rows()));
            for (int i = A.rows(); --i >= 0;) {
                identity.set(i, i, 1.0);
            }

            return solve(A, identity);
        }
    }

    private static boolean diagonalInverse(final DoubleMatrix2d A) {
        checkSquare(A);

        final NumericalContext context = NumericalContext.get();
        boolean nonSingular = true;
        for (int i = A.rows(); --i >= 0; ) {
            double v = A.get(i, i);
            nonSingular &= context.isNotZero(v);
            A.set(i, i, 1.0/v);
        }

        return nonSingular;
    }

}
