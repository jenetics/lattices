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

import static org.assertj.core.api.Assertions.assertThat;
import static io.jenetics.linealgebra.LinealgebraAsserts.assertEquals;
import static io.jenetics.linealgebra.LinealgebraAsserts.assertNotEquals;
import static io.jenetics.linealgebra.MatrixRandom.next;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.linalg.SeqBlas;

import java.util.random.RandomGenerator;
import java.util.stream.Stream;

import org.testng.annotations.Test;

import io.jenetics.linealgebra.Colts;
import io.jenetics.linealgebra.array.DenseDoubleArray;
import io.jenetics.linealgebra.grid.Extent1d;
import io.jenetics.linealgebra.matrix.DoubleMatrix1d;
import io.jenetics.linealgebra.matrix.DoubleMatrix2d;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class BlasTest {

    @Test
    public void dcopy() {
        final var x = next(new Extent1d(50));
        final var y = x.like();
        assertNotEquals(x, y);

        Blas.DEFAULT.dcopy(x, y);
        assertEquals(x, y);
    }

    @Test
    public void drotg() {
        final var random = RandomGenerator.getDefault();

        check(
            "drotg",
            random.nextDouble(12, 20),
            random.nextDouble(1, 32),
            ((DenseDoubleArray)next(4).array()).elements()
        );
    }

    @Test
    public void drot() {
        final var random = RandomGenerator.getDefault();

        check(
            "drot",
            next(43),
            next(43),
            random.nextDouble(12, 20),
            random.nextDouble(1, 32)
        );
    }

    /* *************************************************************************
     * Helper methods for closing the gap between this and Colt.
     * ************************************************************************/

    private static void check(final String name, final Object... parameters) {
        final Object[] result = blas(name, parameters);
        final Object[] expected = colt(name, parameters);
        assertThat(result).isEqualTo(expected);
    }

    private static Object[] blas(final String name, final Object... parameters) {
        try {
            final var parameterTypes = Stream.of(parameters)
                .map(Object::getClass)
                .map(BlasTest::toPrimitiveType)
                .toArray(Class<?>[]::new);

            final var parametersCopy = Stream.of(parameters)
                .map(BlasTest::copy)
                .toArray();

            final var method = Blas.class.getMethod(name, parameterTypes);
            final var result = method.invoke(Blas.DEFAULT, parametersCopy);
            return Stream.concat(Stream.of(parametersCopy), Stream.of(result))
                    .toArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Object copy(final Object value) {
        if (value instanceof DoubleMatrix1d matrix) {
            return matrix.copy();
        } else if (value instanceof DoubleMatrix2d matrix) {
            return matrix.copy();
        } else if (value instanceof double[] array) {
            return array.clone();
        } else {
            return value;
        }
    }

    private static Object[] colt(final String name, final Object... parameters) {
        try {
            final var parameterTypes = Stream.of(parameters)
                .map(Object::getClass)
                .map(BlasTest::toPrimitiveType)
                .map(BlasTest::toColtType)
                .toArray(Class<?>[]::new);

            final var parametersCopy = Stream.of(parameters)
                .map(BlasTest::toColt)
                .toArray();

            final var method = SeqBlas.class.getMethod(name, parameterTypes);
            final var result = method.invoke(SeqBlas.seqBlas, parametersCopy);
            return Stream.concat(Stream.of(parametersCopy), Stream.of(result))
                .map(BlasTest::toBlas)
                .toArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Object toColt(final Object value) {
        if (value instanceof DoubleMatrix1d matrix) {
            return Colts.toColt(matrix);
        } else if (value instanceof DoubleMatrix2d matrix) {
            return Colts.toColt(matrix);
        } else if (value instanceof double[] array) {
            return array.clone();
        } else {
            return value;
        }
    }

    private static Object toBlas(final Object value) {
        if (value instanceof DoubleMatrix1D matrix) {
            return Colts.toLinealgebra(matrix);
        } else if (value instanceof DoubleMatrix2D matrix) {
            return Colts.toLinealgebra(matrix);
        } else {
            return value;
        }
    }

    private static Class<?> toColtType(final Class<?> type) {
        if (type == DoubleMatrix1d.class) {
            return DoubleMatrix1D.class;
        } else if (type == DoubleMatrix2d.class) {
            return DoubleMatrix2D.class;
        } else {
            return type;
        }
    }

    private static Class<?> toPrimitiveType(final Class<?> type) {
        if (type == Integer.class) {
            return int.class;
        } else if (type == Double.class) {
            return double.class;
        } else if (type == Long.class) {
            return long.class;
        } else if (type == Integer[].class) {
            return int[].class;
        } else if (type == Double[].class) {
            return double[].class;
        } else if (type == Long[].class) {
            return long[].class;
        } else {
            return type;
        }
    }

}
