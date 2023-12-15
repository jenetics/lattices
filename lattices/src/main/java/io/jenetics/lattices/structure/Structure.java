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

/**
 * Definition of a lattice structure.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public interface Structure extends Dimensional {

    /**
     * The extent of the structure.
     *
     * @return the extent of the structure
     */
    Extent extent();

    /**
     * The structure layout.
     *
     * @return the structure layout
     */
    Layout layout();

    /**
     * Create a new structure with the given {@code extent} and {@code layout}.
     *
     * @param extent the extent of the structure
     * @param layout the structure layout
     * @return a new structure with the given {@code extent} and {@code layout}
     */
    static Structure of(Extent extent, Layout layout) {
        record StructureNd(Extent extent, Layout layout) implements Structure {
            StructureNd {
                if (extent.dimensions() != layout.dimensions()) {
                    throw new IllegalArgumentException(
                        "extent.dimensions != layout.dimensions: %d != %d."
                            .formatted(extent.dimensions(), layout.dimensions())
                    );
                }
            }
            @Override
            public int dimensions() {
                return extent.dimensions();
            }
        }

        if (extent instanceof Extent1d ext && layout instanceof Layout1d lt) {
            return new Structure1d(ext, lt);
        }
        if (extent instanceof Extent2d ext && layout instanceof Layout2d lt) {
            return new Structure2d(ext, lt);
        }
        if (extent instanceof Extent3d ext && layout instanceof Layout3d lt) {
            return new Structure3d(ext, lt);
        }

        return new StructureNd(extent, layout);
    }

    /**
     * Create a new structure with the given extent.
     *
     * @param extent the structure extent
     * @return a new structure with the given extent
     */
    static Structure of(Extent extent) {
        if (extent instanceof Extent1d ext) {
            return new Structure1d(ext);
        }
        if (extent instanceof Extent2d ext) {
            return new Structure2d(ext);
        }
        if (extent instanceof Extent3d ext) {
            return new Structure3d(ext);
        }

        final var strides = new int[extent.dimensions()];
        for (int i = 0; i < extent.dimensions() - 1; ++i) {
            int stride = 1;
            for (int j = extent.dimensions() - i - 1; --j >= 0;) {
                stride *= extent.at(j);
            }
            stride *= extent.bands();
            strides[extent.dimensions() - i - 1] = stride;
        }
        strides[extent.dimensions() - 1] = extent.bands();

        return Structure.of(
            extent,
            Layout.of(
                Index.of(new int[extent.dimensions()]),
                Stride.of(strides),
                Band.ZERO
            )
        );
    }

}
