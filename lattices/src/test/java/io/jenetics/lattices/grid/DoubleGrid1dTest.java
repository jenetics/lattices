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
package io.jenetics.lattices.grid;

import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.Test;

import io.jenetics.lattices.array.DenseDoubleArray;
import io.jenetics.lattices.structure.Extent1d;
import io.jenetics.lattices.structure.Structure1d;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class DoubleGrid1dTest {

    @Test
    public void assign() {
        final var extent = new Extent1d(100);
        final var structure = new Structure1d(extent);
        final var grid = new DoubleGrid1d(
            structure,
            DenseDoubleArray.ofSize(extent.size())
        );

        grid.assign(87);
        grid.forEach(i -> assertThat(grid.get(i)).isEqualTo(87.0));
    }

}
