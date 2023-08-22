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
 * The extent of 2-d structures.
 *
 * @param nrows the number of rows must be greater or equal zero
 * @param ncols the number of columns must be greater or equal zero
 * @param nbands the number of bands
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record Extent2d(int nrows, int ncols, int nbands)
    implements Iterable<Index2d>
{

    /**
     * Create a new 2-d extent.
     *
     * @param nrows the number of rows
     * @param ncols the number of cols
     * @param nbands the number of bands
     * @throws IllegalArgumentException if one of the arguments is smaller than
     *         zero or {@code rows*cols*channels > Integer.MAX_VALUE}
     */
    public Extent2d {
        if (nrows < 0 || ncols < 0 || nbands < 1 ||
            multNotSave(nrows, ncols, nbands))
        {
            throw new IllegalArgumentException(
                "Extent is out of bounds: [%d, %d, bands=%d]."
                    .formatted(nrows, ncols, nbands)
            );
        }
    }

    /**
     * Create a new 2-d extent.
     *
     * @param rows the number of rows
     * @param cols the number of cols
     * @throws IllegalArgumentException if one of the arguments is smaller than
     *         zero or {@code rows*cols > Integer.MAX_VALUE}
     */
    public Extent2d(int rows, int cols) {
        this(rows, cols, 1);
    }

    /**
     * The number of elements.
     *
     * @return the number of elements
     */
    public int nelements() {
        return nrows*ncols;
    }

    /**
     * Return the length of the array, needed for storing all cells:
     * {@code size()*channels}.
     *
     * @return the array length needed for storing all cells
     */
    public int ncells() {
        return nelements()*nbands;
    }

    @Override
    public Iterator<Index2d> iterator() {
        return new Index2dIterator(new Range2d(this));
    }

    @Override
    public String toString() {
        return "[%d, %d]".formatted(nrows(), ncols());
    }

}
