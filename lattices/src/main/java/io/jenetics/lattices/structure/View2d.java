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
 * Functional interface for doing view transformation.
 */
@FunctionalInterface
public interface View2d {

    /**
     * This function performs a transpose transformation.
     */
    View2d TRANSPOSE = structure ->  new Structure2d(
        new Extent2d(
            structure.extent().cols(),
            structure.extent().rows()
        ),
        new StrideOrder2d(
            new Index2d(
                structure.order().start().col(),
                structure.order().start().row()
            ),
            new Stride2d(
                structure.order().stride().col(),
                structure.order().stride().row()
            )
        )
    );

    /**
     * Applies the <em>view</em> transformation of the given {@code structure}.
     *
     * @param structure the structure to apply the view transformation on
     * @return a new <em>view</em>-structure
     */
    Structure2d apply(final Structure2d structure);

    /**
     * Return a transformation which creates a view of the given {@code range}.
     *
     * @param range the range of the view
     * @return a transformation which creates a view of the given {@code range}
     */
    static View2d of(final Range2d range) {
        requireNonNull(range);

        return structure -> new Structure2d(
            range.extent(),
            new StrideOrder2d(
                new Index2d(
                    structure.order().start().row() +
                        structure.order().stride().row()*range.start().row(),
                    structure.order().start().col() +
                        structure.order().stride().col()*range.start().col()
                ),
                structure.order().stride()
            )
        );
    }

    /**
     * Return a transformation which creates a view of the given {@code extent}.
     *
     * @param extent the extent of the view
     * @return a transformation which creates a view of the given {@code extent}
     */
    static View2d of(final Extent2d extent) {
        return of(new Range2d(extent));
    }

    /**
     * Return a new stride view transformation.
     *
     * @param stride the stride of the created view transformation
     * @return a new stride view transformation
     */
    static View2d of(final Stride2d stride) {
        requireNonNull(stride);

        return structure -> {
            final var extent = structure.extent();
            final var order = structure.order();

            return new Structure2d(
                new Extent2d(
                    extent.rows() != 0
                        ? (extent.rows() - 1)/stride.row() + 1
                        : 0,
                    extent.cols() != 0
                        ? (extent.cols() - 1)/stride.col() + 1
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
        };
    }

}
