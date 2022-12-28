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

/**
 * Defines the structure of a 1-d matrix, which is defined by the dimension of
 * the matrix and the index order of the underlying element array.
 *
 * @param extent the extent of the structure
 * @param order the element order
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record Structure1d(Extent1d extent, StrideOrder1d order) {

    public Structure1d {
        requireNonNull(extent);
        requireNonNull(order);
    }

    /**
     * Create a new matrix structure with the given dimension and the default
     * element order.
     *
     * @param extent the extent of the structure
     */
    public Structure1d(final Extent1d extent) {
        this(extent, StrideOrder1d.DEFAULT);
    }

    /**
     * Create a new structure which is like this one.
     *
     * @return a new structure which is like this one
     */
    public Structure1d like() {
        return new Structure1d(extent);
    }

    /**
     * Return a new structure which defines a view with the given range.
     *
     * @param range the view range
     * @return a new structure which defines a view with the given range
     * @throws IndexOutOfBoundsException if the created view structure doesn't
     *         fit into the current structure
     */
    public Structure1d view(final Range1d range) {
        if (range.start().value() + range.extent().size() > extent.size()) {
            throw new IndexOutOfBoundsException(extent + " : " + range);
        }

        return new Structure1d(
            range.extent(),
            new StrideOrder1d(
                order.start().value() +
                    order.stride().value()*range.start().value(),
                order.stride().value()
            )
        );
    }

    /**
     * Return a new structure which defines a view with the given range.
     *
     * @param stride the stride
     * @return a new structure which defines a view with the given range
     */
    public Structure1d view(final Stride1d stride) {
        return new Structure1d(
            new Extent1d(
                extent.size() != 0
                    ? (extent.size() - 1)/stride.value() + 1
                    : 0
            ),
            new StrideOrder1d(
                order.start().value(),
                order.stride().value()*stride.value()
            )
        );
    }

    /**
     * Return a new structure which defines a copy with the given range.
     *
     * @param range the view range
     * @return a new structure which defines a view with the given range
     * @throws IndexOutOfBoundsException if the created view structure doesn't
     *         fit into the current structure
     * @throws UnsupportedOperationException if the {@link #order()} function
     *         is not an instance of {@link StrideOrder1d}
     */
    public Structure1d copy(final Range1d range) {
        checkRange(range);

        return new Structure1d(range.extent(), StrideOrder1d.DEFAULT);
    }

    private void checkRange(final Range1d range) {
        if (range.extent().size() > extent.size()) {
            throw new IndexOutOfBoundsException(extent + " : " + range);
        }
    }

}
