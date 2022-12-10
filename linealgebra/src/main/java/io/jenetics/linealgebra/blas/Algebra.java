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

import java.util.function.Function;

import io.jenetics.linealgebra.Tolerance;
import io.jenetics.linealgebra.matrix.DoubleMatrix2d;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
public final class Algebra {

    private Algebra() {
    }

    /**
     * Solves A*X = B.
     *
     * @return X; a new independent matrix; solution if A is square, least
     * squares solution otherwise.
     */
    public static DoubleMatrix2d solve(DoubleMatrix2d A, DoubleMatrix2d B) {
        return A.rows() == A.cols()
            ? lu(A).apply(B)
            : qr(A).apply(B);
    }

    private static Function<DoubleMatrix2d, DoubleMatrix2d>
    lu(final DoubleMatrix2d matrix) {
        final var A = matrix.copy();
        final var lu = LU.decompose(A);

        return B -> {
            final var BC = B.copy();
            lu.solve(BC);
            return BC;
        };
    }

    private static Function<DoubleMatrix2d, DoubleMatrix2d>
    qr(final DoubleMatrix2d matrix) {
        final var A = matrix.copy();
        final var qr = QR.decompose(A);

        return B -> {
            final var BC = B.copy();
            qr.solve(BC);
            return BC;
        };
    }

    public static DoubleMatrix2d inverse(final DoubleMatrix2d A) {
        return null;
    }

    /**
     * Check if the given {@code matrix} is non-singular.
     *
     * @param matrix the {@code matrix} to test
     * @return {@code true} if the given {@code matrix} is non-singular,
     *         {@code false} otherwise
     */
    static boolean isNonSingular(final DoubleMatrix2d matrix) {
        final var epsilon = Tolerance.epsilon();

        for (int j = Math.min(matrix.rows(), matrix.cols()); --j >= 0;) {
            if (Math.abs(matrix.get(j, j)) <= epsilon) {
                return false;
            }
        }

        return true;
    }

}
