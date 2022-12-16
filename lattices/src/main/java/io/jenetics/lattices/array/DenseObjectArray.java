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

import static java.util.Objects.requireNonNull;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Implementation of a <em>dense</em> array of {@code Object} values.
 *
 * @param elements the underlying {@code Object} element values
 * @param <T> the value type
 */
public record DenseObjectArray<T>(T[] elements) implements ObjectArray<T> {

    public DenseObjectArray {
        requireNonNull(elements);
    }

    @Override
    public T get(final int index) {
        return elements[index];
    }

    @Override
    public void set(final int index, final T value) {
        elements[index] = value;
    }

    @Override
    public int length() {
        return elements.length;
    }

    @Override
    public ObjectArray<T> copy() {
        return new DenseObjectArray<>(elements.clone());
    }

    @Override
    public ObjectArray<T> copy(final int start, final int length) {
        final var array = Arrays.copyOfRange(elements, start, start + length);
        return new DenseObjectArray<>(array);
    }

    @Override
    public ObjectArray<T> like(final int length) {
        return ofSize(length);
    }

    /**
     * Create a new dense {@code int} array with the given {@code length}.
     *
     * @param length the length of the created array
     * @param __ not used (Java trick for getting "reified" element type)
     * @return a new dense {@code int} array with the given {@code length}
     */
    @SafeVarargs
    public static <T> ObjectArray<T> ofSize(final int length, final T... __) {
        @SuppressWarnings("unchecked")
        final T[] elements = (T[])Array
            .newInstance(__.getClass().getComponentType(), length);
        return new DenseObjectArray<>(elements);
    }

    @Override
    public String toString() {
        return Arrays.toString(elements);
    }

}
