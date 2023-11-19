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

import java.util.Objects;

/**
 * This interface defines a projection from 3-d to 2-d.
 *
 * @see Projection2d
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
@FunctionalInterface
public interface Projection3d {

    /**
     * Performs the projection the given {@code structure}.
     *
     * @param structure the structure to apply this projection
     * @return the projected 2-d structure
     */
    Structure2d apply(Structure3d structure);

    /**
     * Returns a new projection function, which first applies the given view
     * transformation and then {@code this} projection.
     *
     * @param view the view to apply first to a given 3-d structure
     * @return a new projection, which applies first the given view
     */
    default Projection3d compose(View3d view) {
        requireNonNull(view);
        return structure -> apply(view.apply(structure));
    }

    /**
     * Returns a new projection function, which applies the given view
     * transformation after {@code this} projection.
     *
     * @param view the view transformation to be applied after the projection
     * @return a new projection
     */
    default Projection3d andThen(View2d view) {
        requireNonNull(view);
        return structure -> view.apply(apply(structure));
    }

    /**
     * Create a <em>slice</em>-projection for the slice with the given
     * {@code index}.
     *
     * @param index the column index
     * @return a new <em>slice</em>-projection
     * @throws IndexOutOfBoundsException if the given {@code index} is negative
     */
    static Projection3d slice(int index) {
        Objects.checkIndex(index, Integer.MAX_VALUE);

        return structure -> {
            Objects.checkIndex(index, structure.extent().slices());

            return new Structure2d(
                new Extent2d(
                    structure.extent().rows(),
                    structure.extent().cols()
                ),
                new Layout2d(
                    new Index2d(
                        structure.layout().start().row(),
                        structure.layout().offset(index, 0, 0)
                    ),
                    new Stride2d(
                        structure.layout().stride().row(),
                        structure.layout().stride().col()
                    ),
                    structure.layout().band()
                )
            );
        };
    }

    /**
     * Create a <em>row</em>-projection for the row with the given {@code index}.
     *
     * @param index the row index
     * @return a new <em>row</em>-projection
     * @throws IndexOutOfBoundsException if the given {@code index} is negative
     */
    static Projection3d row(int index) {
        Objects.checkIndex(index, Integer.MAX_VALUE);

        return structure -> {
            Objects.checkIndex(index, structure.extent().rows());

            return new Structure2d(
                new Extent2d(
                    structure.extent().slices(),
                    structure.extent().cols()
                ),
                new Layout2d(
                    new Index2d(
                        structure.layout().start().slice(),
                        structure.layout().offset(0, index, 0)
                    ),
                    new Stride2d(
                        structure.layout().stride().slice(),
                        structure.layout().stride().col()
                    ),
                    structure.layout().band()
                )
            );
        };
    }

    /**
     * Create a <em>column</em>-projection for the column with the given
     * {@code index}.
     *
     * @param index the column index
     * @return a new <em>column</em>-projection
     * @throws IndexOutOfBoundsException if the given {@code index} is negative
     */
    static Projection3d col(int index) {
        Objects.checkIndex(index, Integer.MAX_VALUE);

        return structure -> {
            Objects.checkIndex(index, structure.extent().cols());

            return new Structure2d(
                new Extent2d(
                    structure.extent().slices(),
                    structure.extent().rows()
                ),
                new Layout2d(
                    new Index2d(
                        structure.layout().start().slice(),
                        structure.layout().offset(0, 0, index)
                    ),
                    new Stride2d(
                        structure.layout().stride().slice(),
                        structure.layout().stride().row()
                    ),
                    structure.layout().band()
                )
            );
        };
    }

}
