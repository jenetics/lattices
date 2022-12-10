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
package io.jenetics.linealgebra.grid;

import static java.util.Objects.requireNonNull;

/**
 * Defines the structure of a 1-d matrix, which is defined by the dimension of
 * the matrix and the index order of the underlying element array.
 *
 * @param extent the extent of the structure
 * @param order the element order
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
public record Structure1d(Extent1d extent, Order1d order) {

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
        if (order instanceof StrideOrder1d) {
            return new Structure1d(extent);
        } else {
            throw new UnsupportedOperationException(
                "Range view structure not supported by " + order
            );
        }
    }

    /**
     * Return a new structure which defines a view with the given range.
     *
     * @param range the view range
     * @return a new structure which defines a view with the given range
     * @throws IndexOutOfBoundsException if the created view structure doesn't
     *         fit into the current structure
     * @throws UnsupportedOperationException if the {@link #order()} function
     *         is not an instance of {@link StrideOrder1d}
     */
    public Structure1d view(final Range1d range) {
        if (range.index() + range.size() > extent.size()) {
            throw new IndexOutOfBoundsException(extent + " : " + range);
        }

        if (order instanceof StrideOrder1d ord) {
            return new Structure1d(
                new Extent1d(range.size()),
                new StrideOrder1d(
                    ord.start() + ord.stride()*range.index(),
                    ord.stride()
                )
            );
        } else {
            throw new UnsupportedOperationException(
                "Range view structure not supported by " + order
            );
        }
    }

    /**
     * Return a new structure which defines a view with the given range.
     *
     * @param stride the stride
     * @return a new structure which defines a view with the given range
     * @throws UnsupportedOperationException if the {@link #order()} function
     *         is not an instance of {@link StrideOrder1d}
     */
    public Structure1d view(final Stride1d stride) {
        if (order instanceof StrideOrder1d ord) {
            return new Structure1d(
                new Extent1d(
                    extent.size() != 0
                        ? (extent.size() - 1)/stride.stride() + 1
                        : 0
                ),
                new StrideOrder1d(
                    ord.start(),
                    ord.stride()*stride.stride()
                )
            );
        } else {
            throw new UnsupportedOperationException(
                "Range view structure not supported by " + order
            );
        }
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

        if (order instanceof StrideOrder1d) {
            return new Structure1d(
                new Extent1d(range.size()),
                StrideOrder1d.DEFAULT
            );
        } else {
            throw new UnsupportedOperationException(
                "Range view structure not supported by " + order
            );
        }
    }

    private void checkRange(final Range1d range) {
        if (range.size() > extent.size()) {
            throw new IndexOutOfBoundsException(extent + " : " + range);
        }
    }

}
