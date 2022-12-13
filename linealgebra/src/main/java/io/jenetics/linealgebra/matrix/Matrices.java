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
package io.jenetics.linealgebra.matrix;

import io.jenetics.linealgebra.NumericalContext;

/**
 * Some helper methods for checking pre-conditions.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
public final class Matrices {

    private Matrices() {
    }

    /**
     * A matrix {@code A} is <em>square</em> if it has the same number of rows
     * and columns.
     *
     * @param A the matrix to check
     * @return {@code true} if the {@code A} is square, {@code false} otherwise
     */
    public static boolean isSquare(final DoubleMatrix2d A) {
        return A.rows() == A.cols();
    }

    /**
     * Checks whether the given matrix {@code A} is <em>square</em>.
     *
     * @param A the matrix to check
     * @throws IllegalArgumentException if {@code A.rows() != A.cols()}
     */
    public static void checkSquare(final DoubleMatrix2d A) {
        if (!isSquare(A)) {
            throw new IllegalArgumentException(
                "Matrix must be square: " + A.extent()
            );
        }
    }

    /**
     * Checks whether the given matrix {@code A} is <em>rectangular</em>.
     *
     * @param A the matrix to check
     * @throws IllegalArgumentException if {@code A.rows() < A.cols()}
     */
    public static void checkRectangular(final DoubleMatrix2d A) {
        if (A.rows() < A.cols()) {
            throw new IllegalArgumentException(
                "Matrix must be rectangular: " + A.extent()
            );
        }
    }

    /**
     * A matrix {@code A} is <em>diagonal</em> if {@code A[i, j] == 0} whenever
     * {@code i != j}. Matrix may but need not be square.
     *
     * @param A the matrix to test
     * @return {@code true} if the matrix {@code A} is <em>diagonal</em>,
     *         {@code false} otherwise
     */
    public static boolean isDiagonal(final DoubleMatrix2d A) {
        final var context = NumericalContext.instance();

        for (int r = A.rows(); --r >= 0; ) {
            for (int c = A.cols(); --c >= 0; ) {
                if (r != c && context.isNotZero(A.get(r, c))) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Check if the given matrix {@code A} is non-singular.
     *
     * @param A the {@code matrix} to test
     * @return {@code true} if the given matrix {@code A} is non-singular,
     *         {@code false} otherwise
     */
    public static boolean isNonSingular(final DoubleMatrix2d A) {
        final var context = NumericalContext.instance();

        for (int i = Math.min(A.rows(), A.cols()); --i >= 0;) {
            if (context.isZero(A.get(i, i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Check if the given matrix {@code A} is non-singular (not
     * {@link #isNonSingular(DoubleMatrix2d)}).
     *
     * @see #isNonSingular(DoubleMatrix2d)
     *
     * @param A the {@code matrix} to test
     * @return {@code true} if the given matrix {@code A} is non-singular,
     *         {@code false} otherwise
     */
    public static boolean isSingular(final DoubleMatrix2d A) {
        return !isNonSingular(A);
    }

    /**
     * Checks if the matrix {@code A} is <em>symmetric</em>. A matrix {@code A}
     * is <em>symmetric</em> if {@code A = transpose(A)}, that is
     * {@code A[i, j] == A[j, i]}.
     *
     * @throws IllegalArgumentException if <tt>!isSquare(A)</tt>.
     */
    public static boolean isSymmetric(final DoubleMatrix2d A) {
        checkSquare(A);
        return A.equals(A.transpose(), NumericalContext.instance());
    }

    /**
     * Checks whether the given matrix {@code A} is <em>square</em>.
     *
     * @param A the matrix to test
     * @throws IllegalArgumentException if {@code A.rows() != A.cols()}
     */
    public static void checkSymmetric(final DoubleMatrix2d A) {
        if (!isSymmetric(A)) {
            throw new IllegalArgumentException(
                "Matrix must be symmetric."
            );
        }
    }

}
