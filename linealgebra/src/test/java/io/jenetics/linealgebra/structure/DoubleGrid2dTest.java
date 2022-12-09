/*
 * Java Linear Algebra Library (@__identifier__@).
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
package io.jenetics.linealgebra.structure;

import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.Test;

import io.jenetics.linealgebra.array.DenseDoubleArray;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class DoubleGrid2dTest {

    @Test
    public void setAndGet() {
        final var extent = new Extent2d(100, 20);
        final var structure = new Structure2d(extent);
        final var grid = new DoubleGrid2d(
            structure,
            DenseDoubleArray.ofSize(extent.size())
        );

        grid.forEach((row, col) -> grid.set(row, col, row*col));
        grid.forEach((row, col) -> assertThat(grid.get(row, col)).isEqualTo(row*col));
    }

}
