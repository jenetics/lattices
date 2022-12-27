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

@FunctionalInterface
public interface View2d {

    /**
     * Applies the <em>view</em> transformation of the given {@code structure}.
     *
     * @param structure the structure to apply the view transformation on
     * @return a new <em>view</em>-structure
     */
    Structure2d apply(final Structure2d structure);

    static View2d of(final Range2d range) {
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

    static View2d of(final Extent2d extent) {
        return of(new Range2d(extent));
    }

    static View2d of(final Stride2d stride) {
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
