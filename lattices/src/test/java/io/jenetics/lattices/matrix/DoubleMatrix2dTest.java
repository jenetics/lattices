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
package io.jenetics.lattices.matrix;

import static org.assertj.core.api.Assertions.assertThat;
import static io.jenetics.lattices.testfixtures.MatrixRandom.next;

import cern.colt.matrix.DoubleMatrix2D;

import io.jenetics.lattices.testfixtures.MatrixRandom;
import org.assertj.core.data.Percentage;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.jenetics.lattices.grid.array.DenseDoubleArray;
import io.jenetics.lattices.grid.DoubleGrid2d;
import io.jenetics.lattices.grid.lattice.Loop2d;
import io.jenetics.lattices.structure.Extent2d;
import io.jenetics.lattices.structure.Index2d;
import io.jenetics.lattices.structure.Range2d;
import io.jenetics.lattices.structure.Stride2d;
import io.jenetics.lattices.structure.Structure2d;
import io.jenetics.lattices.structure.View2d;
import io.jenetics.lattices.testfixtures.Colts;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class DoubleMatrix2dTest {


    @Test(dataProvider = "matricesRanges")
    public void copy(final DoubleMatrix2d matrix, final Range2d range) {
        if (range != null) {
            final var copy = matrix.view(View2d.of(range)).copy();

            Loop2d.of(range).forEach((r, c) -> {
                final var i = r - range.start().row();
                final var j = c - range.start().col();

                assertThat(copy.get(i, j))
                    .withFailMessage("Expected \n%s\nbut got\n%s".formatted(matrix, copy))
                    .isEqualTo(matrix.get(r, c));
            });
        }
    }

    @DataProvider
    public Object[][] matricesRanges() {
        return new Object[][] {
            { MatrixRandom.nextDoubleMatrix2d(new Extent2d(10, 10)), new Range2d(new Index2d(0, 0), new Extent2d(10, 10)) },
            { MatrixRandom.nextDoubleMatrix2d(new Extent2d(10, 10)), new Range2d(new Index2d(0, 0), new Extent2d(5, 5)) },
            { MatrixRandom.nextDoubleMatrix2d(new Extent2d(10, 10)), new Range2d(new Index2d(5, 5), new Extent2d(5, 5)) },
            { MatrixRandom.nextDoubleMatrix2d(new Extent2d(10, 10)), new Range2d(new Index2d(2, 3), new Extent2d(7, 4)) },
            { MatrixRandom.nextDoubleMatrix2d(new Extent2d(50, 10)), new Range2d(new Index2d(23, 3), new Extent2d(16, 7)) },
            { MatrixRandom.nextDoubleMatrix2d(new Extent2d(77, 59)), new Range2d(new Index2d(23, 3), new Extent2d(16, 7)) },

            // Test also matrix views.
            {
                MatrixRandom.nextDoubleMatrix2d(new Extent2d(77, 59))
                    .view(View2d.of(new Range2d(new Index2d(3, 7), new Extent2d(20, 30))))
                    .transpose(),
                new Range2d(new Index2d(12, 3), new Extent2d(5, 7)),
            },
            {
                MatrixRandom.nextDoubleMatrix2d(new Extent2d(77, 59))
                    .view(View2d.of(new Range2d(new Index2d(0, 0), new Extent2d(20, 30)))),
                new Range2d(new Index2d(1, 3), new Extent2d(11, 7))
            },
            {
                MatrixRandom.nextDoubleMatrix2d(new Extent2d(77, 59))
                    .view(View2d.of(new Range2d(new Index2d(0, 0), new Extent2d(20, 30)))),
                new Range2d(new Index2d(0, 0), new Extent2d(11, 7))
            },
            {
                MatrixRandom.nextDoubleMatrix2d(new Extent2d(77, 59))
                    .view(View2d.of(new Range2d(new Index2d(3, 2), new Extent2d(20, 30)))),
                new Range2d(new Index2d(0, 0), new Extent2d(11, 7))
            },
            {
                MatrixRandom.nextDoubleMatrix2d(new Extent2d(77, 59))
                    .view(View2d.of(new Range2d(new Index2d(3, 2), new Extent2d(20, 30))))
                    .view(View2d.of(new Range2d(new Index2d(3, 2), new Extent2d(10, 20)))),
                new Range2d(new Index2d(0, 0), new Extent2d(5, 7))
            },
            {
                MatrixRandom.nextDoubleMatrix2d(new Extent2d(77, 59))
                    .view(View2d.of(new Range2d(new Index2d(3, 2), new Extent2d(20, 30))))
                    .view(View2d.of(new Stride2d(2, 3))),
                new Range2d(new Index2d(1, 2), new Extent2d(5, 4))
            },
        };
    }

    @Test(dataProvider = "matricesRanges")
    public void view(final DoubleMatrix2d matrix, final Range2d range) {
        if (range != null) {
            final var view = matrix.view(View2d.of(range));

            Loop2d.of(range).forEach((r, c) -> {
                final var i = r - range.start().row();
                final var j = c - range.start().col();

                assertThat(view.get(i, j))
                    .withFailMessage("Expected \n%s\nbut got\n%s"
                        .formatted(matrix.view(View2d.of(range)).copy(), view))
                    .isEqualTo(matrix.get(r, c));
            });
        }
    }

    @Test(dataProvider = "matricesRanges")
    public void columnViewFromMatrix(final DoubleMatrix2d matrix, final Range2d range) {
        var A = matrix;

        for (int c = 0; c < A.cols(); ++c) {
            final var column = A.colAt(c);

            assertThat(column.extent().size()).isEqualTo(A.rows());
            for (int i = 0; i < A.rows(); ++i) {
                assertThat(column.get(i)).isEqualTo(A.get(i, c));
            }
        }
    }

    @Test(dataProvider = "matricesRanges")
    public void columnViewFromMatrixCopy(final DoubleMatrix2d matrix, final Range2d range) {
        var A = matrix.view(View2d.of(range)).copy();

        for (int c = 0; c < A.cols(); ++c) {
            final var column = A.colAt(c);

            assertThat(column.extent().size()).isEqualTo(A.rows());
            for (int i = 0; i < A.rows(); ++i) {
                assertThat(column.get(i)).isEqualTo(A.get(i, c));
            }
        }
    }

    @Test(dataProvider = "matricesRanges")
    public void columnViewFromMatrixView(final DoubleMatrix2d matrix, final Range2d range) {
        var A = matrix.view(View2d.of(range));

        for (int c = 0; c < A.cols(); ++c) {
            final var column = A.colAt(c);

            assertThat(column.extent().size()).isEqualTo(A.rows());
            for (int i = 0; i < A.rows(); ++i) {
                assertThat(column.get(i)).isEqualTo(A.get(i, c));
            }
        }
    }

    @Test(dataProvider = "matricesRanges")
    public void rowViewFromMatrix(final DoubleMatrix2d matrix, final Range2d range) {
        var A = matrix;

        for (int r = 0; r < A.rows(); ++r) {
            final var row = A.rowAt(r);

            assertThat(row.extent().size()).isEqualTo(A.cols());
            for (int i = 0; i < A.cols(); ++i) {
                assertThat(row.get(i)).isEqualTo(A.get(r, i));
            }
        }
    }

    @Test(dataProvider = "matricesRanges")
    public void rowViewFromMatrixCopy(final DoubleMatrix2d matrix, final Range2d range) {
        var A = matrix.view(View2d.of(range)).copy();

        for (int r = 0; r < A.rows(); ++r) {
            final var row = A.rowAt(r);

            assertThat(row.extent().size()).isEqualTo(A.cols());
            for (int i = 0; i < A.cols(); ++i) {
                assertThat(row.get(i)).isEqualTo(A.get(r, i));
            }
        }
    }

    @Test(dataProvider = "matricesRanges")
    public void rowViewFromMatrixView(final DoubleMatrix2d matrix, final Range2d range) {
        var A = matrix.view(View2d.of(range));

        for (int r = 0; r < A.rows(); ++r) {
            final var row = A.rowAt(r);

            assertThat(row.extent().size()).isEqualTo(A.cols());
            for (int i = 0; i < A.cols(); ++i) {
                assertThat(row.get(i)).isEqualTo(A.get(r, i));
            }
        }
    }

    @Test(dataProvider = "matricesRanges")
    public void transpose(final DoubleMatrix2d matrix, final Range2d range) {
        var A = matrix.copy().transpose();
        assertThat(A.cols()).isEqualTo(matrix.rows());
        assertThat(A.rows()).isEqualTo(matrix.cols());

        Loop2d.of(range).forEach((r, c) ->
            assertThat(matrix.get(r, c)).isEqualTo(A.get(c, r))
        );
    }

    @Test
    public void equals() {
        final var dimension = new Extent2d(100, 34);
        final var a = MatrixRandom.nextDoubleMatrix2d(dimension);
        final var b = a.copy();

        assertThat(b).isNotSameAs(a);
        assertThat(b).isEqualTo(a);
    }

    @Test
    public void assign() {
        final var matrix = DoubleMatrix2d.DENSE.create(4, 3);
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
    public void transpose() {

    }

    @Test
    public void reduce() {
        final var matrix = DoubleMatrix2d.DENSE.create(4, 3);
        matrix.assign(new double[][] {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9},
            {10, 11, 12}
        });

        final var result = matrix.reduce(Double::sum, a -> 2*a).orElseThrow();
        assertThat(result).isEqualTo(156);
    }

    @Test
    public void sum() {
        final var matrix = DoubleMatrix2d.DENSE.create(4, 3);
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

        final var A = MatrixRandom.nextDoubleMatrix2d(dimension);
        final var B = MatrixRandom.nextDoubleMatrix2d(dimension);
        final var C = A.mult(B, null, 2, 3, false, false);

        final var coltA = Colts.toColt(A);
        final var coltB = Colts.toColt(B);
        final var coltC = coltA.zMult(coltB, null, 2, 3, false, false);

        assertEquals(C, coltC);
    }

    private static void assertEquals(final DoubleMatrix2d a, final DoubleMatrix2D coltA) {
        final var epsilon = Percentage.withPercentage(0.01);

        Loop2d.of(new Range2d(a.extent())).forEach((r, c) ->
            assertThat(a.get(r, c)).isCloseTo(coltA.getQuick(r, c), epsilon)
        );
    }

    @Test
    public void isView() {
        final var extent = new Extent2d(4, 6);
        final var matrix = DoubleMatrix2d.DENSE.create(extent);

        final var structure = matrix.structure();
        final var copy = Structure2d.of(extent);
        assertThat(copy).isEqualTo(structure);

        final var copy2 = Structure2d.of(new Extent2d(3, 6));
        assertThat(copy2).isNotEqualTo(structure);
    }

    @Test
    public void foo() {
        final var matrix = new DoubleMatrix2d(
            Structure2d.of(new Extent2d(3, 4)),
            new DenseDoubleArray(new double[] {
                1, 2,  3,  4,
                5, 6,  7,  8,
                9, 10, 11, 12
            })
        );

        final var foo = DoubleMatrix2d.of(
            new Extent2d(3, 4),
            1, 2,  3,  4,
            5, 6,  7,  8,
            9, 10, 11, 12
        );
    }

    @Test
    public void constructor() {
        final var grid = DoubleGrid2d.DENSE.create(10, 10);
        grid.assign(29.2);

        final var matrix = new DoubleMatrix2d(grid);
        assertThat(grid.equals(matrix)).isTrue();
    }

}
