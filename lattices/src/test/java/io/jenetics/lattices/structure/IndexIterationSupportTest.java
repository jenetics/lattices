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
package io.jenetics.lattices.structure;

import static org.assertj.core.api.Assertions.assertThat;
import static io.jenetics.lattices.structure.IndexIterationSupport.backward;
import static io.jenetics.lattices.structure.IndexIterationSupport.forward;
import static io.jenetics.lattices.structure.Precedence.natural;
import static io.jenetics.lattices.structure.Precedence.reverse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class IndexIterationSupportTest {

    record IntArray(int[] values) {
        IntArray {
            values = values.clone();
        }
        @Override
        public int hashCode() {
            return Arrays.hashCode(values);
        }
        @Override
        public boolean equals(Object obj) {
            return obj instanceof IntArray array &&
                Arrays.equals(values, array.values);
        }
        @Override
        public String toString() {
            return Arrays.toString(values);
        }
    }

    @Test
    public void simpleIteration() {
        final var range = Range.of(Index.of(1, 2, 3), Extent.of(3, 3, 3));
        final var indexes = forward(range, natural(range.dimensionality()));
        for (var index : indexes) {
            System.out.println(Arrays.toString(index));
        }
    }

    @Test(dataProvider = "ranges")
    public void iterable(Range range) {
        var indexes = forward(range, natural(range.dimensionality()));
        assertThat(count(indexes.iterator())).isEqualTo(range.extent().elements());

        indexes = backward(range, natural(range.dimensionality()));
        assertThat(count(indexes.iterator())).isEqualTo(range.extent().elements());

        indexes = forward(range, reverse(range.dimensionality()));
        assertThat(count(indexes.iterator())).isEqualTo(range.extent().elements());

        indexes = backward(range, reverse(range.dimensionality()));
        assertThat(count(indexes.iterator())).isEqualTo(range.extent().elements());
    }

    private static int count(Iterator<int[]> it) {
        final var elements = new HashSet<>();
        while (it.hasNext()) {
            elements.add(new IntArray(it.next()));
        }
        return elements.size();
    }

    @Test(dataProvider = "ranges")
    public void iteratorOrder(Range range) {
        var indexes = forward(range, reverse(range.dimensionality()));
        final var lowMajorForward = elements(indexes.iterator());

        indexes = backward(range, reverse(range.dimensionality()));
        final var lowMajorBackward = elements(indexes.iterator());

        indexes = forward(range, natural(range.dimensionality()));
        final var highMajorForward = elements(indexes.iterator());

        indexes = backward(range, natural(range.dimensionality()));
        final var highMajorBackward = elements(indexes.iterator());

        // Check order between forward and backward
        if (range.extent().elements() > 1) {
            assertThat(lowMajorForward).isNotEqualTo(lowMajorBackward);
            assertThat(highMajorForward).isNotEqualTo(highMajorBackward);
        }
        assertThat(lowMajorForward.reversed()).isEqualTo(lowMajorBackward);
        assertThat(highMajorForward.reversed()).isEqualTo(highMajorBackward);
    }

    private static List<IntArray> elements(Iterator<int[]> it) {
        final var list = new ArrayList<IntArray>();
        while (it.hasNext()) {
            final var index = new IntArray(it.next());
            list.add(index);
            //System.out.println(index);
        }
        return list;
    }

    @DataProvider
    public Object[][] ranges() {
        return new Object[][] {
            { Range.of(Index.of(0), Extent.of(1)) },
            { Range.of(Index.of(0), Extent.of(5)) },
            { Range.of(Index.of(0), Extent.of(17)) },
            { Range.of(Index.of(1), Extent.of(1)) },
            { Range.of(Index.of(2), Extent.of(5)) },
            { Range.of(Index.of(3), Extent.of(17)) },

            { Range.of(Index.of(0, 0), Extent.of(1, 1)) },
            { Range.of(Index.of(0, 0), Extent.of(5, 6)) },
            { Range.of(Index.of(0, 0), Extent.of(7, 6)) },
            { Range.of(Index.of(1, 2), Extent.of(1, 1)) },
            { Range.of(Index.of(1, 2), Extent.of(5, 6)) },
            { Range.of(Index.of(1, 2), Extent.of(7, 6)) },

            { Range.of(Index.of(0, 0, 0), Extent.of(1, 1, 1)) },
            { Range.of(Index.of(0, 0, 0), Extent.of(5, 6, 7)) },
            { Range.of(Index.of(0, 0, 0), Extent.of(7, 6, 10)) },
            { Range.of(Index.of(1, 2, 3), Extent.of(1, 1, 1)) },
            { Range.of(Index.of(1, 2, 3), Extent.of(5, 6, 7)) },
            { Range.of(Index.of(1, 2, 3), Extent.of(7, 6, 10)) },

            { Range.of(Index.of(0, 0, 0, 0), Extent.of(1, 1, 1, 1)) },
            { Range.of(Index.of(0, 0, 0, 0), Extent.of(5, 6, 7, 9)) },
            { Range.of(Index.of(0, 0, 0, 0), Extent.of(7, 6, 10, 9)) },
            { Range.of(Index.of(1, 2, 3, 4), Extent.of(1, 1, 1, 1)) },
            { Range.of(Index.of(1, 2, 3, 4), Extent.of(5, 6, 7, 9)) },
            { Range.of(Index.of(1, 2, 3, 4), Extent.of(7, 6, 10, 9)) },

            { Range.of(Index.of(0, 0, 0, 0, 0), Extent.of(1, 1, 1, 1, 1)) },
            { Range.of(Index.of(0, 0, 0, 0, 0), Extent.of(5, 6, 7, 9, 17)) },
            { Range.of(Index.of(0, 0, 0, 0, 0), Extent.of(7, 6, 10, 9, 3)) },
            { Range.of(Index.of(1, 2, 3, 4, 5), Extent.of(1, 1, 1, 1, 1)) },
            { Range.of(Index.of(1, 2, 3, 4, 5), Extent.of(5, 6, 7, 9, 17)) },
            { Range.of(Index.of(1, 2, 3, 4, 5), Extent.of(7, 6, 10, 9, 3)) },

            { Range.of(Index.of(0, 0, 0, 0, 0, 0), Extent.of(1, 1, 1, 1, 1, 1)) },
            { Range.of(Index.of(0, 0, 0, 0, 0, 0), Extent.of(5, 6, 7, 9, 17, 11)) },
            { Range.of(Index.of(0, 0, 0, 0, 0, 0), Extent.of(7, 6, 10, 9, 3, 7)) },
            { Range.of(Index.of(1, 2, 3, 4, 5, 6), Extent.of(1, 1, 1, 1, 1, 1)) },
            { Range.of(Index.of(1, 2, 3, 4, 5, 6), Extent.of(5, 6, 7, 9, 17, 11)) },
            { Range.of(Index.of(1, 2, 3, 4, 5, 6), Extent.of(7, 6, 10, 9, 3, 7)) }
        };
    }

}
