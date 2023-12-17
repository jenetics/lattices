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

import java.util.Arrays;

import org.testng.annotations.Test;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class IndexIteratorTest {

    @Test
    public void lowMajorForward() {
        final var range = Range.of(Extent.of(2, 2, 2));

        final var it = new IndexIterator.Forward.LowMajor(range);
        final var it2 = IdxIterator.LowMajor.forward(range);
        while (it.hasNext()) {
            final var idx = it.next();
            assertThat(it2.next()).isEqualTo(idx);
            System.out.println(Arrays.toString(idx));
        }
    }

    @Test
    public void lowMajorBackward() {
        final var range = Range.of(Extent.of(2, 2, 2));

        final var it = new IndexIterator.Backward.LowMajor(range);
        final var it2 = IdxIterator.LowMajor.backward(range);
        while (it.hasNext()) {
            final var idx = it.next();
            assertThat(it2.next()).isEqualTo(idx);
            System.out.println(Arrays.toString(idx));
        }
    }

    @Test
    public void highMajorForward() {
        final var range = Range.of(Extent.of(2, 2, 2));

        final var it = new IndexIterator.Forward.HighMajor(range);
        final var it2 = IdxIterator.HighMajor.forward(range);
        while (it.hasNext()) {
            final var idx = it.next();
            assertThat(it2.next()).isEqualTo(idx);
            System.out.println(Arrays.toString(idx));
        }
    }

    @Test
    public void highMajorBackward() {
        final var range = Range.of(Extent.of(2, 2, 2));

        final var it = new IndexIterator.Backward.HighMajor(range);
        final var it2 = IdxIterator.HighMajor.backward(range);
        while (it.hasNext()) {
            final var idx = it.next();
            assertThat(it2.next()).isEqualTo(idx);
            System.out.println(Arrays.toString(idx));
        }
    }

}
