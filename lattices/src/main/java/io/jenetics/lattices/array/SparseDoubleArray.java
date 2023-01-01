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

/**
 * Implementation of a <em>sparse</em> array of {@code double} values.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public class SparseDoubleArray implements DoubleArray {

    @Override
    public double get(final int index) {
        return 0;
    }

    @Override
    public void set(final int index, final double value) {

    }

    @Override
    public int length() {
        return 0;
    }

    @Override
    public SparseDoubleArray copy(final int start, final int length) {
        return null;
    }

    @Override
    public SparseDoubleArray like(final int length) {
        return null;
    }

}
