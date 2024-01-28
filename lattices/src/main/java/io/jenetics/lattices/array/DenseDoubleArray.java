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

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Implementation of a <em>dense</em> array of {@code double} values. This is
 * <em>just</em> a wrapper around the underlying {@code double[]} array and no
 * values are copied.
 *
 * @param elements the underlying {@code double} element values
 * @param from the index of the first array element (inclusively)
 * @param length the length of the subarea
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record DenseDoubleArray(double[] elements, int from, int length)
    implements Array.OfDouble, Array.Dense<double[], DenseDoubleArray>
{

    /**
     * Create a new <em>dense</em> double array with the given values
     *
     * @param elements the underlying {@code double} element values
     * @param from the index of the first array element (inclusively)
     * @param length the length of the subarray
     * @throws IndexOutOfBoundsException if the given {@code from} value and
     *         {@code length} is out of bounds
     */
    public DenseDoubleArray {
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
    public DenseDoubleArray(double[] elements, int from) {
        this(elements, from, elements.length - from);
    }

    /**
     * Create a new <em>dense</em> array of {@code double} values.
     *
     * @param elements the underlying {@code double} element values
     */
    public DenseDoubleArray(double... elements) {
        this(elements, 0, elements.length);
    }

    @Override
    public double get(int index) {
        return elements[index + from];
    }

    @Override
    public void set(int index, double value) {
        elements[index + from] = value;
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public DenseDoubleArray copy() {
        final var elems = Arrays.copyOfRange(elements, from, from + length);
        return new DenseDoubleArray(elems);
    }

    @Override
    public DenseDoubleArray copy(int from, int length) {
        final var array = Arrays.copyOfRange(
            elements,
            from + this.from, from + this.from + length
        );
        return new DenseDoubleArray(array);
    }

    @Override
    public DenseDoubleArray like(final int length) {
        return ofLength(length);
    }

    @Override
    public String toString() {
        return stream()
            .mapToObj(Double::toString)
            .collect(Collectors.joining(", ", "[", "]"));
    }

    /**
     * Create a new dense {@code double} array with the given {@code length}.
     *
     * @param length the length of the created array
     * @return a new dense {@code double} array with the given {@code length}
     */
    public static DenseDoubleArray ofLength(int length) {
        return new DenseDoubleArray(new double[length]);
    }

}
