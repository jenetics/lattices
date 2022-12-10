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

import static java.util.Objects.requireNonNull;

import java.util.Optional;

/**
 * Represents the <em>row-major</em> order.
 *
 * @param start the index of the first element
 * @param stride the number of indexes between any two elements
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
public record StrideOrder1d(int start, int stride) implements Order1d {

    public static final StrideOrder1d DEFAULT = new StrideOrder1d(0, 1);

    @Override
    public int index(final int rank) {
        return start + rank*stride;
    }

    /**
     * Return the continuous copyable range from the given {@code rage}. If this
     * order doesn't represent a continuous array portion, {@link Optional#empty()}
     * is returned.
     *
     * @param range the desired range to be copied
     * @return the continuous copyable range
     */
    public Optional<Range1d> copyableRange(final Range1d range) {
        requireNonNull(range);

        if (stride == 1) {
            return Optional.of(new Range1d(
                range.index() + start(),
                range.size()
            ));
        } else {
            return Optional.empty();
        }
    }

}
