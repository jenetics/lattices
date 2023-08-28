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

import java.util.random.RandomGenerator;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.jenetics.lattices.Index1dRandom;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class Layout1dTest {

    private final Index1dRandom random =
        new Index1dRandom(RandomGenerator.getDefault());

    @Test(dataProvider = "layouts")
    public void indexOffset(Layout1d layout) {
        final var range = new Range1d(0, 1_000_000);
        for (int i = 0; i < 100; ++i) {
            final Index1d index = random.next(range);

            final int offset = layout.offset(index);
            assertThat(offset).isGreaterThan(0);
            assertThat(layout.index(offset)).isEqualTo(index);
        }
    }

    @DataProvider
    public Object[][] layouts() {
        return new Object[][] {
            { new Layout1d(new Index1d(0), new Stride1d(1), Band.ZERO) },
            { new Layout1d(new Index1d(0), new Stride1d(2), Band.ZERO) },
            { new Layout1d(new Index1d(0), new Stride1d(3), Band.ZERO) },
            { new Layout1d(new Index1d(0), new Stride1d(5), Band.ZERO) },
            { new Layout1d(new Index1d(0), new Stride1d(11), Band.ZERO) },
            { new Layout1d(new Index1d(7), new Stride1d(1), Band.ZERO) },
            { new Layout1d(new Index1d(11), new Stride1d(17), Band.ZERO) }
        };
    }

}
