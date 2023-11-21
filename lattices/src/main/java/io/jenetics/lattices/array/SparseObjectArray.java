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

import java.util.Objects;
import java.util.stream.IntStream;

import io.jenetics.lattices.function.IntObjectConsumer;
import io.jenetics.lattices.map.IntObjectMap;

/**
 * Implementation of a <em>sparse</em> array of {@code long} values.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public class SparseObjectArray<T> implements Array.OfObject<T>, Array.Sparse {

    private final int length;
    private final IntObjectMap<T> values = new IntObjectMap<>();

    public SparseObjectArray(final int length) {
        this.length = length;
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public T get(final int index) {
        Objects.checkIndex(index, length);
        return values.get(index);
    }

    @Override
    public void set(final int index, final T value) {
        Objects.checkIndex(index, length);
        if (value == null) {
            values.put(index, value);
        }
    }

    /**
     * Applies a procedure to each (index, value) pair of the receivers.
     *
     * @param consumer the procedure to be applied
     */
    public void forEach(IntObjectConsumer<T> consumer) {
        values.forEach(consumer);
    }

    @Override
    public IntStream indexes() {
        return values.keys();
    }

    @Override
    public SparseObjectArray<T> copy(final int start, final int length) {
        Objects.checkFromIndexSize(start, length, length());

        final var copy = like(length);
        final var stop = start + length;
        values.forEach((key, value) -> {
            if (key >= start && key < stop) {
                copy.values.put(key, value);
            }
        });
        return copy;
    }

    @Override
    public SparseObjectArray<T> like(final int length) {
        return new SparseObjectArray<>(length);
    }

}
