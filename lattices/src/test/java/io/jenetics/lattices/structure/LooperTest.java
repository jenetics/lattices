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
import static io.jenetics.lattices.structure.IndexCursor.forward;
import static io.jenetics.lattices.structure.IndexCursor.loopable;
import static io.jenetics.lattices.structure.Precedence.reverse;

import java.util.ArrayList;

import org.testng.annotations.Test;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class LooperTest {

    @Test
    public void simpleIteration() {
        final var range = Range.of(Index.of(1, 2, 3), Extent.of(2, 2, 2));

        final var forEachIndexes = new ArrayList<>();
        final Looper loop = Looper.forward(range, reverse(range.dimensionality()));
        loop.forEach((a, b, c) -> {
            forEachIndexes.add(new IntArray(a, b, c));
        });

        final var loopableIndexes = new ArrayList<>();
        final var indexes = loopable(() -> forward(range, reverse(range.dimensionality())));
        for (var index : indexes) {
            loopableIndexes.add(new IntArray(index));
        }

        assertThat(forEachIndexes).isEqualTo(loopableIndexes);
    }

}
