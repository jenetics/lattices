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

import java.util.Iterator;

/**
 * The extent of 1-d structures.
 *
 * @param elements the number of elements must be greater or equal zero
 * @param bands the number of bands
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record Extent1d(int elements, int bands)
    implements Extent, Comparable<Extent1d>, Iterable<Index1d>
{

    /**
     * Create a new 1-d extent with the given size.
     *
     * @param elements the size of the extent
     * @param bands the number of bands
     *
     * @throws IllegalArgumentException if the {@code size} is smaller than zero
     */
    public Extent1d {
        if (elements < 0 || bands < 1 || Checks.multNotSave(elements, bands)) {
            throw new IllegalArgumentException(
                "Extent is out of bounds: [%d, bands=%d]."
                    .formatted(elements, bands)
            );
        }
    }

    /**
     * Create a new 1-d extent with the given size.
     *
     * @param elements the number of elements
     *
     * @throws IllegalArgumentException if the {@code size} is smaller than zero
     */
    public Extent1d(int elements) {
        this(elements, 1);
    }

    /**
     * Return the number of dimensions; always 1.
     *
     * @return 1
     */
    @Override
    public int dimensionality() {
        return 1;
    }

    @Override
    public int at(int dimension) {
        return switch (dimension) {
            case 0 -> elements;
            default -> throw new IndexOutOfBoundsException(
                "Dimension out of range [0..%d): %d."
                    .formatted(dimensionality(), dimension)
            );
        };
    }

    @Override
    public int compareTo(Extent1d other) {
        return Integer.compare(elements, other.elements);
    }

    @Override
    public String toString() {
        return "[%d, channels=%d]".formatted(elements, bands);
    }

    @Override
    public Iterator<Index1d> iterator() {
        return new Range1d(this ).iterator();
    }

}
