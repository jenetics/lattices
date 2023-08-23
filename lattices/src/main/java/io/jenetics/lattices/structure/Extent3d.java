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
 * The extent of 3-d structures.
 *
 * @param nslices the number of slices must be greater or equal zero
 * @param nrows the number of rows must be greater or equal zero
 * @param ncols the number of columns must be greater or equal zero
 * @param nbands the number of bands
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version 3.0
 * @since 3.0
 */
public record Extent3d(int nslices, int nrows, int ncols, int nbands)
    implements Iterable<Index3d>
{

    /**
     * Create a new 3-d extent.
     *
     * @param nslices the number of slices must be greater or equal zero
     * @param nrows the number of rows must be greater or equal zero
     * @param ncols the number of columns must be greater or equal zero
     * @param nbands the number of bands
     * @throws IllegalArgumentException if one of the arguments is smaller than
     *         zero or {@code slices*rows*cols*channels > Integer.MAX_VALUE}
     */
    public Extent3d {
        if (nslices < 0 || nrows < 0 || ncols < 0 || nbands < 1 ||
            Checks.multNotSave(nslices, nrows, ncols, nbands))
        {
            throw new IllegalArgumentException(
                "Extent is out of bounds: [%d, %d, %d, bands=%d]."
                    .formatted(nslices, nrows, ncols, nbands)
            );
        }
    }

    /**
     * Create a new 3-d extent.
     *
     * @param nslices the number of slices must be greater or equal zero
     * @param nrows the number of rows must be greater or equal zero
     * @param ncols the number of columns must be greater or equal zero
     * @throws IllegalArgumentException if one of the arguments is smaller than
     *         zero or {@code slices*rows*cols > Integer.MAX_VALUE}
     */
    public Extent3d(int nslices, int nrows, int ncols) {
        this(nslices, nrows, ncols, 1);
    }

    /**
     * The number of elements.
     *
     * @return the number of elements
     */
    public int nelements() {
        return nslices*nrows*ncols;
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
    public Iterator<Index3d> iterator() {
        return new Index3dIterator(new Range3d(this));
    }

    @Override
    public String toString() {
        return "[%d, %d, %d]".formatted(nslices(), nrows(), ncols());
    }

}
