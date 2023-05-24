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

import static io.jenetics.lattices.structure.Structures.multNotSave;

import java.util.Iterator;

/**
 * The extent of 1-d structures.
 *
 * @param value the number of elements, must be greater or equal zero
 * @param bands the number of bands
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record Extent1d(int value, int bands)
    implements Comparable<Extent1d>, Iterable<Index1d>
{

    /**
     * Create a new 1-d extent with the given size.
     *
     * @param value the size of the extent
     * @param bands the number of bands
     *
     * @throws IllegalArgumentException if the {@code size} is smaller than zero
     */
    public Extent1d {
        if (value < 0 || bands < 1 || multNotSave(value, bands)) {
            throw new IllegalArgumentException(
                "Extent is out of bounds: [%d, bands=%d]."
                    .formatted(value, bands)
            );
        }
    }

    /**
     * Create a new 1-d extent with the given size.
     *
     * @param value the size of the extent
     *
     * @throws IllegalArgumentException if the {@code size} is smaller than zero
     */
    public Extent1d(int value) {
        this(value, 1);
    }

    /**
     * The number of elements of the structure.
     *
     * @return the number of cells of the structure
     */
    public int size() {
        return value;
    }

    /**
     * Return the length of the array, needed for storing all cells:
     * {@code size()*channels}.
     *
     * @return the array length needed for storing all cells
     */
    public int length() {
        return size()* bands;
    }

    @Override
    public int compareTo(Extent1d other) {
        return Integer.compare(value, other.value);
    }

    @Override
    public String toString() {
        return "[%d, channels=%d]".formatted(value, bands);
    }

    @Override
    public Iterator<Index1d> iterator() {
        return new Index1dIterator(new Range1d(this ), Stride1d.ONE) ;
    }

}
