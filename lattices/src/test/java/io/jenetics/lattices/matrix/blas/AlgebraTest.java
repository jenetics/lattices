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
import static io.jenetics.lattices.testfuxtures.Colts.toColt;
import static io.jenetics.lattices.testfuxtures.Colts.toLinealgebra;
import static io.jenetics.lattices.testfuxtures.LinealgebraAsserts.EPSILON;
import static io.jenetics.lattices.testfuxtures.LinealgebraAsserts.assertEquals;
import static io.jenetics.lattices.testfuxtures.MatrixRandom.next;

import java.util.random.RandomGenerator;

import org.testng.annotations.Test;

import io.jenetics.lattices.structure.Extent2d;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class AlgebraTest {

    private static final cern.colt.matrix.linalg.Algebra COLT =
        cern.colt.matrix.linalg.Algebra.DEFAULT;


    @Test(invocationCount = 5)
    public void norm1Vector() {
        final var random = RandomGenerator.getDefault();
        final var x = next(random.nextInt(123));

        assertThat(Algebra.norm1(x))
            .isCloseTo(COLT.norm1(toColt(x)), EPSILON);
    }

    @Test(invocationCount = 5)
    public void norm1Matrix() {
        final var random = RandomGenerator.getDefault();
        final var A = next(random.nextInt(10, 100), random.nextInt(10, 100));

        assertThat(Algebra.norm1(A))
            .isCloseTo(COLT.norm1(toColt(A)), EPSILON);
    }

    @Test(invocationCount = 5)
    public void norm2Vector() {
        final var random = RandomGenerator.getDefault();
        final var x = next(random.nextInt(123));

        assertThat(Algebra.norm2(x))
            .isCloseTo(COLT.norm2(toColt(x)), EPSILON);
    }

    @Test(invocationCount = 5)
    public void norm2Matrix() {
        final var random = RandomGenerator.getDefault();
        final var size = random.nextInt(10, 100);
        final var A = next(size, size);

        assertThat(Algebra.norm2(A))
            .isCloseTo(COLT.norm2(toColt(A)), EPSILON);
    }

    @Test(invocationCount = 5)
    public void normInfVector() {
        final var random = RandomGenerator.getDefault();
        final var x = next(random.nextInt(123));

        assertThat(Algebra.normInf(x))
            .isCloseTo(COLT.normInfinity(toColt(x)), EPSILON);
    }

    @Test(invocationCount = 5)
    public void normInfMatrix() {
        final var random = RandomGenerator.getDefault();
        final var A = next(random.nextInt(10, 100), random.nextInt(10, 100));

        assertThat(Algebra.normInf(A))
            .isCloseTo(COLT.normInfinity(toColt(A)), EPSILON);
    }

    @Test(invocationCount = 5)
    public void trace() {
        final var random = RandomGenerator.getDefault();
        final var A = next(random.nextInt(10, 100), random.nextInt(10, 100));

        assertThat(Algebra.trace(A))
            .isCloseTo(COLT.trace(toColt(A)), EPSILON);
    }

    @Test(invocationCount = 5)
    public void det() {
        final var random = RandomGenerator.getDefault();
        final var size = random.nextInt(10, 100);
        final var A = next(size, size);

        assertThat(Algebra.det(A))
            .isCloseTo(COLT.det(toColt(A)), EPSILON);
    }

    @Test(invocationCount = 5)
    public void cond() {
        final var random = RandomGenerator.getDefault();
        final var size = random.nextInt(10, 100);
        final var A = next(size, size);

        assertThat(Algebra.cond(A))
            .isCloseTo(COLT.cond(toColt(A)), EPSILON);
    }

    @Test(invocationCount = 5)
    public void solve() {
        //final var extent = new Extent2d(1000, 1000);
        final var extent = new Extent2d(55, 55);
        final var A = next(extent);
        final var B = next(extent);

        assertEquals(
            Algebra.solve(A, B),
            toLinealgebra(COLT.solve(toColt(A), toColt(B)))
        );
    }

    @Test(invocationCount = 5)
    public void inverse() {
        final var random = RandomGenerator.getDefault();
        final var cols = random.nextInt(45, 65);
        final var rows = random.nextInt(cols, 100);
        final var extent = new Extent2d(rows, cols);
        final var A = next(extent);

        assertEquals(Algebra.inverse(A), toLinealgebra(COLT.inverse(toColt(A))));
    }

}
