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

import io.jenetics.lattices.structure.Extent1d;
import io.jenetics.lattices.structure.Extent2d;
import io.jenetics.lattices.structure.Index2d;
import io.jenetics.lattices.structure.Projection2d;
import io.jenetics.lattices.structure.Range2d;
import io.jenetics.lattices.structure.Stride2d;
import io.jenetics.lattices.structure.View2d;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class ObjectGrid2dTest {

    private static ObjectGrid2d<String> grid(final Extent2d extent) {
        final var grid = ObjectGrid2d.<String>dense().create(extent);
        grid.forEach((r, c) -> grid.set(r, c, "%d_%d".formatted(r, c)));
        return grid;
    }

    @Test
    public void viewRange() {
        final var grid = grid(new Extent2d(7, 9));

        final var start = new Index2d( 2, 3);
        final var extent = new Extent2d( 3, 4);
        final var range = new Range2d(start, extent);

        final var view = grid.view(View2d.of(range));
        view.forEach((r, c) -> {
            final var value = view.get(r, c);
            final var expected = "%d_%d".formatted(
                 start.row() + r, start.col() + c
            );
            assertThat(value).isEqualTo(expected);
        });
    }

    @Test
    public void viewViewRange() {
        final var grid = grid(new Extent2d( 22, 23))
            .view(View2d.of(new Index2d( 2, 3)));

        assertThat(grid.extent()).isEqualTo(new Extent2d( 20, 20));

        final var start = new Index2d(2, 3);
        final var extent = new Extent2d( 3, 4);
        final var range = new Range2d(start, extent);

        final var view = grid.view(View2d.of(range));
        view.forEach((r, c) -> {
            final var value = view.get(r, c);
            final var expected = "%d_%d".formatted(
                start.row() + r + 2, start.col() + c + 3
            );
            assertThat(value).isEqualTo(expected);
        });
    }

    @Test
    public void viewViewViewRange() {
        final var grid = grid(new Extent2d( 22, 23))
            .view(View2d.of(new Index2d( 2, 3)))
            .view(View2d.of(new Extent2d( 16, 17)));

        assertThat(grid.extent()).isEqualTo(new Extent2d( 16, 17));

        final var start = new Index2d( 2, 3);
        final var extent = new Extent2d( 3, 4);
        final var range = new Range2d(start, extent);

        final var view = grid.view(View2d.of(range));
        view.forEach((r, c) -> {
            final var value = view.get(r, c);
            final var expected = "%d_%d".formatted(
                start.row() + r + 2, start.col() + c + 3
            );
            assertThat(value).isEqualTo(expected);
        });
    }

    @Test
    public void strideRange() {
        final var grid = grid(new Extent2d( 22, 23));

        final var view = grid.view(View2d.of(new Stride2d( 3, 4)));
        view.forEach((r, c) -> {
            final var value = view.get(r, c);

            final var expected = "%d_%d".formatted(r*3, c*4);
            assertThat(value).isEqualTo(expected);
        });
    }

    @Test
    public void projectionRow() {
        final var grid = grid(new Extent2d( 7, 9));

        final var projection = grid.view(Projection2d.row(3));
        assertThat(projection.extent()).isEqualTo(new Extent1d(9));

        projection.forEach(c -> {
            final var value = projection.get(c);

            final var expected = "%d_%d".formatted(3, c);
            assertThat(value).isEqualTo(expected);
        });
    }

    @Test
    public void viewProjectionRow() {
        final var grid = grid(new Extent2d( 7, 9))
            .view(View2d.of(new Index2d(2, 2)));

        final var projection = grid.view(Projection2d.row(3));
        assertThat(projection.extent()).isEqualTo(new Extent1d(7));

        projection.forEach(c -> {
            final var value = projection.get(c);

            final var expected = "%d_%d".formatted(5, c + 2);
            assertThat(value).isEqualTo(expected);
        });
    }

    @Test
    public void projectionCol() {
        final var grid = grid(new Extent2d( 7, 9));

        final var projection = grid.view(Projection2d.col(3));
        assertThat(projection.extent()).isEqualTo(new Extent1d(7));

        projection.forEach(r -> {
            final var value = projection.get(r);

            final var expected = "%d_%d".formatted(r, 3);
            assertThat(value).isEqualTo(expected);
        });
    }

    @Test
    public void viewProjectionCol() {
        final var grid = grid(new Extent2d( 7, 9))
            .view(View2d.of(new Index2d(2, 2)));

        final var projection = grid.view(Projection2d.col(3));
        assertThat(projection.extent()).isEqualTo(new Extent1d(5));

        projection.forEach(r -> {
            final var value = projection.get(r);

            final var expected = "%d_%d".formatted(r + 2, 5);
            assertThat(value).isEqualTo(expected);
        });
    }

}
