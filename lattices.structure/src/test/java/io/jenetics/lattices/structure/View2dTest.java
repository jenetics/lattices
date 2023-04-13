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

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class View2dTest {

    private static final Extent2d EXTENT = new Extent2d(100, 200);

    private static final Channels CHANNELS = Channels.THREE;

    private static final Structure2d STRUCTURE = Structure2d.of(EXTENT, CHANNELS);

    private static final String[] ARRAY = new String[EXTENT.size()*CHANNELS.value()];
    static {
        for (int i = 0; i < EXTENT.size(); ++i) {
            final var offset = i*CHANNELS.value();
            final var index = STRUCTURE.index(offset);
            final var value = "v_" + index.row() + "_" + index.col();

            ARRAY[offset] = value;
            ARRAY[offset + 1] = value + "_c2";
            ARRAY[offset + 2] = value + "_c3";
        }
    }

    @Test
    public void transpose() {
        final var structure = View2d.TRANSPOSE.apply(STRUCTURE);
        assertThat(structure.extent().rows()).isEqualTo(EXTENT.cols());
        assertThat(structure.extent().cols()).isEqualTo(EXTENT.rows());

        final var structure0 = View2d.of(Channel.ZERO).apply(structure);
        final var structure1 = View2d.of(Channel.ONE).apply(structure);
        final var structure2 = View2d.of(Channel.TWO).apply(structure);

        for (int r = 0; r < structure.extent().rows(); ++r) {
            for (int c = 0; c < structure.extent().cols(); ++c) {
                final int offset = structure.layout().offset(r, c);

                final var expected = "v_" + c + "_" + r;

                assertThat(ARRAY[offset]).isEqualTo(expected);
                assertThat(ARRAY[offset + 1]).isEqualTo(expected + "_c2");
                assertThat(ARRAY[offset + 2]).isEqualTo(expected + "_c3");

                assertThat(ARRAY[structure0.offset(r, c)]).isEqualTo(expected);
                assertThat(ARRAY[structure1.offset(r, c)]).isEqualTo(expected + "_c2");
                assertThat(ARRAY[structure2.offset(r, c)]).isEqualTo(expected + "_c3");
            }
        }
    }

    @Test(dataProvider = "ranges")
    public void ofRange(Range2d range) {
        final var view = View2d.of(range);
        final var structure = view.apply(STRUCTURE);
        assertThat(structure.extent().size()).isEqualTo(range.extent().size());

        final var structure0 = View2d.of(Channel.ZERO).apply(structure);
        final var structure1 = View2d.of(Channel.ONE).apply(structure);
        final var structure2 = View2d.of(Channel.TWO).apply(structure);

        for (int r = 0; r < structure.extent().rows(); ++r) {
            for (int c = 0; c < structure.extent().cols(); ++c) {
                final int offset = structure.layout().offset(r, c);

                final var expected = "v_" +
                    (r + range.start().row()) + "_" +
                    (c + range.start().col());

                assertThat(ARRAY[offset]).isEqualTo(expected);
                assertThat(ARRAY[offset + 1]).isEqualTo(expected + "_c2");
                assertThat(ARRAY[offset + 2]).isEqualTo(expected + "_c3");

                assertThat(ARRAY[structure0.offset(r, c)]).isEqualTo(expected);
                assertThat(ARRAY[structure1.offset(r, c)]).isEqualTo(expected + "_c2");
                assertThat(ARRAY[structure2.offset(r, c)]).isEqualTo(expected + "_c3");
            }
        }
    }

    @DataProvider
    public Object[][] ranges() {
        return new Object[][] {
            { new Range2d(EXTENT) },
            { new Range2d(new Index2d(0, 0), new Extent2d(34, 32)) },
            { new Range2d(new Index2d(0, 4), new Extent2d(34, 32)) },
            { new Range2d(new Index2d(5, 0), new Extent2d(34, 32)) },
            { new Range2d(new Index2d(7, 4), new Extent2d(34, 32)) }
        };
    }

    @Test(dataProvider = "starts")
    public void ofStart(Index2d start) {
        final var view = View2d.of(start);
        final var structure = view.apply(STRUCTURE);

        final var structure0 = View2d.of(Channel.ZERO).apply(structure);
        final var structure1 = View2d.of(Channel.ONE).apply(structure);
        final var structure2 = View2d.of(Channel.TWO).apply(structure);

        for (int r = 0; r < structure.extent().rows(); ++r) {
            for (int c = 0; c < structure.extent().cols(); ++c) {
                int offset = structure.offset(r, c);

                final var expected = "v_" +
                    (r + start.row()) + "_" +
                    (c + start.col());

                assertThat(ARRAY[offset]).isEqualTo(expected);
                assertThat(ARRAY[offset + 1]).isEqualTo(expected + "_c2");
                assertThat(ARRAY[offset + 2]).isEqualTo(expected + "_c3");

                assertThat(ARRAY[structure0.offset(r, c)]).isEqualTo(expected);
                assertThat(ARRAY[structure1.offset(r, c)]).isEqualTo(expected + "_c2");
                assertThat(ARRAY[structure2.offset(r, c)]).isEqualTo(expected + "_c3");
            }
        }
    }

    @DataProvider
    public Object[][] starts() {
        return new Object[][] {
            { new Index2d(0, 0) },
            { new Index2d(11, 0) },
            { new Index2d(11, 7) }
        };
    }

    @Test(dataProvider = "strides")
    public void ofStride(Stride2d stride) {
        final var view = View2d.of(stride);
        final var structure = view.apply(STRUCTURE);

        final var structure0 = View2d.of(Channel.ZERO).apply(structure);
        final var structure1 = View2d.of(Channel.ONE).apply(structure);
        final var structure2 = View2d.of(Channel.TWO).apply(structure);


        for (int r = 0; r < structure.extent().rows(); ++r) {
            for (int c = 0; c < structure.extent().cols(); ++c) {
                final int offset = structure.layout().offset(r, c);

                final var expected = "v_" +
                    (r*stride.row()) + "_" +
                    (c*stride.col());

                assertThat(ARRAY[offset]).isEqualTo(expected);
                assertThat(ARRAY[offset + 1]).isEqualTo(expected + "_c2");
                assertThat(ARRAY[offset + 2]).isEqualTo(expected + "_c3");

                assertThat(ARRAY[structure0.offset(r, c)]).isEqualTo(expected);
                assertThat(ARRAY[structure1.offset(r, c)]).isEqualTo(expected + "_c2");
                assertThat(ARRAY[structure2.offset(r, c)]).isEqualTo(expected + "_c3");
            }
        }
    }

    @DataProvider
    public Object[][] strides() {
        return new Object[][] {
            { new Stride2d(1, 1) },
            { new Stride2d(2, 1) },
            { new Stride2d(3, 2) },
            { new Stride2d(4, 3) },
            { new Stride2d(5, 2) },
            { new Stride2d(11, 4) }
        };
    }

}
