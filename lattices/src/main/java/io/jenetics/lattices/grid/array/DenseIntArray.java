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
package io.jenetics.lattices.grid.array;

import static java.util.Objects.checkFromIndexSize;
import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Implementation of a <em>dense</em> array of {@code int} values.
 *
 * @param elements the underlying {@code int} element values
 * @param from the index of the first array element (inclusively)
 * @param length the length of the sub-array
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record DenseIntArray(int[] elements, int from, int length)
    implements Array.OfInt
{

    /**
     * Create a new <em>dense</em> int array with the given values
     *
     * @param elements the underlying {@code int} element values
     * @param from the index of the first array element (inclusively)
     * @param length the length of the sub-array
     * @throws IndexOutOfBoundsException if the given {@code from} value and
     *         {@code length} is out of bounds
     */
    public DenseIntArray {
        requireNonNull(elements);
        checkFromIndexSize(from, length, elements.length);
    }

    /**
     * Create a new <em>dense</em> int array with the given values
     *
     * @param elements the underlying {@code int} element values
     * @param from the index of the first array element (inclusively)
     * @throws IndexOutOfBoundsException if the given {@code from} value is out
     *         of bounds
     */
    public DenseIntArray(int[] elements, int from) {
        this(elements, from, elements.length - from);
    }

    /**
     * Create a new <em>dense</em> array of {@code int} values.
     *
     * @param elements the underlying {@code int} element values
     */
    public DenseIntArray(int[] elements) {
        this(elements, 0, elements.length);
    }

    @Override
    public int get(int index) {
        return elements[index + from];
    }

    @Override
    public void set(int index, int value) {
        elements[index + from] = value;
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public DenseIntArray copy() {
        final var elems = Arrays.copyOfRange(elements, from, from + length);
        return new DenseIntArray(elems);
    }

    @Override
    public DenseIntArray copy(int start, int length) {
        final var array = Arrays.copyOfRange(
            elements,
            start + from, start + from + length
        );
        return new DenseIntArray(array);
    }

    @Override
    public DenseIntArray like(int length) {
        return ofSize(length);
    }

    /**
     * Return an int stream from the given array.
     *
     * @return an int stream from the given array
     */
    public IntStream stream() {
        return IntStream.range(0, length())
            .map(this::get);
    }

    @Override
    public String toString() {
        return stream()
            .mapToObj(Integer::toString)
            .collect(Collectors.joining(", ", "[", "]"));
    }

    /**
     * Create a new dense {@code int} array with the given {@code length}.
     *
     * @param length the length of the created array
     * @return a new dense {@code int} array with the given {@code length}
     */
    public static DenseIntArray ofSize(int length) {
        return new DenseIntArray(new int[length]);
    }

}
