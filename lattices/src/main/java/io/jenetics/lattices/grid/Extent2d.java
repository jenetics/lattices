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
 * The extent of 2-d structures.
 *
 * @param rows the number of rows
 * @param cols the number of columns
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
public record Extent2d(int rows, int cols) {

    public Extent2d {
        if (rows < 0) {
            throw new IllegalArgumentException(
                "Number of rows must greater or equal than start: " + rows
            );
        }
        if (cols < 0) {
            throw new IllegalArgumentException(
                "Number of columns must greater or equal than start: " + cols
            );
        }
    }

    public Extent2d(final Range2d range) {
        this(range.height(), range.width());
    }

    /**
     * The number of matrix elements (cells) a matrix with {@code this}
     * dimensions consists of.
     *
     * @return the number of cells for {@code this} matrix dimension
     */
    public int size() {
        return rows*cols;
    }

    @Override
    public String toString() {
        return "[%s x %s]".formatted(rows(), cols());
    }

}