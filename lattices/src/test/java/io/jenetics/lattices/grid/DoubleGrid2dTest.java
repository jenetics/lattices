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

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.jenetics.lattices.MatrixRandom;
import io.jenetics.lattices.array.DenseDoubleArray;
import io.jenetics.lattices.structure.Extent2d;
import io.jenetics.lattices.structure.Structure2d;

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
            DenseDoubleArray.ofLength(extent.cells())
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
        if (equals) {
            assertThat(grid1).isEqualTo(grid2);
            assertThat(grid2).isEqualTo(grid1);
        } else {
            assertThat(grid1).isNotEqualTo(grid2);
            assertThat(grid2).isNotEqualTo(grid1);
        }
    }

    @DataProvider
    public Object[][] grids() {
        return new Object[][] {
            { MatrixRandom.nextDoubleMatrix2d(new Extent2d(0, 0)), MatrixRandom.nextDoubleMatrix2d(new Extent2d(0, 0)), true },
            { MatrixRandom.nextDoubleMatrix2d(new Extent2d(0, 1)), MatrixRandom.nextDoubleMatrix2d(new Extent2d(0, 1)), true },
            { MatrixRandom.nextDoubleMatrix2d(new Extent2d(1, 0)), MatrixRandom.nextDoubleMatrix2d(new Extent2d(1, 0)), true },
            { MatrixRandom.nextDoubleMatrix2d(new Extent2d(0, 0)), MatrixRandom.nextDoubleMatrix2d(new Extent2d(0, 10)), false },
            { MatrixRandom.nextDoubleMatrix2d(new Extent2d(5, 0)), MatrixRandom.nextDoubleMatrix2d(new Extent2d(0, 0)), false },
            { MatrixRandom.nextDoubleMatrix2d(new Extent2d(5, 50)), MatrixRandom.nextDoubleMatrix2d(new Extent2d(5, 50)), false },
            { MatrixRandom.nextDoubleMatrix2d(new Extent2d(50, 9)), MatrixRandom.nextDoubleMatrix2d(new Extent2d(50, 9)), false },
            { MatrixRandom.nextDoubleMatrix2d(new Extent2d(50, 30)), MatrixRandom.nextDoubleMatrix2d(new Extent2d(50, 9)), false },
            equalGrids(new Extent2d(1, 1)),
            equalGrids(new Extent2d(1, 100)),
            equalGrids(new Extent2d(100, 1)),
            equalGrids(new Extent2d(100, 100))
        };
    }

    private static Object[] equalGrids(final Extent2d extent) {
        final var matrix = MatrixRandom.nextDoubleMatrix2d(extent);
        return new Object[] { matrix, matrix.copy(), true };
    }

    @Test
    public void createFromDoubleArray() {
        // Double array, which is created somewhere else.
        final var data = new double[10*15];
        // Wrap the data into an array. This is just a view, no
        // actual data are copied.
        final var array = new DenseDoubleArray(data);

        // Define the structure (extent) of your 2-d grid.
        final var structure = new Structure2d(new Extent2d(10, 15));
        // Create the grid with your defined structure and data.
        // The grid is a 2-d view onto your one-dimensional double array.
        final var grid = new DoubleGrid2d(structure, array);

        // Assign each grid element the value 42.
        grid.forEach((i, j) -> grid.set(i, j, 42.0));

        // The value is written to the underlying double[] array
        for (var value : data) {
            assertThat(value).isEqualTo(42.0);
        }
    }

}
