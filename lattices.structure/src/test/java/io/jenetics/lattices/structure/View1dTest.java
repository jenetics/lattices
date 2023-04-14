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
public class View1dTest {

    private static final Extent1d EXTENT = new Extent1d(10_000);

    private static final Channels CHANNELS = Channels.THREE;

    private static final Structure1d STRUCTURE = Structure1d.of(EXTENT, CHANNELS);

    private static final String[] ARRAY = new String[EXTENT.size()*CHANNELS.value()];
    static {
        for (int i = 0; i < EXTENT.size(); ++i) {
            final var offset = i*CHANNELS.value();
            final var index = STRUCTURE.index(offset);
            final var value = "v_" + index.value();

            ARRAY[offset] = value;
            ARRAY[offset + 1] = value + "_c2";
            ARRAY[offset + 2] = value + "_c3";
        }
    }

    @Test(dataProvider = "ranges")
    public void ofRange(Range1d range) {
        final var view = View1d.of(range);
        final var structure = view.apply(STRUCTURE);
        assertThat(structure.extent().value()).isEqualTo(range.extent().value());

        final var structure0 = View1d.of(Channel.ZERO).apply(structure);
        final var structure1 = View1d.of(Channel.ONE).apply(structure);
        final var structure2 = View1d.of(Channel.TWO).apply(structure);

        for (int i = 0; i < structure.extent().value(); ++i) {
            final var offset = structure.offset(i);

            final var expected = "v_" + (i + range.start().value());

            assertThat(ARRAY[offset]).isEqualTo(expected);
            assertThat(ARRAY[offset + 1]).isEqualTo(expected + "_c2");
            assertThat(ARRAY[offset + 2]).isEqualTo(expected + "_c3");

            assertThat(ARRAY[structure0.offset(i)]).isEqualTo(expected);
            assertThat(ARRAY[structure1.offset(i)]).isEqualTo(expected + "_c2");
            assertThat(ARRAY[structure2.offset(i)]).isEqualTo(expected + "_c3");
        }
    }

    @DataProvider
    public Object[][] ranges() {
        return new Object[][] {
            { new Range1d(EXTENT) },
            { new Range1d(0, 100) },
            { new Range1d(10, 100) },
            { new Range1d(4_000, 5_000) }
        };
    }

    @Test(dataProvider = "starts")
    public void ofStart(Index1d start) {
        final var view = View1d.of(start);
        final var structure = view.apply(STRUCTURE);
        assertThat(structure.extent().value())
            .isEqualTo(STRUCTURE.extent().value() - start.value());

        final var structure0 = View1d.of(Channel.ZERO).apply(structure);
        final var structure1 = View1d.of(Channel.ONE).apply(structure);
        final var structure2 = View1d.of(Channel.TWO).apply(structure);

        for (int i = 0; i < structure.extent().value(); ++i) {
            final int offset = structure.layout().offset(i);

            final var expected = "v_" + (i + start.value());

            assertThat(ARRAY[offset]).isEqualTo(expected);
            assertThat(ARRAY[offset + 1]).isEqualTo(expected + "_c2");
            assertThat(ARRAY[offset + 2]).isEqualTo(expected + "_c3");

            assertThat(ARRAY[structure0.offset(i)]).isEqualTo(expected);
            assertThat(ARRAY[structure1.offset(i)]).isEqualTo(expected + "_c2");
            assertThat(ARRAY[structure2.offset(i)]).isEqualTo(expected + "_c3");
        }
    }

    @DataProvider
    public Object[][] starts() {
        return new Object[][] {
            { new Index1d(0) },
            { new Index1d(11) }
        };
    }

    @Test(dataProvider = "strides")
    public void ofStride(Stride1d stride) {
        final var view = View1d.of(stride);
        final var structure = view.apply(STRUCTURE);

        final var structure0 = View1d.of(Channel.ZERO).apply(structure);
        final var structure1 = View1d.of(Channel.ONE).apply(structure);
        final var structure2 = View1d.of(Channel.TWO).apply(structure);

        for (int i = 0; i < structure.extent().value(); ++i) {
            final int offset = structure.layout().offset(i);

            final var expected = "v_" + i*stride.value();

            assertThat(ARRAY[offset]).isEqualTo(expected);
            assertThat(ARRAY[offset + 1]).isEqualTo(expected + "_c2");
            assertThat(ARRAY[offset + 2]).isEqualTo(expected + "_c3");

            assertThat(ARRAY[structure0.offset(i)]).isEqualTo(expected);
            assertThat(ARRAY[structure1.offset(i)]).isEqualTo(expected + "_c2");
            assertThat(ARRAY[structure2.offset(i)]).isEqualTo(expected + "_c3");
        }
    }

    @DataProvider
    public Object[][] strides() {
        return new Object[][] {
            { new Stride1d(1) },
            { new Stride1d(2) },
            { new Stride1d(3) },
            { new Stride1d(4) },
            { new Stride1d(5) },
            { new Stride1d(11) }
        };
    }

}
