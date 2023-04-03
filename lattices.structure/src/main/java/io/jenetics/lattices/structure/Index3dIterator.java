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

import static java.util.Objects.requireNonNull;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
final class Index3dIterator implements Iterator<Index3d> {

    private final Range3d range;

    private int sliceCurser;
    private int rowCursor;
    private int colCursor;

    Index3dIterator(Range3d range) {
        this.range = requireNonNull(range);

        sliceCurser = range.start().slice();
        rowCursor = range.start().row();
        colCursor = range.start().col();
    }

    @Override
    public boolean hasNext() {
        return
            sliceCurser < range.start().slice() + range.extent().slices() &&
            rowCursor < range.start().row() + range.extent().rows() &&
            colCursor < range.start().col() + range.extent().cols();
    }

    @Override
    public Index3d next() {
        final int s = sliceCurser;
        final int r = rowCursor;
        final int c = colCursor;

        if (sliceCurser >= range.start().slice() + range.extent().slices() &&
            rowCursor >= range.start().row() + range.extent().rows() &&
            colCursor >= range.start().col() + range.extent().cols())
        {
            throw new NoSuchElementException();
        }

        colCursor = c + 1;
        if (colCursor >= range.start().col() + range.extent().cols()) {
            colCursor = range.start().col();
            rowCursor = r + 1;

            if (rowCursor >= range.start().row() + range.extent().rows()) {
                colCursor = range.start().col();
                rowCursor = range.start().row();
                sliceCurser = s + 1;
            }
        }

        return new Index3d(s, r, c);
    }
}
