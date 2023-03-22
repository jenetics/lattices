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
 * The extent of 2-d structures.
 *
 * @param rows the number of rows, must be greater or equal zero
 * @param cols the number of columns, must be greater or equal zero
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record Extent2d(int rows, int cols) implements Iterable<Index2d> {

    /**
     * Create a new 2-d extent.
     *
     * @param rows the number of rows
     * @param cols the number of cols
     * @throws IndexOutOfBoundsException if one of the arguments is smaller than
     *         zero or {@code rows*cols > Integer.MAX_VALUE}
     */
    public Extent2d {
        if (rows < 0 || cols < 0 || multNotSave(rows, cols)) {
            throw new IndexOutOfBoundsException(
                "Extent is out of bounds: [%d, %d].".formatted(rows, cols)
            );
        }
    }

    private static boolean multNotSave(int x, int y) {
        long r = (long)x*(long)y;
        return (int)r != r;
    }

    /**
     * The number of elements of the structure.
     *
     * @return the number of cells of the structure
     */
    public int size() {
        return rows*cols;
    }

    @Override
    public Iterator<Index2d> iterator() {
        return new Index2dIterator(new Range2d(this));
    }

    @Override
    public String toString() {
        return "[%d, %d]".formatted(rows(), cols());
    }

}
