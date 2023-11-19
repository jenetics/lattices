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

import java.util.HashMap;
import java.util.Map;
import java.util.random.RandomGenerator;

import org.testng.annotations.Test;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class IntDoubleHashMapTest {

    @Test
    public void putGet() {
        Map<String, String> mapadf;
        final var random = RandomGenerator.getDefault();
        final var map = new IntDoubleMap();
        //final var map = new HashMap<Integer, Double>();

        for (int i = 0; i < 20_000; ++i) {
            final var key = random.nextInt(
                Integer.MIN_VALUE/2,
                Integer.MAX_VALUE/2 - 10
            );

            final var value = random.nextDouble();

            map.put(key, value);
            assertThat(map.get(key)).isEqualTo(value);
            //assertThat(map.keyOf(value)).isEqualTo(key);
            assertThat(map.containsKey(key)).isTrue();
            assertThat(map.containsValue(value)).isTrue();
            assertThat(map.size()).isEqualTo(i + 1);

            assertThat(map.containsKey(random.nextInt(Integer.MAX_VALUE/2))).isFalse();
            assertThat(map.containsValue(random.nextDouble(2, 100))).isFalse();
        }
    }

}
