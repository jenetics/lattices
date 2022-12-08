/*
 * Java Linear Algebra Library (@__identifier__@).
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
package io.jenetics.linealgebra.structure;

/**
 * Represents a <em>grid</em> range with the given parameters.
 *
 * @param index the start index of the range
 * @param size the size of the range
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
public record Range1d(int index, int size) {

    public Range1d {
        if (index < 0 || size < 0) {
            throw new IllegalArgumentException(
                "Invalid range [%d, %d].".formatted(index, size)
            );
        }
    }

    /**
     * Create a new range from the given extent. The start indices ({@link #index}
     * is set to zero.
     *
     * @param extent the extent of the new range
     */
    public Range1d(final Extent1d extent) {
        this(0, extent.size());
    }

}