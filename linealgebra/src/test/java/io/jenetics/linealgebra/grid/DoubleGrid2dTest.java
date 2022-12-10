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
package io.jenetics.linealgebra.grid;

import static org.assertj.core.api.Assertions.assertThat;
import static io.jenetics.linealgebra.MatrixRandom.next;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.jenetics.linealgebra.LinealgebraAsserts;
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

    @Test(dataProvider = "grids")
    public void equals(
        final DoubleGrid2d grid1,
        final DoubleGrid2d grid2,
        final boolean equals
    ) {
        LinealgebraAsserts.assertEquals(grid1, grid1);
        LinealgebraAsserts.assertEquals(grid2, grid2);

        if (equals) {
            LinealgebraAsserts.assertEquals(grid1, grid2);
            LinealgebraAsserts.assertEquals(grid2, grid1);
        } else {
            LinealgebraAsserts.assertNotEquals(grid1, grid2);
            LinealgebraAsserts.assertNotEquals(grid2, grid1);
        }
    }

    @DataProvider
    public Object[][] grids() {
        return new Object[][] {
            { next(new Extent2d(0, 0)), next(new Extent2d(0, 0)), true },
            { next(new Extent2d(0, 1)), next(new Extent2d(0, 1)), true },
            { next(new Extent2d(1, 0)), next(new Extent2d(1, 0)), true },
            { next(new Extent2d(0, 0)), next(new Extent2d(0, 10)), false },
            { next(new Extent2d(5, 0)), next(new Extent2d(0, 0)), false },
            { next(new Extent2d(5, 50)), next(new Extent2d(5, 50)), false },
            { next(new Extent2d(50, 9)), next(new Extent2d(50, 9)), false },
            { next(new Extent2d(50, 30)), next(new Extent2d(50, 9)), false },
            equalGrids(new Extent2d(1, 1)),
            equalGrids(new Extent2d(1, 100)),
            equalGrids(new Extent2d(100, 1)),
            equalGrids(new Extent2d(100, 100))
        };
    }

    private static Object[] equalGrids(final Extent2d extent) {
        final var matrix = next(extent);
        return new Object[] { matrix, matrix.copy(), true };
    }

}
