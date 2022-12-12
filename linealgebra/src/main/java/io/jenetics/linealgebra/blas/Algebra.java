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

import io.jenetics.linealgebra.NumericalContext;
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
     * Solves {@code A*X = B}.
     *
     * @return {@code X}, new independent matrix of the solution if A is square,
     *         least squares solution otherwise.
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
     * @return a new independent matrix. inverse(matrix) if the matrix is
     *         square, pseudo-inverse otherwise.
     */
    public static DoubleMatrix2d inverse(final DoubleMatrix2d A) {
        return null;
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

}
