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

import java.util.stream.IntStream;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class StructureTest {

    @Test(dataProvider = "extents")
    public void layoutIndexFromOffset(Extent extent) {
        final var structure = Structure.of(extent);
        final var count = IntStream.range(0, structure.extent().elements())
            .mapToObj(offset -> structure.layout().index(offset*extent.bands()))
            .distinct()
            .count();

        assertThat(count).isEqualTo(structure.extent().elements());
    }

    @Test(dataProvider = "extents")
    public void layoutOffsetFromIndex(Extent extent) {
        final var structure = Structure.of(extent);

        int count = 0;
        for (var index : Range.iterable(Range.of(extent))) {
            final var offset = structure.layout().offset(index.toArray());

            assertThat(offset).isEqualTo(count*extent.bands());
            assertThat(structure.layout().index(offset)).isEqualTo(index);

            ++count;
        }
    }


    @DataProvider
    public Object[][] extents() {
        return new Object[][] {
            { Extent.of(new int[] {1}, 1) },
            { Extent.of(new int[] {5}, 1) },
            { Extent.of(new int[] {17}, 1) },
            { Extent.of(new int[] {1}, 2) },
            { Extent.of(new int[] {5}, 2) },
            { Extent.of(new int[] {17}, 2) },
            { Extent.of(new int[] {1}, 5) },
            { Extent.of(new int[] {5}, 5) },
            { Extent.of(new int[] {17}, 5) },

            { Extent.of(new int[] {1, 1}, 1) },
            { Extent.of(new int[] {5, 6}, 1) },
            { Extent.of(new int[] {7, 6}, 1) },
            { Extent.of(new int[] {1, 1}, 2) },
            { Extent.of(new int[] {5, 6}, 2) },
            { Extent.of(new int[] {7, 6}, 2) },
            { Extent.of(new int[] {1, 1}, 5) },
            { Extent.of(new int[] {5, 6}, 5) },
            { Extent.of(new int[] {7, 6}, 5) },

            { Extent.of(new int[] {1, 1, 1}, 1) },
            { Extent.of(new int[] {5, 6, 7}, 1) },
            { Extent.of(new int[] {7, 6, 10}, 1) },
            { Extent.of(new int[] {1, 1, 1}, 2) },
            { Extent.of(new int[] {5, 6, 7}, 2) },
            { Extent.of(new int[] {7, 6, 10}, 2) },
            { Extent.of(new int[] {1, 1, 1}, 5) },
            { Extent.of(new int[] {5, 6, 7}, 5) },
            { Extent.of(new int[] {7, 6, 10}, 5) },

            { Extent.of(new int[] {1, 1, 1, 1}, 1) },
            { Extent.of(new int[] {5, 6, 7, 9}, 1) },
            { Extent.of(new int[] {7, 6, 10, 9}, 1) },
            { Extent.of(new int[] {1, 1, 1, 1}, 2) },
            { Extent.of(new int[] {5, 6, 7, 9}, 2) },
            { Extent.of(new int[] {7, 6, 10, 9}, 2) },
            { Extent.of(new int[] {1, 1, 1, 1}, 5) },
            { Extent.of(new int[] {5, 6, 7, 9}, 5) },
            { Extent.of(new int[] {7, 6, 10, 9}, 5) },

            { Extent.of(new int[] {1, 1, 1, 1, 1}, 1) },
            { Extent.of(new int[] {5, 6, 7, 9, 17}, 1) },
            { Extent.of(new int[] {7, 6, 10, 9, 3}, 1) },
            { Extent.of(new int[] {1, 1, 1, 1, 1}, 2) },
            { Extent.of(new int[] {5, 6, 7, 9, 17}, 2) },
            { Extent.of(new int[] {7, 6, 10, 9, 3}, 2) },
            { Extent.of(new int[] {1, 1, 1, 1, 1}, 5) },
            { Extent.of(new int[] {5, 6, 7, 9, 17}, 5) },
            { Extent.of(new int[] {7, 6, 10, 9, 3}, 5) },

            { Extent.of(new int[] {1, 1, 1, 1, 1, 1}, 1) },
            { Extent.of(new int[] {5, 6, 7, 9, 17, 11}, 1) },
            { Extent.of(new int[] {7, 6, 10, 9, 3, 7}, 1) },
            { Extent.of(new int[] {1, 1, 1, 1, 1, 1}, 2) },
            { Extent.of(new int[] {5, 6, 7, 9, 17, 11}, 2) },
            { Extent.of(new int[] {7, 6, 10, 9, 3, 7}, 2) },
            { Extent.of(new int[] {1, 1, 1, 1, 1, 1}, 5) },
            { Extent.of(new int[] {5, 6, 7, 9, 17, 11}, 5) },
            { Extent.of(new int[] {7, 6, 10, 9, 3, 7}, 5) }
        };
    }

}
