/*
 * Java Linear Algebra Library (@__identifier__@).
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
package io.jenetics.linealgebra.array;

import java.util.Arrays;

/**
 * Implementation of a <em>dense</em> array of {@code int} values.
 *
 * @param elements the underlying {@code int} element values
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
public record DenseIntArray(int[] elements) implements IntArray {

    @Override
    public int get(final int index) {
        return elements[index];
    }

    @Override
    public void set(final int index, final int value) {
        elements[index] = value;
    }

    @Override
    public int length() {
        return elements.length;
    }

    @Override
    public DenseIntArray copy() {
        return new DenseIntArray(elements.clone());
    }

    @Override
    public DenseIntArray copy(final int start, final int length) {
        final var array = Arrays.copyOfRange(elements, start, start + length);
        return new DenseIntArray(array);
    }

    @Override
    public DenseIntArray like(final int length) {
        return ofSize(length);
    }

    /**
     * Create a new dense {@code int} array with the given {@code length}.
     *
     * @param length the length of the created array
     * @return a new dense {@code int} array with the given {@code length}
     */
    public static DenseIntArray ofSize(final int length) {
        return new DenseIntArray(new int[length]);
    }

    @Override
    public String toString() {
        return Arrays.toString(elements);
    }

}
