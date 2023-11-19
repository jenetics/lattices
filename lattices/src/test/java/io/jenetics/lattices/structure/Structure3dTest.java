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

import java.util.random.RandomGenerator;

import org.testng.annotations.Test;

import io.jenetics.lattices.Index3dRandom;
/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class Structure3dTest {

    private final Index3dRandom random =
        new Index3dRandom(RandomGenerator.getDefault());

    @Test
    public void indexOffset() {
        final var structure = new Structure3d(new Extent3d(100, 400, 78));
        final var layout = structure.layout();
        final var range = new Range3d(structure.extent());

        for (int i = 0; i < 1000; ++i) {
            final Index3d index = random.next(range);

            final int offset = layout.offset(index);
            assertThat(offset).isGreaterThanOrEqualTo(0);

            assertThat(layout.index(offset))
                .withFailMessage("Got %s != expect %s: %s %s"
                    .formatted(layout.index(offset), index, layout.start(), layout.stride()))
                .isEqualTo(index);
        }
    }

}
