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
 * Defines a 2-d structure, which is defined by the extent of the structure and
 * the index order of the underlying 1-d structure.
 *
 * @param extent the extent of the structure
 * @param order the element order
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record Structure2d(Extent2d extent, StrideOrder2d order) {

    public Structure2d {
        requireNonNull(extent);
        requireNonNull(order);
    }

    /**
     * Create a new structure with the given extent and the default element order.
     *
     * @param extent the extent of the structure
     */
    public Structure2d(final Extent2d extent) {
        this(extent, new StrideOrder2d(extent));
    }

    /**
     * Create a new structure which is like this one.
     *
     * @return a new structure which is like this one
     */
    public Structure2d like() {
        return new Structure2d(extent);
    }

    /**
     * Return a new structure which defines a view with the given range.
     *
     * @param range the view range
     * @return a new structure which defines a view with the given range
     * @throws IndexOutOfBoundsException if the created view structure doesn't
     *         fit into the current structure
     * @throws UnsupportedOperationException if the {@link #order()} function
     *         is not an instance of {@link StrideOrder2d}
     */
    public Structure2d view(final Range2d range) {
        checkRange(range);

        return new Structure2d(
            range.extent(),
            new StrideOrder2d(
                new Index2d(
                order.start().row() + order.stride().row()*range.start().row(),
                order.start().col() + order.stride().col()*range.start().col()
                ),
                order.stride()
            )
        );
    }

    private void checkRange(final Range2d range) {
        if (range.start().col() + range.extent().cols() > extent.cols() ||
            range.start().row() + range.extent().rows() > extent.rows())
        {
            throw new IndexOutOfBoundsException(extent + " : " + range);
        }
    }

    /**
     * Return a new structure which defines a view with the given range.
     *
     * @param stride the strides
     * @return a new structure which defines a view with the given range
     * @throws IndexOutOfBoundsException if the created view structure doesn't
     *         fit into the current structure
     * @throws UnsupportedOperationException if the {@link #order()} function
     *         is not an instance of {@link StrideOrder2d}
     */
    public Structure2d view(final Stride2d stride) {
        return new Structure2d(
            new Extent2d(
                extent.rows() != 0
                    ? (extent.rows() - 1)/stride.row() + 1
                    : 0,
                extent.cols() != 0
                    ? (extent.cols() - 1)/ stride.col() + 1
                    : 0
            ),
            new StrideOrder2d(
                order.start(),
                new Stride2d(
                    order.stride().row()*stride.row(),
                    order.stride().col()*stride.col()
                )
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
     *         is not an instance of {@link StrideOrder2d}
     */
    public Structure2d copy(final Range2d range) {
        checkRange(range);

        return new Structure2d(
            range.extent(),
            new StrideOrder2d(range.extent())
        );
    }

    /**
     * Transposes the matrix structure without copying any matrix elements.
     *
     * @return the transposed matrix structure
     */
    public Structure2d transpose() {
        return new Structure2d(
            new Extent2d(extent.cols(), extent.rows()),
            order.transpose()
        );
    }

}
