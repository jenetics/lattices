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
 * This interface defines the array offset for a given 3-d index.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public interface Mapper3d extends Mapper {

    /**
     * Return the position of the given coordinate within the (virtual or
     * non-virtual) internal 1-d array.
     *
     * @param slice the slice index
     * @param row the row index
     * @param col the column index
     * @return the (linearized) index of the given {@code slice}, {@code row}
     *         and {@code col}
     */
    int offset(int slice, int row, int col);

    /**
     * Return the <em>array</em> index from the given <em>dimensional</em> index.
     *
     * @see #offset(int, int, int)
     *
     * @param index the dimensional index
     * @return the array index
     */
    default int offset(Index3d index) {
        return offset(index.slice(), index.row(), index.col());
    }

    /**
     * Calculates the index for the given {@code offset}. This is the
     * <em>inverse</em> operation of the {@link #offset(Index3d)} method.
     *
     * @param offset the offset for which to calculate the index
     * @return the index for the given {@code offset}
     */
    @Override
    Index3d index(int offset);

}
