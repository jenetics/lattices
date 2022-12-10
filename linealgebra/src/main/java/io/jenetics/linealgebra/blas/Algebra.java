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

import io.jenetics.linealgebra.Tolerance;
import io.jenetics.linealgebra.matrix.DoubleMatrix2d;
import io.jenetics.linealgebra.structure.Structural2d;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
public final class Algebra {

    private Algebra() {
    }

    /**
     * Checks whether the given matrix {@code a} is <em>rectangular</em>.
     *
     * @param a the structural to test
     * @throws IllegalArgumentException if {@code a.rows() < a.cols()}.
     */
    public static void checkRectangular(final Structural2d a) {
        if (a.rows() < a.cols()) {
            throw new IllegalArgumentException(
                "Matrix must be rectangular: " + a.extent()
            );
        }
    }

    /**
     * Check if the given {@code matrix} is non-singular.
     *
     * @param matrix the {@code matrix} to test
     * @return {@code true} if the given {@code matrix} is non-singular,
     *         {@code false} otherwise
     */
    public static boolean isNonSingular(final DoubleMatrix2d matrix) {
        final var epsilon = Tolerance.epsilon();

        for (int j = Math.min(matrix.rows(), matrix.cols()); --j >= 0;) {
            if (Math.abs(matrix.get(j, j)) <= epsilon) {
                return false;
            }
        }

        return true;
    }

}
