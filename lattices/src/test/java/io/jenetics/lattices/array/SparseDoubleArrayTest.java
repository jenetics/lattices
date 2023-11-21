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
package io.jenetics.lattices.array;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.random.RandomGenerator;

import org.testng.annotations.Test;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class SparseDoubleArrayTest {

    final int[] indexes = RandomGenerator.getDefault()
        .ints(0, Integer.MAX_VALUE/2)
        .limit(10)
        .toArray();

    @Test
    public void setGet() {
        final var array = new SparseDoubleArray(Integer.MAX_VALUE);
        for (int j : indexes) {
            array.set(j, j);
        }

        assertThat(array.length()).isEqualTo(Integer.MAX_VALUE);
        for (var index : indexes) {
            assertThat(array.get(index)).isEqualTo(index);
        }
        for (int i = 10; i < 100; ++i) {
            assertThat(array.get(Integer.MAX_VALUE/2 + i)).isEqualTo(0);
        }
    }

    @Test
    public void indexes() {
        final var array = new SparseDoubleArray(Integer.MAX_VALUE);
        for (int j : indexes) {
            array.set(j, j);
        }

        assertThat(array.indexes().toArray()).containsExactlyInAnyOrder(indexes);
    }

}
