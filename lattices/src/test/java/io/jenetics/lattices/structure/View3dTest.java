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

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class View3dTest {

    private static final Extent3d EXTENT = new Extent3d(100, 100, 100);

    private static final Channels CHANNELS = Channels.THREE;

    private static final Structure3d STRUCTURE = Structure3d.of(EXTENT, CHANNELS);

    private static final String[] ARRAY = new String[EXTENT.size()*CHANNELS.value()];
    static {
        for (int i = 0; i < EXTENT.size(); ++i) {
            final var offset = i*CHANNELS.value();
            final var index = STRUCTURE.index(offset);
            final var value ="v_" + index.slice() +
                "_" + index.row() +
                "_" + index.col();

            ARRAY[offset] = value;
            ARRAY[offset + 1] = value + "_c2";
            ARRAY[offset + 2] = value + "_c3";
        }
    }

    @Test(dataProvider = "ranges")
    public void ofRange(Range3d range) {
        final var view = View3d.of(range);
        final var structure = view.apply(STRUCTURE);
        assertThat(structure.extent().size())
            .isEqualTo(range.extent().size());

        for (int s = 0; s < structure.extent().slices(); ++s) {
            for (int r = 0; r < structure.extent().rows(); ++r) {
                for (int c = 0; c < structure.extent().cols(); ++c) {
                    final int offset = structure.offset(s, r, c);

                    final var expected = "v_" +
                        (s + range.start().slice()) + "_" +
                        (r + range.start().row()) + "_" +
                        (c + range.start().col());

                    assertThat(ARRAY[offset]).isEqualTo(expected);
                }
            }
        }
    }

    @DataProvider
    public Object[][] ranges() {
        return new Object[][] {
            { new Range3d(EXTENT) },
            { new Range3d(new Index3d(0, 0, 0), new Extent3d(34, 32, 45)) },
            { new Range3d(new Index3d(0, 0, 4), new Extent3d(34, 32, 45)) },
            { new Range3d(new Index3d(0, 5, 0), new Extent3d(34, 32, 45)) },
            { new Range3d(new Index3d(0, 7, 4), new Extent3d(34, 32, 45)) },
            { new Range3d(new Index3d(3, 0, 4), new Extent3d(34, 32, 45)) },
            { new Range3d(new Index3d(4, 5, 0), new Extent3d(34, 32, 45)) },
            { new Range3d(new Index3d(5, 7, 4), new Extent3d(34, 32, 45)) }
        };
    }

    @Test(dataProvider = "starts")
    public void ofStart(Index3d start) {
        final var view = View3d.of(start);
        final var structure = view.apply(STRUCTURE);

        for (int s = 0; s < structure.extent().slices(); ++s) {
            for (int r = 0; r < structure.extent().rows(); ++r) {
                for (int c = 0; c < structure.extent().cols(); ++c) {
                    final int offset = structure.offset(s, r, c);

                    final var expected = "v_" +
                        (s + start.slice()) + "_" +
                        (r + start.row()) + "_" +
                        (c + start.col());

                    assertThat(ARRAY[offset]).isEqualTo(expected);
                }
            }
        }
    }

    @DataProvider
    public Object[][] starts() {
        return new Object[][] {
            { new Index3d(0, 0, 0) },
            { new Index3d(0, 0, 4) },
            { new Index3d(0, 5, 0) },
            { new Index3d(0, 7, 4) },
            { new Index3d(3, 0, 4) },
            { new Index3d(4, 5, 0) },
            { new Index3d(5, 7, 4) }
        };
    }

    @Test(dataProvider = "strides")
    public void ofStride(Stride3d stride) {
        final var view = View3d.of(stride);
        final var structure = view.apply(STRUCTURE);

        for (int s = 0; s < structure.extent().slices(); ++s) {
            for (int r = 0; r < structure.extent().rows(); ++r) {
                for (int c = 0; c < structure.extent().cols(); ++c) {
                    final int offset = structure.offset(s, r, c);

                    final var expected = "v_" +
                        (s*stride.slice()) + "_" +
                        (r*stride.row()) + "_" +
                        (c*stride.col());

                    assertThat(ARRAY[offset]).isEqualTo(expected);
                }
            }
        }
    }

    @DataProvider
    public Object[][] strides() {
        return new Object[][] {
            { new Stride3d(1, 1, 1) },
            { new Stride3d(1, 2, 1) },
            { new Stride3d(1, 3, 2) },
            { new Stride3d(1, 4, 3) },
            { new Stride3d(1, 5, 2) },
            { new Stride3d(1, 11, 4) },

            { new Stride3d(2, 1, 1) },
            { new Stride3d(2, 2, 1) },
            { new Stride3d(2, 3, 2) },
            { new Stride3d(2, 4, 3) },
            { new Stride3d(2, 5, 2) },
            { new Stride3d(2, 11, 4) },

            { new Stride3d(3, 1, 1) },
            { new Stride3d(3, 2, 1) },
            { new Stride3d(3, 3, 2) },
            { new Stride3d(3, 4, 3) },
            { new Stride3d(3, 5, 2) },
            { new Stride3d(3, 11, 4) },

            { new Stride3d(4, 1, 1) },
            { new Stride3d(4, 2, 1) },
            { new Stride3d(4, 3, 2) },
            { new Stride3d(4, 4, 3) },
            { new Stride3d(4, 5, 2) },
            { new Stride3d(4, 11, 4) },

            { new Stride3d(11, 1, 1) },
            { new Stride3d(11, 2, 1) },
            { new Stride3d(11, 3, 2) },
            { new Stride3d(11, 4, 3) },
            { new Stride3d(11, 5, 2) },
            { new Stride3d(11, 11, 4) }
        };
    }

}
