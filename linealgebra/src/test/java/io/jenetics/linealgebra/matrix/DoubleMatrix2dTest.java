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
package io.jenetics.linealgebra.matrix;

import static org.assertj.core.api.Assertions.assertThat;

import cern.colt.matrix.DoubleMatrix2D;

import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

import org.assertj.core.data.Percentage;
import org.testng.annotations.Test;

import io.jenetics.linealgebra.ColtMatrices;
import io.jenetics.linealgebra.DenseDoubleMatrix2dRandom;
import io.jenetics.linealgebra.structure.Extent2d;
import io.jenetics.linealgebra.structure.Loop2d;

public class DoubleMatrix2dTest {

    @Test
    public void equals() {
        final var generator = RandomGeneratorFactory.getDefault().create();
        final var random = new DenseDoubleMatrix2dRandom(generator);

        final var dimension = new Extent2d(100, 34);
        final var a = random.next(dimension);
        final var b = a.copy();

        assertThat(b).isNotSameAs(a);
        assertThat(b).isEqualTo(a);
    }

    @Test
    public void assign() {
        final var matrix = DoubleMatrix2d.DENSE_FACTORY.newMatrix(4, 3);
        matrix.assign(new double[][] {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9},
            {10, 11, 12}
        });

        assertThat(matrix.get(0,0)).isEqualTo(1);
        assertThat(matrix.get(1,1)).isEqualTo(5);
        assertThat(matrix.get(2,2)).isEqualTo(9);
        assertThat(matrix.get(3,2)).isEqualTo(12);
    }

    @Test
    public void columnView() {
        final var generator = RandomGenerator.getDefault();
        final var random = new DenseDoubleMatrix2dRandom(generator);
        final var dimension = new Extent2d(23, 54);

        final var A = random.next(dimension);
        final var column23 = A.col(23);

        assertThat(column23.size()).isEqualTo(dimension.rows());
        for (int i = 0; i < dimension.rows(); ++i) {
            assertThat(column23.get(i)).isEqualTo(A.get(i, 23));
        }
    }

    @Test
    public void rowView() {
        final var generator = RandomGenerator.getDefault();
        final var random = new DenseDoubleMatrix2dRandom(generator);
        final var dimension = new Extent2d(23, 54);

        final var A = random.next(dimension);
        final var row12 = A.row(12);

        assertThat(row12.size()).isEqualTo(dimension.cols());
        for (int i = 0; i < dimension.cols(); ++i) {
            assertThat(row12.get(i)).isEqualTo(A.get(12, i));
        }
    }

    @Test
    public void reduce() {
        final var matrix = DoubleMatrix2d.DENSE_FACTORY.newMatrix(4, 3);
        matrix.assign(new double[][] {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9},
            {10, 11, 12}
        });

        final var result = matrix.reduce(Double::sum, a -> 2*a);
        assertThat(result).isEqualTo(156);
    }

    @Test
    public void sum() {
        final var matrix = DoubleMatrix2d.DENSE_FACTORY.newMatrix(4, 3);
        matrix.assign(new double[][] {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9},
            {10, 11, 12}
        });

        final var result = matrix.sum();
        assertThat(result).isEqualTo(78);
    }

    @Test
    public void mult() {
        final var generator = RandomGeneratorFactory.getDefault().create(1234);
        final var random = new DenseDoubleMatrix2dRandom(generator);
        final var dimension = new Extent2d(10, 10);

        final var A = random.next(dimension);
        final var B = random.next(dimension);
        final var C = A.mult(B, null, 2, 3, false, false);

        final var coltA = ColtMatrices.of(A);
        final var coltB = ColtMatrices.of(B);
        final var coltC = coltA.zMult(coltB, null, 2, 3, false, false);

        assertEquals(C, coltC);
    }

    private static void assertEquals(final DoubleMatrix2d a, final DoubleMatrix2D coltA) {
        final var epsilon = Percentage.withPercentage(0.01);

        new Loop2d.RowMajor(a.extent()).forEach((r, c) ->
            assertThat(a.get(r, c)).isCloseTo(coltA.getQuick(r, c), epsilon)
        );
    }

}