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

import io.jenetics.lattices.structure.Index1dIterator;
import io.jenetics.lattices.structure.Range1d;
import io.jenetics.lattices.structure.Stride1d;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class Index1dIteratorTest {

    @Test
    public void iterateStride1() {
        final var range = new Range1d(23, 120);
        final var stride = new Stride1d(1);
        final var it = new Index1dIterator(range, stride);

        int calls = 0;
        for (int i = range.start().value();
             i < range.start().value() + range.extent().size();
             i += stride.value())
        {
            ++calls;
            assertThat(it.next().value()).isEqualTo(i);
        }

        assertThat(it.hasNext()).isFalse();
        assertThat(calls).isEqualTo(range.extent().size()/stride.value());
    }

    @DataProvider
    public Object[][] parameters() {
        return new Object[][] {
            { new Range1d(0, 101), new Stride1d(1) },
            { new Range1d(5, 77), new Stride1d(1) },
            { new Range1d(11, 928), new Stride1d(1) },
            { new Range1d(0, 101), new Stride1d(2) },
            { new Range1d(5, 77), new Stride1d(2) },
            { new Range1d(11, 928), new Stride1d(2) },
            { new Range1d(0, 101), new Stride1d(3) },
            { new Range1d(5, 77), new Stride1d(3) },
            { new Range1d(11, 928), new Stride1d(3) },
            { new Range1d(0, 101), new Stride1d(4) },
            { new Range1d(5, 77), new Stride1d(4) },
            { new Range1d(11, 928), new Stride1d(4) },
            { new Range1d(0, 101), new Stride1d(5) },
            { new Range1d(5, 77), new Stride1d(5) },
            { new Range1d(11, 928), new Stride1d(5) },
            { new Range1d(0, 101), new Stride1d(11) },
            { new Range1d(5, 77), new Stride1d(11) },
            { new Range1d(11, 928), new Stride1d(11) },
        };
    }

}
