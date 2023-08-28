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
package io.jenetics.lattices.matrix.linalg;

import static org.assertj.core.api.Assertions.assertThat;
import static io.jenetics.lattices.Colts.toColt;
import static io.jenetics.lattices.Colts.toLinealgebra;
import static io.jenetics.lattices.LinealgebraAsserts.EPSILON;
import static io.jenetics.lattices.LinealgebraAsserts.assertEquals;

import java.util.random.RandomGenerator;

import org.testng.annotations.Test;

import io.jenetics.lattices.structure.Extent2d;
import io.jenetics.lattices.MatrixRandom;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class AlgebraTest {

    private static final cern.colt.matrix.linalg.Algebra COLT =
        cern.colt.matrix.linalg.Algebra.DEFAULT;


    @Test(invocationCount = 15)
    public void norm1Vector() {
        final var random = RandomGenerator.getDefault();
        final var size = random.nextInt(10, 15);
        final var x = MatrixRandom.nextDoubleMatrix1d(size);

        assertThat(Algebra.norm1(x))
            .isCloseTo(COLT.norm1(toColt(x)), EPSILON);
    }

    @Test(invocationCount = 15)
    public void norm1Matrix() {
        final var random = RandomGenerator.getDefault();
        final var extent = new Extent2d(
            random.nextInt(10, 15),
            random.nextInt(10, 15)
        );
        final var A = MatrixRandom.nextDoubleMatrix2d(extent);

        assertThat(Algebra.norm1(A))
            .isCloseTo(COLT.norm1(toColt(A)), EPSILON);
    }

    @Test(invocationCount = 15)
    public void norm2Vector() {
        final var random = RandomGenerator.getDefault();
        final var size = random.nextInt(10, 15);
        final var x = MatrixRandom.nextDoubleMatrix1d(size);

        assertThat(Algebra.norm2(x))
            .isCloseTo(COLT.norm2(toColt(x)), EPSILON);
    }

    @Test(invocationCount = 15)
    public void norm2Matrix() {
        final var random = RandomGenerator.getDefault();
        final var size = random.nextInt(10, 15);
        final var A = MatrixRandom.nextDoubleMatrix2d(size, size);

        assertThat(Algebra.norm2(A))
            .isCloseTo(COLT.norm2(toColt(A)), EPSILON);
    }

    @Test(invocationCount = 15)
    public void normInfVector() {
        final var random = RandomGenerator.getDefault();
        final var size = random.nextInt(10, 15);
        final var x = MatrixRandom.nextDoubleMatrix1d(size);

        assertThat(Algebra.normInf(x))
            .isCloseTo(COLT.normInfinity(toColt(x)), EPSILON);
    }

    @Test(invocationCount = 15)
    public void normInfMatrix() {
        final var random = RandomGenerator.getDefault();
        final var extent = new Extent2d(
            random.nextInt(10, 15),
            random.nextInt(10, 15)
        );
        final var A = MatrixRandom.nextDoubleMatrix2d(extent);

        assertThat(Algebra.normInf(A))
            .isCloseTo(COLT.normInfinity(toColt(A)), EPSILON);
    }

    @Test(invocationCount = 15)
    public void trace() {
        final var random = RandomGenerator.getDefault();
        final var extent = new Extent2d(
            random.nextInt(10, 15),
            random.nextInt(10, 15)
        );
        final var A = MatrixRandom.nextDoubleMatrix2d(extent);

        assertThat(Algebra.trace(A))
            .isCloseTo(COLT.trace(toColt(A)), EPSILON);
    }

    @Test(invocationCount = 15)
    public void det() {
        final var random = RandomGenerator.getDefault();
        final var size = random.nextInt(10, 15);
        final var A = MatrixRandom.nextDoubleMatrix2d(size, size);

        assertThat(Algebra.det(A))
            .isCloseTo(COLT.det(toColt(A)), EPSILON);
    }

    @Test(invocationCount = 15)
    public void cond() {
        final var random = RandomGenerator.getDefault();
        final var size = random.nextInt(10, 15);
        final var A = MatrixRandom.nextDoubleMatrix2d(size, size);

        assertThat(Algebra.cond(A))
            .isCloseTo(COLT.cond(toColt(A)), EPSILON);
    }

    @Test(invocationCount = 20, successPercentage = 80)
    public void solve() {
        final var extent = new Extent2d(15, 15);
        final var A = MatrixRandom.nextDoubleMatrix2d(extent);
        final var B = MatrixRandom.nextDoubleMatrix2d(extent);

        assertEquals(
            Algebra.solve(A, B),
            toLinealgebra(COLT.solve(toColt(A), toColt(B)))
        );
    }

    @Test(invocationCount = 20, successPercentage = 80)
    public void inverse() {
        final var random = RandomGenerator.getDefault();
        final var cols = random.nextInt(10, 15);
        final var rows = random.nextInt(cols, 100);
        final var extent = new Extent2d(rows, cols);
        final var A = MatrixRandom.nextDoubleMatrix2d(extent);

        assertEquals(Algebra.inverse(A), toLinealgebra(COLT.inverse(toColt(A))));
    }

}
