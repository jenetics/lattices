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

import java.util.Arrays;

/**
 * Implementation of a <em>dense</em> array of {@code double} values.
 *
 * @param elements the underlying {@code double} element values
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record DenseDoubleArray(double[] elements) implements DoubleArray {

    @Override
    public double get(final int index) {
        return elements[index];
    }

    @Override
    public void set(final int index, final double value) {
        elements[index] = value;
    }

    @Override
    public int length() {
        return elements.length;
    }

    @Override
    public DenseDoubleArray copy() {
        return new DenseDoubleArray(elements.clone());
    }

    @Override
    public DoubleArray copy(final int start, final int length) {
        final var array = Arrays.copyOfRange(elements, start, start + length);
        return new DenseDoubleArray(array);
    }

    @Override
    public DoubleArray like(final int length) {
        return ofSize(length);
    }

    /**
     * Create a new dense {@code double} array with the given {@code length}.
     *
     * @param length the length of the created array
     * @return a new dense {@code double} array with the given {@code length}
     */
    public static DenseDoubleArray ofSize(final int length) {
        return new DenseDoubleArray(new double[length]);
    }

    @Override
    public String toString() {
        return Arrays.toString(elements);
    }

}
