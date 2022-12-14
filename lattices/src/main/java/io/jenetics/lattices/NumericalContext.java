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
package io.jenetics.lattices;

import static java.lang.Math.abs;
import static java.util.Objects.requireNonNull;

import java.util.function.Supplier;
import java.util.random.RandomGeneratorFactory;

/**
 * Encapsulates the context settings which describes certain rules for numerical
 * operations. The default context is created with an epsilon of 10<sup>-9</sup>.
 * The default value can be changed with defining a Java <em>property</em> on
 * the command line.
 * <pre>
 * $ java -Dio.jenetics.lattices.precision=12 ...
 * </pre>
 * The example above will change the epsilon of the default context object to
 * 10<sup>-12</sup>. It is also possible to change the numerical context only
 * for specific parts of your code. This might be useful in a testing
 * environment. The following example shows how to do this.
 * <pre>{@code
 * final DoubleMatrix2d A = ...;
 * final DoubleMatrix2d B = ...;
 *
 * // Executes the 'solve' operation with a different epsilon.
 * // Other parts of the program are not effected.
 * NumericalContext.using(new NumericalContext(0.001), () -> {
 *     final DoubleMatrix2d X = Algebra.solve(A, B);
 * });
 * }</pre>
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
public class NumericalContext {

    /**
     * The default context used for linear algebra calculations. It can be
     * defined on the command line with a Java property
     * {@code io.jenetics.lattices.precission}, where {@code precission} is the
     * number of significant decimal digits. The <em>epsilon</em> values is
     * calculated as follows: {@code Math.pow(10, -precission)}.
     */
    private static final NumericalContext DEFAULT_EPSILON_CONTEXT =
        new NumericalContext(EnvPrecission.EPSILON);

    /**
     * Numerical context with an {@link #epsilon()} of zero.
     */
    public static final NumericalContext ZERO_EPSILON =
        new NumericalContext(0.0) {
            @Override
            public boolean equals(final double a, final double b) {
                return Double.compare(a, b) == 0;
            }
        };

    // Holds the current context.
    private static final Context<NumericalContext> CONTEXT =
        new Context<>(DEFAULT_EPSILON_CONTEXT);


    private final double epsilon;

    /**
     * Create a new numerical context with the given epsilon.
     *
     * @param epsilon the {@code epsilon} of this context
     */
    public NumericalContext(final double epsilon) {
        this.epsilon = abs(epsilon);
    }

    /**
     * Return the epsilon value used in this numerical context.
     *
     * @return the epsilon value used in this numerical context
     */
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


    @Override
    public String toString() {
        return "NumericalContext[epsilon=%f]".formatted(epsilon);
    }

    /* *************************************************************************
     * Accessor methods.
     * ************************************************************************/

    /**
     * Return the current numerical context.
     *
     * @return the current numerical context
     */
    public static NumericalContext get() {
        return CONTEXT.get();
    }

    /**
     * Set a new {@link NumericalContext} for the <em>global</em> scope.
     *
     * @param context the new {@link NumericalContext} for the <em>global</em>
     *        scope
     * @throws NullPointerException if the {@code context} object is {@code null}
     */
    public static void set(final NumericalContext context) {
        requireNonNull(context);
        CONTEXT.set(context);
    }

    /**
     * Set the context object to its default value.
     */
    public static void reset() {
        CONTEXT.reset();
    }

    /**
     * Executes the given {@code task} with the new numerical {@code context}.
     * The numerical context effects only the task execution. The following
     * example shows how to solve a matrix equation with a different numerical
     * context then the default one.
     *
     * <pre>{@code
     * final DoubleMatrix2d A = ...;
     * final DoubleMatrix2d B = ...;
     * NumericalContext.using(new NumericalContext(0.001), () -> {
     *     final DoubleMatrix2d X = Algebra.solve(A, B);
     * });
     * }</pre>
     *
     * The example above shuffles the given integer {@code seq} <i>using</i> the
     * given {@link RandomGeneratorFactory#getDefault()} factory.
     *
     * @since 7.0
     *
     * @param context the numerical context used in the given {@code task}
     * @param task the {@code task} which is executed within the <i>scope</i> of
     *        the given numerical context
     * @throws NullPointerException if one of the arguments is {@code null}
     */
    public static void using(final NumericalContext context, final Runnable task) {
        requireNonNull(context);
        requireNonNull(task);

        CONTEXT.with(context, c -> { task.run(); return null; });
    }

    /**
     * Opens a new <em>scope</em> with the given numerical and executes the
     * given {@code supplier}.
     *
     * @param context the numerical context used when executing the
     *        {@code supplier}
     * @param supplier the supplier to execute with the new numerical context
     * @return the supplier result
     * @param <C> the numerical context type
     * @param <T> the type of the supplier result
     */
    public static <C extends NumericalContext, T> T with(
        final C context,
        final Supplier<? extends T> supplier
    ) {
        requireNonNull(context);
        requireNonNull(supplier);

        return CONTEXT.with(context, c -> supplier.get());
    }

    @SuppressWarnings("removal")
    private static final class EnvPrecission {
        private static final int DEFAULT_PRECISSION = 9;

        private static final int PRECISSION =
            java.security.AccessController.doPrivileged(
                (java.security.PrivilegedAction<Integer>)() ->
                    Integer.getInteger(
                        "io.jenetics.lattices.precision",
                        DEFAULT_PRECISSION
                    )
            );

        private static final double EPSILON = Math.pow(10, -PRECISSION);
    }

}
