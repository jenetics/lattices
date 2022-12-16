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
 * Represents a <em>grid</em> range with the given parameters.
 *
 * @param row the row where the range starts
 * @param col the column where the range starts
 * @param height the height of the range
 * @param width the size of the range
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record Range2d(int row, int col, int height, int width) {

    public Range2d {
        if (row < 0 || col < 0 || height < 0 || width < 0) {
            throw new IllegalArgumentException(
                "Invalid range: [%d..%d, %d..%d]"
                    .formatted(row, height, col, width)
            );
        }
    }

    /**
     * Create a new range from the given extent. The start indices ({@link #row}
     * and {@link #col()}) are set to zero.
     *
     * @param extent the extent of the new range
     */
    public Range2d(final Extent2d extent) {
        this(0, 0, extent.rows(), extent.cols());
    }

    public int size() {
        return height*width;
    }

    @Override
    public String toString() {
        return "[%d..%d, %d..%d]".formatted(row, height, col, width);
    }

}
