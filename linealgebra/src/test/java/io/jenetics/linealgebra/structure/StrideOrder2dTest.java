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
public class StrideOrder2dTest {

    @Test
    public void indexExtent() {
        final var extent = new Extent2d(23, 43);
        final var order = new StrideOrder2d(extent);

        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < 5; ++j) {
                final var idx = order.index(i, j);
                assertThat(idx).isEqualTo(i*extent.cols() + j);
            }
        }
    }

    @Test
    public void transpose() {
        final var order = new StrideOrder2d(34, 87, new Extent2d(87, 23));
        final var torder = order.transpose();

        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < 5; ++j) {
                assertThat(torder.index(i, j)).isEqualTo(order.index(j, i));
            }
        }
    }

}
