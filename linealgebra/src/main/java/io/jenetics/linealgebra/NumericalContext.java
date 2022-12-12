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
 * Encapsulates the context settings which describes certain rules for numerical
 * operations.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
public interface NumericalContext {

    /**
     * Numerical context with an {@link #epsilon()} of zero.
     */
    NumericalContext ZERO = new NumericalContext() {
        @Override
        public double epsilon() {
            return 0.0;
        }
        @Override
        public boolean equals(final double a, final double b) {
            return Double.compare(a, b) == 0;
        }
    };

    default double epsilon() {
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
    default boolean equals(final double a, final double b) {
        return Double.compare(a, b) == 0 || Math.abs(a - b) <= epsilon();
    }

    /**
     * Tests whether the given double value is zero, according to the defined
     * {@link #epsilon()}.
     *
     * @param a the value to test
     * @return {@code true} if the given value is (near) zero, {@code false}
     *         otherwise
     */
    default boolean isZero(final double a) {
        return equals(a, 0);
    }

    /**
     * Tests whether the given double value is one, according to the defined
     * {@link #epsilon()}.
     *
     * @param a the value to test
     * @return {@code true} if the given value is (near) one, {@code false}
     *         otherwise
     */
    default boolean isOne(final double a) {
        return equals(a, 1);
    }

    /**
     * Return the default numerical context.
     *
     * @return the default numerical context
     */
    static NumericalContext instance() {
        record NC(double epsilon) implements NumericalContext {}
        return new NC(0.0000000000001);
    }

}
