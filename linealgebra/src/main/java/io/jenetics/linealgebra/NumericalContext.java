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

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Encapsulates the context settings which describes certain rules for numerical
 * operations.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
public class NumericalContext {

    /**
     * Numerical context with an {@link #epsilon()} of zero.
     */
    public static final NumericalContext ZERO = new NumericalContext(0.0) {
        @Override
        public boolean equals(final double a, final double b) {
            return Double.compare(a, b) == 0;
        }
    };

    /**
     * The default numerical context.
     */
    public static final NumericalContext DEFAULT_CONTEXT =
        new NumericalContext(Math.pow(10, -Env.precission));

    private static final AtomicReference<NumericalContext> CONTEXT =
        new AtomicReference<>(DEFAULT_CONTEXT);

    private final double epsilon;

    private NumericalContext(final double epsilon) {
        this.epsilon = abs(epsilon);
    }

    public double epsilon() {
        return epsilon;
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
        return Double.compare(a, b) == 0 || abs(a - b) <= epsilon();
    }

    /**
     * Tests whether the given value {@code a} is greater than zero.
     *
     * @param a the value to test
     * @return {@code true} if the given value is greater than zero, {@code false}
     *         otherwise
     */
    public boolean isGreaterZero(final double a) {
        return abs(a) > epsilon() && Double.compare(a, 0.0) > 0;
    }

    /**
     * Tests whether the given value {@code a} is smaller than zero.
     *
     * @param a the value to test
     * @return {@code true} if the given value is smaller than zero, {@code false}
     *         otherwise
     */
    public boolean isSmallerZero(final double a) {
        return abs(a) > epsilon() && Double.compare(a, 0.0) < 0;
    }

    /**
     * Tests whether the given double value is zero, according to the defined
     * {@link #epsilon()}.
     *
     * @param a the value to test
     * @return {@code true} if the given value is (near) zero, {@code false}
     *         otherwise
     */
    public boolean isZero(final double a) {
        return equals(a, 0);
    }

    /**
     * Tests whether the given double value is not zero, according to the defined
     * {@link #epsilon()}.
     *
     * @param a the value to test
     * @return {@code true} if the given value is not (near) zero, {@code false}
     *         otherwise
     */
    public boolean isNotZero(final double a) {
        return !isZero(a);
    }

    /**
     * Tests whether the given double value is one, according to the defined
     * {@link #epsilon()}.
     *
     * @param a the value to test
     * @return {@code true} if the given value is (near) one, {@code false}
     *         otherwise
     */
    public boolean isOne(final double a) {
        return equals(a, 1);
    }

    public static NumericalContext ofPrecision(final int precision) {
        return new NumericalContext(Math.pow(10, -Math.abs(precision)));
    }

    /**
     * Set a new epsilon value for the numerical context.
     *
     * @param precission the new context precission
     */
    public static void precission(final int precission) {
        CONTEXT.set(new NumericalContext(Math.pow(10, -Math.abs(precission))));
    }

    /**
     * Return the numerical context.
     *
     * @return the numerical context
     */
    public static NumericalContext instance() {
        return CONTEXT.get();
    }

    @SuppressWarnings("removal")
    private static final class Env {
        private static final int precission = java.security.AccessController.doPrivileged(
                (java.security.PrivilegedAction<Integer>)() -> {
                    final int value = Integer.getInteger(
                        "io.jenetics.lattice.defaultPrecision",
                        9
                    );

                    return min(max(value, 1), 20);
                }
            );
    }

}
