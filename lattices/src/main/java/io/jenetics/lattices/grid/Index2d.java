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
package io.jenetics.lattices.grid;

/**
 * Represents a 2-d index.
 *
 * @param row the row index
 * @param col the column index
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record Index2d(int row, int col) {

    /**
     * Index where row and column is zero.
     */
    public static final Index2d ZERO = new Index2d(0, 0);

    /**
     * Return a new range from {@code this} <em>to</em> {@code end}.
     *
     * @param end the end index of the created range, exclusively
     * @return a new range from {@code this} <em>to</em> {@code end}
     * @throws IllegalArgumentException if {@code this >= end}
     */
    public Range2d to(final Index2d end) {
        return new Range2d(this, end);
    }

}
