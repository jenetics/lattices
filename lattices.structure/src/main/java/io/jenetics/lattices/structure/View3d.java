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
 * @see View2d
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
@FunctionalInterface
public interface View3d {

    /**
     * Applies the <em>view</em> transformation of the given {@code structure}.
     *
     * @param structure the structure to apply the view transformation on
     * @return a new <em>view</em>-structure
     */
    Structure3d apply(Structure3d structure);

    /**
     * Return a new view, which apply first the given {@code view} and then
     * {@code this} view.
     *
     * @param view the view to be applied first
     * @return a new <em>composed</em> view
     */
    default View3d compose(View3d view) {
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
    default View3d andThen(View3d view) {
        requireNonNull(view);
        return structure -> view.apply(apply(structure));
    }

    /**
     * Return a transformation which creates a view of the given {@code range}.
     *
     * @param range the range of the view
     * @return a transformation which creates a view of the given {@code range}
     */
    static View3d of(Range3d range) {
        requireNonNull(range);

        return structure -> new Structure3d(
            range.extent(),
            new Layout3d(
                new Index3d(
                    structure.layout().start().slice() +
                        structure.layout().stride().slice()*range.start().slice(),
                    structure.layout().start().row() +
                        structure.layout().stride().row()*range.start().row(),
                    structure.layout().start().col() +
                        structure.layout().stride().col()*range.start().col()
                ),
                structure.layout().stride()
            ),
            structure.channel()
        );
    }

    /**
     * Return a transformation which creates a view of the given {@code start}.
     *
     * @param start the start of the view
     * @return a transformation which creates a view of the given {@code start}
     */
    static View3d of(Index3d start) {
        requireNonNull(start);

        return structure -> View3d
            .of(
                new Range3d(
                    start,
                    new Extent3d(
                        structure.extent().slices() - start.slice(),
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
    static View3d of(Extent3d extent) {
        return of(new Range3d(extent));
    }

    /**
     * Return a new stride view transformation.
     *
     * @param stride the stride of the created view transformation
     * @return a new stride view transformation
     */
    static View3d of(Stride3d stride) {
        requireNonNull(stride);

        return structure -> {
            final var extent = structure.extent();
            final var order = structure.layout();

            return new Structure3d(
                new Extent3d(
                    extent.slices() != 0
                        ? (extent.slices() - 1)/stride.slice() + 1
                        : 0,
                    extent.rows() != 0
                        ? (extent.rows() - 1)/stride.row() + 1
                        : 0,
                    extent.cols() != 0
                        ? (extent.cols() - 1)/stride.col() + 1
                        : 0
                ),
                new Layout3d(
                    order.start(),
                    new Stride3d(
                        order.stride().slice()*stride.slice(),
                        order.stride().row()*stride.row(),
                        order.stride().col()*stride.col()
                    )
                ),
                structure.channel()
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
    static View3d of(Channel channel) {
        requireNonNull(channel);

        return structure -> {
            if (structure.channel().equals(channel)) {
                return structure;
            }

            return new Structure3d(
                structure.extent(),
                structure.layout(),
                channel
            );
        };
    }

}
