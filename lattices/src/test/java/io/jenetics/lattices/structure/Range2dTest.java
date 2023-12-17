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

import org.testng.annotations.Test;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class Range2dTest {

    @Test
    public void iterator() {
        final var extent = new Extent2d(432, 43);
        final var range = new Range2d(extent);
        final var it = range.iterator();

        assertThat(it).isInstanceOf(IndexIterator.Forward.class);
        assertThat(it).isInstanceOf(IndexIterator.LowMajor.class);
        while (it.hasNext()) {
            final Index2d index = it.next();
            assertThat(index).isNotNull();
        }
    }

    @Test
    public void indexes() {
        final var extent = new Extent2d(432, 43);
        final var range = new Range2d(extent);
        assertThat(range.indexes().count()).isEqualTo(extent.elements());
    }

}
