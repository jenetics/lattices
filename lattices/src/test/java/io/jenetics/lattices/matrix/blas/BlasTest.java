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

import static org.assertj.core.api.Assertions.assertThat;
import static io.jenetics.lattices.testfixtures.LinealgebraAsserts.assertEquals;
import static io.jenetics.lattices.testfixtures.LinealgebraAsserts.assertNotEquals;
import static io.jenetics.lattices.testfixtures.MatrixRandom.next;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.linalg.SeqBlas;

import java.util.random.RandomGenerator;
import java.util.stream.Stream;

import org.assertj.core.util.DoubleComparator;
import org.testng.annotations.Test;

import io.jenetics.lattices.array.DenseDoubleArray;
import io.jenetics.lattices.grid.DoubleGrid1d;
import io.jenetics.lattices.grid.DoubleGrid2d;
import io.jenetics.lattices.matrix.DoubleMatrix1d;
import io.jenetics.lattices.matrix.DoubleMatrix2d;
import io.jenetics.lattices.structure.Extent1d;
import io.jenetics.lattices.testfixtures.Colts;
import io.jenetics.lattices.testfixtures.DoubleGrid1dComparator;
import io.jenetics.lattices.testfixtures.DoubleGrid2dComparator;
import io.jenetics.lattices.testfixtures.DoubleMatrix1dComparator;
import io.jenetics.lattices.testfixtures.DoubleMatrix2dComparator;

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

    @Test
    public void dscal() {
        final var random = RandomGenerator.getDefault();

        check(
            "dscal",
            random.nextDouble(32, 321),
            next(34)
        );
    }

    @Test
    public void dswap() {
        check("dswap", next(121), next(121));
    }

    @Test
    public void daxpy() {
        final var random = RandomGenerator.getDefault();

        check(
            "daxpy",
            random.nextDouble(32, 1233),
            next(311),
            next(311)
        );
    }

    @Test
    public void ddot() {
        check("ddot", next(12), next(3)
        );
    }

    @Test
    public void dnrm2() {
        check("dnrm2", next(30));
    }

    @Test
    public void dasum() {
        check("dasum", next(23));
    }

    @Test
    public void idamax() {
        check("idamax", next(43));
    }

    @Test(invocationCount = 5)
    public void dgemv() {
        final var random = RandomGenerator.getDefault();

        check(
            "dgemv",
            random.nextBoolean(),
            random.nextDouble(3, 12),
            next(5, 5),
            next(5),
            random.nextDouble(2, 4),
            next(5)
        );
    }

    @Test
    public void dger() {
        final var random = RandomGenerator.getDefault();

        check(
            "dger",
            random.nextDouble(3, 31),
            next(7),
            next(7),
            next(7, 7)
        );
    }

    @Test(invocationCount = 5)
    public void dsymv() {
        final var random = RandomGenerator.getDefault();

        check(
            "dsymv",
            random.nextBoolean(),
            random.nextDouble(6, 34),
            next(10, 10),
            next(10),
            random.nextDouble(8, 89),
            next(10)
        );
    }

    @Test(invocationCount = 10)
    public void dtrmv() {
        final var random = RandomGenerator.getDefault();

        check(
            "dtrmv",
            random.nextBoolean(),
            random.nextBoolean(),
            random.nextBoolean(),
            next(10, 10),
            next(10)
        );
    }

    @Test
    public void dcopy_l3() {
        check("dcopy", next(20, 20), next(20, 20));
    }

    @Test
    public void dscal_l3() {
        final var random = RandomGenerator.getDefault();

        check("dscal", random.nextDouble(9, 87), next(34, 34));
    }

    @Test(invocationCount = 5)
    public void dgemm() {
        final var random = RandomGenerator.getDefault();

        check(
            "dgemm",
            random.nextBoolean(),
            random.nextBoolean(),
            random.nextDouble(2, 5),
            next(3, 3),
            next(3, 3),
            random.nextDouble(2, 6),
            next(3, 3)
        );
    }

    @Test
    public void daxpy_l3() {
        final var random = RandomGenerator.getDefault();

        check(
            "daxpy",
            random.nextDouble(23, 244),
            next(30, 30),
            next(30, 30)
        );
    }

    @Test
    public void dswap_l3() {
        check("dswap", next(34, 23), next(34, 23));
    }

    /* *************************************************************************
     * Helper methods for closing the gap between this and Colt.
     * ************************************************************************/

    private static final double PRECISION = Math.pow(10, -5);

    private static void check(final String name, final Object... parameters) {
        final Object[] result = blas(name, parameters);
        final Object[] expected = colt(name, parameters);

        assertThat(result)
            .usingComparatorForType(new DoubleComparator(PRECISION), Double.class)
            .usingComparatorForType(new DoubleGrid1dComparator(PRECISION), DoubleGrid1d.class)
            .usingComparatorForType(new DoubleGrid2dComparator(PRECISION), DoubleGrid2d.class)
            .usingComparatorForType(new DoubleMatrix1dComparator(PRECISION), DoubleMatrix1d.class)
            .usingComparatorForType(new DoubleMatrix2dComparator(PRECISION), DoubleMatrix2d.class)
            .isEqualTo(expected);
    }

    private static int compare(final double v1, final double v2) {
        return Math.abs(v1 - v2) <= Math.abs(v1*0.01) ? 0 : 1;
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
        } else if (type == Boolean.class) {
          return boolean.class;
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
