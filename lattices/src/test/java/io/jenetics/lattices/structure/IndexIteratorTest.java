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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class IndexIteratorTest {

    @Test(dataProvider = "ranges")
    public void iteratorElements(Range range) {
        assertThat(count(IndexIterator.LowMajor.forward(range)))
            .isEqualTo(range.extent().elements());

        assertThat(count(IndexIterator.LowMajor.backward(range)))
            .isEqualTo(range.extent().elements());

        assertThat(count(IndexIterator.HighMajor.forward(range)))
            .isEqualTo(range.extent().elements());

        assertThat(count(IndexIterator.HighMajor.backward(range)))
            .isEqualTo(range.extent().elements());
    }

    private static int count(Iterator<Index> it) {
        final var elements = new HashSet<>();
        while (it.hasNext()) {
            final var element = it.next();
            elements.add(element);
        }
        return elements.size();
    }

    @Test(dataProvider = "ranges")
    public void iteratorOrder(Range range) {
        final var lowMajorForward = elements(IndexIterator.LowMajor.forward(range));
        final var lowMajorBackward = elements(IndexIterator.LowMajor.backward(range));
        final var highMajorForward = elements(IndexIterator.HighMajor.forward(range));
        final var highMajorBackward = elements(IndexIterator.HighMajor.backward(range));

        // Check order between forward and backward
        if (range.extent().elements() > 1) {
            assertThat(lowMajorForward).isNotEqualTo(lowMajorBackward);
            assertThat(highMajorForward).isNotEqualTo(highMajorBackward);
        }
        assertThat(lowMajorForward.reversed()).isEqualTo(lowMajorBackward);
        assertThat(highMajorForward.reversed()).isEqualTo(highMajorBackward);
    }

    private static List<Index> elements(Iterator<Index> it) {
        final var list = new ArrayList<Index>();
        while (it.hasNext()) {
            list.add(it.next());
        }
        return list;
    }

    @DataProvider
    public Object[][] ranges() {
        return new Object[][] {
            { Range.of(Extent.of(1)) },
            { Range.of(Extent.of(5)) },
            { Range.of(Extent.of(17)) },

            { Range.of(Extent.of(1, 1)) },
            { Range.of(Extent.of(5, 6)) },
            { Range.of(Extent.of(7, 6)) },

            { Range.of(Extent.of(1, 1, 1)) },
            { Range.of(Extent.of(5, 6, 7)) },
            { Range.of(Extent.of(7, 6, 10)) },

            { Range.of(Extent.of(1, 1, 1, 1)) },
            { Range.of(Extent.of(5, 6, 7, 9)) },
            { Range.of(Extent.of(7, 6, 10, 9)) },

            { Range.of(Extent.of(1, 1, 1, 1, 1)) },
            { Range.of(Extent.of(5, 6, 7, 9, 17)) },
            { Range.of(Extent.of(7, 6, 10, 9, 3)) },

            { Range.of(Extent.of(1, 1, 1, 1, 1, 1)) },
            { Range.of(Extent.of(5, 6, 7, 9, 17, 11)) },
            { Range.of(Extent.of(7, 6, 10, 9, 3, 7)) }
        };
    }

}
