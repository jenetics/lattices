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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.IntStream;

import org.testng.annotations.Test;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class Projection3dTest {

    private static final Extent3d EXTENT = new Extent3d(100, 100, 100);

    private static final Structure3d STRUCTURE = Structure3d.of(EXTENT);

    private static final String[] ARRAY = IntStream.range(0, EXTENT.size())
        .mapToObj(i -> {
            final var index = STRUCTURE.layout().index(i);
            return "v_" + index.slice() + "_" + index.row() + "_" + index.col();
        })
        .toArray(String[]::new);

    @Test
    public void sliceProjection() {
        final var projection = Projection3d.slice(32);
        final var structure = projection.apply(STRUCTURE);

        for (int r = 0; r < structure.extent().rows(); ++r) {
            for (int c = 0; c < structure.extent().cols(); ++c) {
                final int offset = structure.layout().offset(r, c);

                final var expected = "v_32_" + r + "_" + c;

                assertThat(ARRAY[offset]).isEqualTo(expected);
            }
        }
    }

    @Test
    public void rowProjection() {
        final var projection = Projection3d.row(32);
        final var structure = projection.apply(STRUCTURE);

        for (int r = 0; r < structure.extent().rows(); ++r) {
            for (int c = 0; c < structure.extent().cols(); ++c) {
                final int offset = structure.layout().offset(r, c);

                final var expected = "v_" + r + "_32_" + c;

                assertThat(ARRAY[offset]).isEqualTo(expected);
            }
        }
    }

    @Test
    public void viewRowProjection() {
        final var projection = Projection3d.row(32);
        final var range = new Range2d(
            new Index2d(20, 20),
            new Extent2d(10, 10)
        );
        final var view = View2d.of(range);
        final var structure = view.apply(projection.apply(STRUCTURE));

        for (int r = 0; r < structure.extent().rows(); ++r) {
            for (int c = 0; c < structure.extent().cols(); ++c) {
                final int offset = structure.layout().offset(r, c);

                final var expected = "v_" +
                    (r + range.start().row()) + "_32_" +
                    (c + range.start().col());

                assertThat(ARRAY[offset]).isEqualTo(expected);
            }
        }
    }

    @Test
    public void colProjection() {
        final var projection = Projection3d.col(32);
        final var structure = projection.apply(STRUCTURE);

        for (int r = 0; r < structure.extent().rows(); ++r) {
            for (int c = 0; c < structure.extent().cols(); ++c) {
                final int offset = structure.layout().offset(r, c);

                final var expected = "v_" + r + "_" + c + "_32";

                assertThat(ARRAY[offset]).isEqualTo(expected);
            }
        }
    }

    @Test
    public void sliceRowProjection() {
        final var prj3 = Projection3d.slice(32);
        final var prj2 = Projection2d.row(32);
        final var structure = prj2.apply(prj3.apply(STRUCTURE));

        for (int i = 0; i < structure.extent().size(); ++i) {
            final int offset = structure.layout().offset(i);

            final var expected = "v_32_" + "32_" + i;

            assertThat(ARRAY[offset]).isEqualTo(expected);
        }
    }

    @Test
    public void sliceColProjection() {
        final var prj3 = Projection3d.slice(32);
        final var prj2 = Projection2d.col(32);
        final var structure = prj2.apply(prj3.apply(STRUCTURE));

        for (int i = 0; i < structure.extent().size(); ++i) {
            final int offset = structure.layout().offset(i);

            final var expected = "v_32_" + i +  "_32";

            assertThat(ARRAY[offset]).isEqualTo(expected);
        }
    }

}
