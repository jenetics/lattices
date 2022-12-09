/*
 * Java Linear Algebra Library (@__identifier__@).
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
package io.jenetics.linealgebra.structure;

import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.Test;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class Structure1dTest {

    @Test
    public void viewRange() {
        final var struct = new Structure1d(new Extent1d(78));
        final var range = new Range1d(12, 13);
        final var view = struct.view(range);

        assertThat(view.extent().size()).isEqualTo(range.size());
        for (int i = 0; i < range.size(); ++i) {
            assertThat(view.order().index(i)).isEqualTo(i + range.index());
        }
    }

    @Test
    public void viewStride() {
        final var struct = new Structure1d(new Extent1d(78));
        final var stride = new Stride1d(2);
        final var view = struct.view(stride);

        assertThat(view.extent().size())
            .isEqualTo(struct.extent().size()/stride.stride());
        for (int i = 0; i < 7; ++i) {
            assertThat(view.order().index(i)).isEqualTo(i*stride.stride());
        }
    }

}
