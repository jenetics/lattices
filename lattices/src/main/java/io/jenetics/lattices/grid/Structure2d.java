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

import static java.util.Objects.requireNonNull;

import io.jenetics.lattices.matrix.Matrix1d;

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
public record Structure2d(Extent2d extent, Order2d order) {

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
        if (order instanceof StrideOrder2d) {
            return new Structure2d(extent);
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
     *         is not an instance of {@link StrideOrder2d}
     */
    public Structure2d view(final Range2d range) {
        checkRange(range);

        if (order instanceof StrideOrder2d ord) {
            return new Structure2d(
                range.extent(),
                new StrideOrder2d(
                    new Index2d(
                    ord.start().row() + ord.stride().row()*range.start().row(),
                    ord.start().col() + ord.stride().col()*range.start().col()
                    ),
                    ord.stride()
                )
            );
        } else {
            throw new UnsupportedOperationException(
                "Range view structure not supported by " + order
            );
        }
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
        if (order instanceof StrideOrder2d ord) {
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
                    ord.start(),
                    new Stride2d(
                        ord.stride().row()*stride.row(),
                        ord.stride().col()*stride.col()
                    )
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
     *         is not an instance of {@link StrideOrder2d}
     */
    public Structure2d copy(final Range2d range) {
        checkRange(range);

        if (order instanceof StrideOrder2d) {
            return new Structure2d(
                range.extent(),
                new StrideOrder2d(range.extent())
            );
        } else {
            throw new UnsupportedOperationException(
                "Range view structure not supported by " + order
            );
        }
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

    /**
     * Create a new {@link Structure1d} object which can be used to
     * create a column view {@link Matrix1d}.
     *
     * @param index the column index
     * @return a new {@link Structure1d} object
     * @throws IndexOutOfBoundsException if {@code index < 0 || index >= cols()}
     * @throws UnsupportedOperationException if the {@link #order()} function
     *         is not an instance of {@link StrideOrder2d}
     */
    public Structure1d colAt(final int index) {
        if (index < 0 || index >= extent().cols()) {
            throw new IndexOutOfBoundsException(
                "Attempted to access " + extent() + " at column=" + index
            );
        }

        if (order instanceof StrideOrder2d ord) {
            return new Structure1d(
                new Extent1d(extent().rows()),
                new StrideOrder1d(
                    ord.index(0, index),
                    ord.stride().row()
                )
            );
        } else {
            throw new UnsupportedOperationException(
                "Column view structure not supported by " + order
            );
        }
    }

    public Structure1d view(final Projection2d projection) {
        return projection.apply(this);
    }

//    /**
//     * Create a new {@link Structure1d} object which can be used to
//     * create a row view {@link Matrix1d}.
//     *
//     * @param index the row index
//     * @return a new {@link Structure1d} object
//     * @throws IndexOutOfBoundsException if {@code index < 0 || index >= rows()}
//     * @throws UnsupportedOperationException if the {@link #order()} function
//     *         is not an instance of {@link StrideOrder2d}
//     */
//    public Structure1d rowAt(final int index) {
//        if (index < 0 || index >= extent().rows()) {
//            throw new IndexOutOfBoundsException(
//                "Attempted to access " + extent() + " at row=" + index
//            );
//        }
//
//        if (order instanceof StrideOrder2d ord) {
//            return new Structure1d(
//                new Extent1d(extent().cols()),
//                new StrideOrder1d(
//                    ord.index(index, 0),
//                    ord.stride().col()
//                )
//            );
//        } else {
//            throw new UnsupportedOperationException(
//                "Row view structure not supported by " + order
//            );
//        }
//    }

}
