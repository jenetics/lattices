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

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public class Index2dIteratorTest {

    @Test(dataProvider = "ranges")
    public void iterate(Range2d range) {
        final var it = new Index2dIterator(range);

        for (int r = 0; r < range.extent().rows(); ++r) {
            final var row = r + range.start().row();

            for (int c = 0; c < range.extent().cols(); ++c) {
                final var col = c + range.start().col();

                final var index = it.next();
                assertThat(index).isEqualTo(new Index2d(row, col));
            }
        }

        assertThat(it.hasNext()).isFalse();
    }

    @DataProvider
    public Object[][] ranges() {
        return new Object[][] {
            { new Range2d(new Index2d(0, 0), new Extent2d(17, 21)) },
            { new Range2d(new Index2d(2, 4), new Extent2d(20, 21)) }
        };
    }

}
