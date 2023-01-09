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
package io.jenetics.lattices.structure;

/**
 * The extent of 1-d structures.
 *
 * @param size the number of elements, must be greater or equal zero
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record Extent1d(int size) implements Comparable<Extent1d> {

    /**
     * Create a new 1-d extent with the given size.
     *
     * @param size the size of the extent
     * @throws IndexOutOfBoundsException if the {@code size} is smaller than zero
     */
    public Extent1d {
        if (size < 0) {
            throw new IndexOutOfBoundsException(
                "Extent is out of bounds: [%d].".formatted(size)
            );
        }
    }

    @Override
    public int compareTo(final Extent1d other) {
        return Integer.compare(size, other.size);
    }

    @Override
    public String toString() {
        return "[%d]".formatted(size());
    }

}
