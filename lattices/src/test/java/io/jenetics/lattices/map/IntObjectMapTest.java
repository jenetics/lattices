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
package io.jenetics.lattices.map;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.function.IntPredicate;
import java.util.random.RandomGenerator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.testng.annotations.Test;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class IntObjectMapTest {

    private static final int ENTRIES = 100;
    private static final int MIN_KEY = 10_000_000;
    private static final int MAX_KEY = 100_000_000;
    private static final double MIN_VALUE = 10;
    private static final double MAX_VALUE = 10_000;

    private static final int[] KEYS = RandomGenerator.getDefault()
        .ints(MIN_KEY, MAX_KEY)
        .distinct()
        .limit(ENTRIES)
        .toArray();

    private static final Double[] VALUES = RandomGenerator.getDefault()
        .doubles(MIN_VALUE, MAX_VALUE)
        .limit(ENTRIES)
        .boxed()
        .toArray(Double[]::new);

    static IntObjectMap<Double> newMap(int[] keys, Double[] values) {
        final var map = new IntObjectMap<Double>();
        for (int i = 0; i < keys.length; ++i) {
            map.put(keys[i], values[i]);
        }
        return map;
    }

    @Test
    public void putAndGet() {
        final var map = newMap(KEYS, VALUES);
        assertThat(map.size()).isEqualTo(KEYS.length);
        for (int i = 0; i < KEYS.length; ++i) {
            assertThat(map.get(KEYS[i])).isEqualTo(VALUES[i]);
        }

        map.put(KEYS[50], VALUES[2]);
        assertThat(map.get(KEYS[50])).isEqualTo(VALUES[2]);
    }

    @Test
    public void clear() {
        final var map = newMap(KEYS, VALUES);
        assertThat(map.size()).isEqualTo(KEYS.length);

        map.clear();
        assertThat(map.size()).isEqualTo(0);
    }

    @Test
    public void isEmpty() {
        final var map = newMap(new int[0], null);
        assertThat(map.isEmpty()).isTrue();

        map.put(1, 1.0);
        assertThat(map.isEmpty()).isFalse();
    }

    @Test
    public void containsKey() {
        final var map = newMap(KEYS, VALUES);
        for (var key : KEYS) {
            assertThat(map.containsKey(key)).isTrue();
        }
        assertThat(map.containsKey(MAX_KEY + 1)).isFalse();
    }

    @Test
    public void forEachKey() {
        final var map = newMap(KEYS, VALUES);

        final var keys = IntStream.builder();
        map.forEachKey(keys);
        assertThat(keys.build().toArray()).containsExactlyInAnyOrder(KEYS);
    }

    @Test
    public void keys() {
        final var map = newMap(KEYS, VALUES);
        assertThat(map.keys().toArray()).containsExactlyInAnyOrder(KEYS);
    }

    @Test
    public void remove() {
        final var map = newMap(KEYS, VALUES);
        assertThat(map.containsKey(KEYS[50])).isTrue();

        map.remove(KEYS[50]);
        assertThat(map.containsKey(KEYS[50])).isFalse();
    }

    @Test
    public void size() {
        final var map = newMap(KEYS, VALUES);
        assertThat(map.size()).isEqualTo(KEYS.length);

        map.remove(KEYS[55]);
        assertThat(map.size()).isEqualTo(KEYS.length - 1);
    }

    @Test
    public void containsValue() {
        final var map = newMap(KEYS, VALUES);
        for (var value : VALUES) {
            assertThat(map.containsValue(value)).isTrue();
        }
        assertThat(map.containsValue(MAX_VALUE + 1)).isFalse();
    }

    @Test
    public void forEach() {
        final var map = newMap(KEYS, VALUES);

        final var keys = new ArrayList<>();
        map.forEach((k, v) -> {
            keys.add(k);
            assertThat(map.get(k)).isEqualTo(v);
        });

        assertThat(keys).containsExactlyInAnyOrderElementsOf(
            IntStream.of(KEYS).boxed().toList()
        );
    }

    @Test
    public void forEachValue() {
        final var map = newMap(KEYS, VALUES);

        final var values = new ArrayList<>();
        map.forEachValue(values::add);

        assertThat(values).containsExactlyInAnyOrderElementsOf(
            Stream.of(VALUES).toList()
        );
    }

    @Test
    public void getOrDefault() {
        final var map = newMap(KEYS, VALUES);
        assertThat(map.getOrDefault(KEYS[3], MAX_VALUE + 10))
            .isEqualTo(VALUES[3]);
        assertThat(map.getOrDefault(MAX_KEY + 1, MAX_VALUE + 1))
            .isEqualTo(MAX_VALUE + 1);
    }

    @Test
    public void update() {
        final var map = newMap(KEYS, VALUES);
        map.update((k, v) -> k*3.0);

        for (var key : KEYS) {
            assertThat(map.get(key)).isEqualTo(key*3.0);
        }
    }

    @Test
    public void values() {
        final var map = newMap(KEYS, VALUES);
        assertThat(map.values().toArray(Double[]::new))
            .containsExactlyInAnyOrder(VALUES);
    }

    @Test
    public void valuesFilter() {
        final var map = newMap(KEYS, VALUES);

        final IntPredicate filter = k -> k%2 == 0;
        final var expected = IntStream.of(KEYS)
            .filter(filter)
            .mapToObj(map::get)
            .toArray();

        assertThat(map.values(filter).toArray())
            .containsExactlyInAnyOrder(expected);
    }

}
