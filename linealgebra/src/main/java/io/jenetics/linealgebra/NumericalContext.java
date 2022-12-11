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
package io.jenetics.linealgebra;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
public class NumericalContext {

    public double epsilon() {
        return 0;
    }

    /**
     * Checks if the given two {@code double} values are equal, obeying the
     * defined {@link #epsilon()}.
     *
     * @param a the first value to compare
     * @param b the second value to compare
     * @return {@code true} if the given values are equal, modulo the given
     *         {@link #epsilon()}, {@code false} otherwise
     */
    public boolean equals(final double a, final double b) {
        return false;
    }

    public boolean isZero(final double a) {
        return equals(a, 0);
    }

    public boolean isOne(final double a) {
        return equals(a, 1);
    }

}
