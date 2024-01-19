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
import static io.jenetics.lattices.structure.IndexCursor.backward;
import static io.jenetics.lattices.structure.IndexCursor.forward;
import static io.jenetics.lattices.structure.IndexCursor.loopable;
import static io.jenetics.lattices.structure.Precedence.natural;
import static io.jenetics.lattices.structure.Precedence.reverse;
import static io.jenetics.lattices.structure.TestRanges.RANGES;

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
public class IndexCursorTest {

    @Test
    public void simpleIteration() {
        final var range = Range.of(Index.of(1, 2, 3), Extent.of(3, 3, 3));
        final var indexes = loopable(() -> forward(range, natural(range.dimensionality())));
        for (var index : indexes) {
            System.out.println(Arrays.toString(index));
        }
    }

    @Test(dataProvider = "ranges")
    public void iterableTest(Range range) {
        var indexes = loopable(() -> forward(range, natural(range.dimensionality())));
        assertThat(count(indexes.iterator())).isEqualTo(range.extent().elements());

        indexes = loopable(() -> backward(range, natural(range.dimensionality())));
        assertThat(count(indexes.iterator())).isEqualTo(range.extent().elements());

        indexes = loopable(() -> forward(range, reverse(range.dimensionality())));
        assertThat(count(indexes.iterator())).isEqualTo(range.extent().elements());

        indexes = loopable(() -> backward(range, reverse(range.dimensionality())));
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
        var indexes = loopable(() -> forward(range, reverse(range.dimensionality())));
        final var lowMajorForward = elements(indexes.iterator());

        indexes = loopable(() -> backward(range, reverse(range.dimensionality())));
        final var lowMajorBackward = elements(indexes.iterator());

        indexes = loopable(() -> forward(range, natural(range.dimensionality())));
        final var highMajorForward = elements(indexes.iterator());

        indexes = loopable(() -> backward(range, natural(range.dimensionality())));
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
        return RANGES;
    }

}
