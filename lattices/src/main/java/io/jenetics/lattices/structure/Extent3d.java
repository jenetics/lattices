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
 * The extent of 3-d structures.
 *
 * @param slices the number of slices, must be greater or equal zero
 * @param rows the number of rows, must be greater or equal zero
 * @param cols the number of columns, must be greater or equal zero
 * @param channels the number of channels
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version 3.0
 * @since 3.0
 */
public record Extent3d(int slices, int rows, int cols, int channels)
    implements Iterable<Index3d>
{

    /**
     * Create a new 3-d extent.
     *
     * @param slices the number of slices, must be greater or equal zero
     * @param rows the number of rows, must be greater or equal zero
     * @param cols the number of columns, must be greater or equal zero
     * @throws IllegalArgumentException if one of the arguments is smaller than
     *         zero or {@code slices*rows*cols > Integer.MAX_VALUE}
     */
    public Extent3d(int slices, int rows, int cols) {
        this(slices, rows, cols, 1);
    }

    /**
     * Create a new 3-d extent.
     *
     * @param slices the number of slices, must be greater or equal zero
     * @param rows the number of rows, must be greater or equal zero
     * @param cols the number of columns, must be greater or equal zero
     * @param channels the number of channels
     * @throws IllegalArgumentException if one of the arguments is smaller than
     *         zero or {@code slices*rows*cols*channels > Integer.MAX_VALUE}
     */
    public Extent3d {
        if (slices < 0 || rows < 0 || cols < 0 || channels < 1 ||
            multNotSave(slices, rows, cols, channels))
        {
            throw new IllegalArgumentException(
                "Extent is out of bounds: [%d, %d, %d, channels=%d]."
                    .formatted(slices, rows, cols, channels)
            );
        }
    }

    /**
     * The number of elements of the structure.
     *
     * @return the number of cells of the structure
     */
    public int size() {
        return slices*rows*cols;
    }

    /**
     * Return the length of the array, needed for storing all cells:
     * {@code size()*channels}.
     *
     * @return the array length needed for storing all cells
     */
    public int length() {
        return size()*channels;
    }

    @Override
    public Iterator<Index3d> iterator() {
        return new Index3dIterator(new Range3d(this));
    }

    @Override
    public String toString() {
        return "[%d, %d, %d]".formatted(slices(), rows(), cols());
    }

}
