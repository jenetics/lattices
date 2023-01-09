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

import static java.util.Objects.checkFromIndexSize;
import static java.util.Objects.requireNonNull;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Implementation of a <em>dense</em> array of {@code Object} values.
 *
 * @param elements the underlying {@code Object} element values
 * @param from the index of the first array element (inclusively)
 * @param length the length of the sub-array
 * @param <T> the value type
 */
public record DenseObjectArray<T>(T[] elements, int from, int length)
    implements ObjectArray<T>
{

    /**
     * Create a new <em>dense</em> double array with the given values
     *
     * @param elements the underlying {@code double} element values
     * @param from the index of the first array element (inclusively)
     * @param length the length of the sub-array
     * @throws IndexOutOfBoundsException if the given {@code from} value and
     *         {@code length} is out of bounds
     */
    public DenseObjectArray {
        requireNonNull(elements);
        checkFromIndexSize(from, length, elements.length);
    }

    /**
     * Create a new <em>dense</em> double array with the given values
     *
     * @param elements the underlying {@code double} element values
     * @param from the index of the first array element (inclusively)
     * @throws IndexOutOfBoundsException if the given {@code from} value is out
     *         of bounds
     */
    public DenseObjectArray(final T[] elements, int from) {
        this(elements, from, elements.length - from);
    }

    /**
     * Create a new <em>dense</em> array of {@code double} values.
     *
     * @param elements the underlying {@code double} element values
     */
    public DenseObjectArray(final T[] elements) {
        this(elements, 0, elements.length);
    }

    @Override
    public T get(final int index) {
        return elements[index + from];
    }

    @Override
    public void set(final int index, final T value) {
        elements[index + from] = value;
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public DenseObjectArray<T> copy() {
        final var elems = Arrays.copyOfRange(elements, from, from + length);
        return new DenseObjectArray<>(elems);
    }

    @Override
    public DenseObjectArray<T> copy(final int start, final int length) {
        final var array = Arrays.copyOfRange(
            elements,
            start + from, start + from + length
        );
        return new DenseObjectArray<>(array);
    }

    @Override
    public DenseObjectArray<T> like(final int length) {
        return ofSize(length);
    }

    /**
     * Return a double stream from the given array.
     *
     * @return a double stream from the given array
     */
    public Stream<T> stream() {
        return IntStream.range(0, length())
            .mapToObj(this::get);
    }

    @Override
    public String toString() {
        return stream()
            .map(Objects::toString)
            .collect(Collectors.joining(", ", "[", "]"));
    }

    /**
     * Create a new dense {@code int} array with the given {@code length}.
     *
     * @param length the length of the created array
     * @param __ not used (Java trick for getting "reified" element type)
     * @return a new dense {@code int} array with the given {@code length}
     */
    @SafeVarargs
    public static <T> DenseObjectArray<T> ofSize(final int length, final T... __) {
        @SuppressWarnings("unchecked")
        final T[] elements = (T[])Array
            .newInstance(__.getClass().getComponentType(), length);
        return new DenseObjectArray<>(elements);
    }

}
