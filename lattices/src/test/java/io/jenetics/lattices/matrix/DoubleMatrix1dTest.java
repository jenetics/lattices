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
import static io.jenetics.lattices.testfuxtures.MatrixRandom.next;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.jenetics.lattices.grid.Loop1d;
import io.jenetics.lattices.structure.Extent1d;
import io.jenetics.lattices.structure.Index1d;
import io.jenetics.lattices.structure.Range1d;
import io.jenetics.lattices.structure.View1d;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class DoubleMatrix1dTest {

    @Test
    public void like() {
        final var matrix = DoubleMatrix1d.DENSE.create(23);
        matrix.forEach(i -> matrix.set(i, 2*i));
        final var like = matrix.like();

        assertThat(like.size()).isEqualTo(matrix.size());
        like.forEach(i -> assertThat(like.get(i)).isEqualTo(0.0));
    }

    @Test(dataProvider = "matricesRanges")
    public void copy(final DoubleMatrix1d matrix, final Range1d range) {
        if (range != null) {
            final var copy = matrix.view(View1d.of(range)).copy();

            Loop1d.of(range).forEach(i -> {
                final var j = i - range.start().value();

                assertThat(copy.get(j))
                    .withFailMessage("Expected \n%s\nbut got\n%s".formatted(matrix, copy))
                    .isEqualTo(matrix.get(i));
            });
        }
    }

    @DataProvider
    public Object[][] matricesRanges() {
        return new Object[][] {
            { next(new Extent1d(10)), new Range1d(new Index1d(0), new Extent1d(0)) },
            { next(new Extent1d(10)), new Range1d(new Index1d(5), new Extent1d(5)) },
            { next(new Extent1d(10)), new Range1d(new Index1d(2), new Extent1d(3)) },
            { next(new Extent1d(50)), new Range1d(new Index1d(23), new Extent1d(3)) },
            { next(new Extent1d(77)), new Range1d(new Index1d(23), new Extent1d(3)) },

            // Test also matrix views.
            {
                next(new Extent1d(77))
                    .view(View1d.of(new Range1d(new Index1d(3), new Extent1d(7)))),
                new Range1d(new Index1d(1), new Extent1d(3)),
            }
        };
    }

    @Test(dataProvider = "matricesRanges")
    public void view(final DoubleMatrix1d matrix, final Range1d range) {
        if (range != null) {
            final var copy = matrix.view(View1d.of(range));

            Loop1d.of(range).forEach(i -> {
                final var j = i - range.start().value();

                assertThat(copy.get(j))
                    .withFailMessage("Expected \n%s\nbut got\n%s".formatted(matrix, copy))
                    .isEqualTo(matrix.get(i));
            });
        }
    }


}
