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
 *
 * @see View1d
 * @see View3d
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
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
        new Layout2d(
            new Index2d(
                structure.layout().start().col(),
                structure.layout().start().row()
            ),
            new Stride2d(
                structure.layout().stride().col(),
                structure.layout().stride().row()
            )
        )
    );

    /**
     * Applies the <em>view</em> transformation of the given {@code structure}.
     *
     * @param structure the structure to apply the view transformation on
     * @return a new <em>view</em>-structure
     */
    Structure2d apply(Structure2d structure);

    /**
     * Return a new view, which apply first the given {@code view} and then
     * {@code this} view.
     *
     * @param view the view to be applied first
     * @return a new <em>composed</em> view
     */
    default View2d compose(View2d view) {
        requireNonNull(view);
        return structure -> apply(view.apply(structure));
    }

    /**
     * Return a new view, which applies the given {@code view} after {@code this}
     * view.
     *
     * @param view the view to be applied after {@code this} view
     * @return a new view
     */
    default View2d andThen(View2d view) {
        requireNonNull(view);
        return structure -> view.apply(apply(structure));
    }

    /**
     * Return a transformation which creates a view of the given {@code range}.
     *
     * @param range the range of the view
     * @return a transformation which creates a view of the given {@code range}
     */
    static View2d of(Range2d range) {
        requireNonNull(range);

        return structure -> new Structure2d(
            range.extent(),
            new Layout2d(
                new Index2d(
                    structure.layout().start().row() +
                        structure.layout().stride().row()*range.start().row(),
                    structure.layout().start().col() +
                        structure.layout().stride().col()*range.start().col()
                ),
                structure.layout().stride()
            )
        );
    }

    /**
     * Return a transformation which creates a view of the given {@code start}.
     *
     * @param start the start of the view
     * @return a transformation which creates a view of the given {@code start}
     */
    static View2d of(Index2d start) {
        requireNonNull(start);

        return structure -> View2d
            .of(
                new Range2d(
                    start,
                    new Extent2d(
                        structure.extent().rows() - start.row(),
                        structure.extent().cols() - start.col()
                    )
                )
            )
            .apply(structure);
    }

    /**
     * Return a transformation which creates a view of the given {@code extent}.
     *
     * @param extent the extent of the view
     * @return a transformation which creates a view of the given {@code extent}
     */
    static View2d of(Extent2d extent) {
        return of(new Range2d(extent));
    }

    /**
     * Return a new stride view transformation.
     *
     * @param stride the stride of the created view transformation
     * @return a new stride view transformation
     */
    static View2d of(Stride2d stride) {
        requireNonNull(stride);

        return structure -> {
            final var extent = structure.extent();
            final var order = structure.layout();

            return new Structure2d(
                new Extent2d(
                    extent.rows() != 0
                        ? (extent.rows() - 1)/stride.row() + 1
                        : 0,
                    extent.cols() != 0
                        ? (extent.cols() - 1)/stride.col() + 1
                        : 0
                ),
                new Layout2d(
                    order.start(),
                    new Stride2d(
                        order.stride().row()*stride.row(),
                        order.stride().col()*stride.col()
                    )
                )
            );
        };
    }

    /**
     * Return a transformation which creates a view onto the given
     * {@code channel}.
     *
     * @param channel the channel number of the returned view
     * @return a transformation which creates a view onto the given
     *        {@code channel}
     */
    static View2d of(Channel channel) {
        requireNonNull(channel);

        return structure -> new Structure2d(
            structure.extent(),
            new Layout2d(
                structure.layout().start(),
                structure.layout().stride(),
                channel
            )
        );
    }

}
