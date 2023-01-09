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
package io.jenetics.lattices.grid;

import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.Test;

import io.jenetics.lattices.structure.Order1d;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class Order1DTest {

    @Test
    public void indexDefaultStride() {
        final var order = Order1d.DEFAULT;

        for (int i = -10; i < 10; ++i) {
            assertThat(order.index(i)).isEqualTo(i);
        }
    }

    @Test
    public void indexNonZeroStart() {
        final var start = 34;
        final var order = new Order1d(start, 1);

        for (int i = -10; i < 10; ++i) {
            assertThat(order.index(i)).isEqualTo(i + start);
        }
    }

    @Test
    public void indexWithStride() {
        final var start = -10;
        final var stride = 34;
        final var order = new Order1d(start, stride);

        for (int i = -10; i < 10; ++i) {
            assertThat(order.index(i)).isEqualTo(start + i*stride);
        }
    }

}
