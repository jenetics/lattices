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
import static io.jenetics.linealgebra.MatrixRandom.next;

import cern.colt.matrix.DoubleMatrix2D;

import org.assertj.core.data.Percentage;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.jenetics.linealgebra.Colts;
import io.jenetics.linealgebra.structure.Extent2d;
import io.jenetics.linealgebra.structure.Loop2d;
import io.jenetics.linealgebra.structure.Range1d;
import io.jenetics.linealgebra.structure.Range2d;
import io.jenetics.linealgebra.structure.Stride2d;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class DoubleMatrix2dTest {


    @Test(dataProvider = "matricesRanges")
    public void copy(final DoubleMatrix2d matrix, final Range2d range) {
        if (range != null) {
            final var copy = matrix.copy(range);

            final var loop = new Loop2d.RowMajor(range);
            loop.forEach((r, c) -> {
                final var i = r - range.row();
                final var j = c - range.column();

                assertThat(copy.get(i, j))
                    .withFailMessage("Expected \n%s\nbut got\n%s".formatted(matrix, copy))
                    .isEqualTo(matrix.get(r, c));
            });
        }
    }

    @DataProvider
    public Object[][] matricesRanges() {
        return new Object[][] {
            { next(new Extent2d(10, 10)), new Range2d(0, 0, 5, 5) },
            { next(new Extent2d(10, 10)), new Range2d(5, 5, 5, 5) },
            { next(new Extent2d(10, 10)), new Range2d(2, 3, 7, 4) },
            { next(new Extent2d(50, 10)), new Range2d(23, 3, 16, 7) },
            { next(new Extent2d(77, 59)), new Range2d(23, 3, 16, 7) },

            // Test also matrix views.
            {
                next(new Extent2d(77, 59))
                    .view(new Range2d(3, 7, 20, 30)),
                new Range2d(12, 3, 5, 7),
            },
            {
                next(new Extent2d(77, 59))
                    .view(new Range2d(0, 0, 20, 30)),
                new Range2d(1, 3, 11, 7)
            },
            {
                next(new Extent2d(77, 59))
                    .view(new Range2d(0, 0, 20, 30)),
                new Range2d(0, 0, 11, 7)
            },
            {
                next(new Extent2d(77, 59))
                    .view(new Range2d(3, 2, 20, 30)),
                new Range2d(0, 0, 11, 7)
            },
            {
                next(new Extent2d(77, 59))
                    .view(new Range2d(3, 2, 20, 30))
                    .view(new Range2d(3, 2, 10, 20)),
                new Range2d(0, 0, 5, 7)
            },
            {
                next(new Extent2d(77, 59))
                    .view(new Range2d(3, 2, 20, 30))
                    .view(new Stride2d(2, 3)),
                new Range2d(1, 2, 5, 4)
            },
        };
    }

    @Test(dataProvider = "matricesRanges")
    public void view(final DoubleMatrix2d matrix, final Range2d range) {
        if (range != null) {
            final var view = matrix.view(range);

            final var loop = new Loop2d.RowMajor(range);
            loop.forEach((r, c) -> {
                final var i = r - range.row();
                final var j = c - range.column();

                assertThat(view.get(i, j))
                    .withFailMessage("Expected \n%s\nbut got\n%s".formatted(matrix.copy(range), view))
                    .isEqualTo(matrix.get(r, c));
            });
        }
    }

    @Test
    public void equals() {
        final var dimension = new Extent2d(100, 34);
        final var a = next(dimension);
        final var b = a.copy();

        assertThat(b).isNotSameAs(a);
        assertThat(b).isEqualTo(a);
    }

    @Test
    public void assign() {
        final var matrix = DoubleMatrix2d.DENSE_FACTORY.newInstance(4, 3);
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
        final var dimension = new Extent2d(10, 10);

        final var A = next(dimension);
        final var column5 = A.columnAt(5);

        assertThat(column5.size()).isEqualTo(dimension.rows());
        for (int i = 0; i < dimension.rows(); ++i) {
            assertThat(column5.get(i)).isEqualTo(A.get(i, 5));
        }

        /*
        final var val = new AtomicInteger();
        A.forEach((i, j) -> A.set(i, j, val.incrementAndGet()));

        column5.assign(0);
        System.out.println(column5);
        System.out.println();
        System.out.println(A);
         */


        //A.set(2, 23, 1234);
        //assertThat(column5.get(2)).isEqualTo(1234);

        column5
            .view(new Range1d(2, 5))
            .update(v -> 0);

        System.out.println(column5.view(new Range1d(2, 5)));
        System.out.println(A.columnAt(5));
        System.out.println(A);

        for (int i = 0; i < 5; ++i) {
            assertThat(A.get(i + 2, 5)).isEqualTo(0);
        }
    }

    @Test
    public void columnViewLike() {
        final var dimension = new Extent2d(23, 54);

        final var A = next(dimension);
        final var column = A.columnAt(0);
        final var like = column.like();

        assertThat(like.size()).isEqualTo(A.rows());
        like.set(like.size() - 1, 89);
    }

    @Test
    public void rowView() {
        final var dimension = new Extent2d(23, 54);

        final var A = next(dimension);
        final var row12 = A.rowAt(12);

        assertThat(row12.size()).isEqualTo(dimension.cols());
        for (int i = 0; i < dimension.cols(); ++i) {
            assertThat(row12.get(i)).isEqualTo(A.get(12, i));
        }
    }

    @Test
    public void reduce() {
        final var matrix = DoubleMatrix2d.DENSE_FACTORY.newInstance(4, 3);
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
        final var matrix = DoubleMatrix2d.DENSE_FACTORY.newInstance(4, 3);
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
        final var dimension = new Extent2d(10, 10);

        final var A = next(dimension);
        final var B = next(dimension);
        final var C = A.mult(B, null, 2, 3, false, false);

        final var coltA = Colts.toColt(A);
        final var coltB = Colts.toColt(B);
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
