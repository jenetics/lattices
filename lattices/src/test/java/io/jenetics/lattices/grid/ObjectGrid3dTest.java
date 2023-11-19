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
import io.jenetics.lattices.structure.Extent3d;
import io.jenetics.lattices.structure.Index3d;
import io.jenetics.lattices.structure.Projection2d;
import io.jenetics.lattices.structure.Projection3d;
import io.jenetics.lattices.structure.Range3d;
import io.jenetics.lattices.structure.Stride3d;
import io.jenetics.lattices.structure.View3d;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class ObjectGrid3dTest {

    private static ObjectGrid3d<String> grid(final Extent3d extent) {
        final var grid = ObjectGrid3d.<String>dense().create(extent);
        grid.forEach((s, r, c) -> grid.set(s, r, c, "%d_%d_%d".formatted(s, r, c)));
        return grid;
    }

    @Test
    public void viewRange() {
        final var grid = grid(new Extent3d(5, 7, 9));

        final var start = new Index3d(1, 2, 3);
        final var extent = new Extent3d(2, 3, 4);
        final var range = new Range3d(start, extent);

        final var view = grid.view(View3d.of(range));
        view.forEach((s, r, c) -> {
            final var value = view.get(s, r, c);
            final var expected = "%d_%d_%d".formatted(
                start.slice() + s, start.row() + r, start.col() + c
            );
            assertThat(value).isEqualTo(expected);
        });
    }

    @Test
    public void viewViewRange() {
        final var grid = grid(new Extent3d(21, 22, 23))
            .view(View3d.of(new Index3d(1, 2, 3)));

        assertThat(grid.extent()).isEqualTo(new Extent3d(20, 20, 20));

        final var start = new Index3d(1, 2, 3);
        final var extent = new Extent3d(2, 3, 4);
        final var range = new Range3d(start, extent);

        final var view = grid.view(View3d.of(range));
        view.forEach((s, r, c) -> {
            final var value = view.get(s, r, c);
            final var expected = "%d_%d_%d".formatted(
                start.slice() + s + 1, start.row() + r + 2, start.col() + c + 3
            );
            assertThat(value).isEqualTo(expected);
        });
    }

    @Test
    public void viewViewViewRange() {
        final var grid = grid(new Extent3d(21, 22, 23))
            .view(View3d.of(new Index3d(1, 2, 3)))
            .view(View3d.of(new Extent3d(15, 16, 17)));

        assertThat(grid.extent()).isEqualTo(new Extent3d(15, 16, 17));

        final var start = new Index3d(1, 2, 3);
        final var extent = new Extent3d(2, 3, 4);
        final var range = new Range3d(start, extent);

        final var view = grid.view(View3d.of(range));
        view.forEach((s, r, c) -> {
            final var value = view.get(s, r, c);
            final var expected = "%d_%d_%d".formatted(
                start.slice() + s + 1, start.row() + r + 2, start.col() + c + 3
            );
            assertThat(value).isEqualTo(expected);
        });
    }

    @Test
    public void strideRange() {
        final var grid = grid(new Extent3d(21, 22, 23));

        final var view = grid.view(View3d.of(new Stride3d(2, 3, 4)));
        view.forEach((s, r, c) -> {
            final var value = view.get(s, r, c);

            final var expected = "%d_%d_%d".formatted(s*2, r*3, c*4);
            assertThat(value).isEqualTo(expected);
        });
    }

    @Test
    public void projectionSlice() {
        final var grid = grid(new Extent3d(5, 7, 9));

        final var projection = grid.project(Projection3d.slice(3));
        assertThat(projection.extent()).isEqualTo(new Extent2d(7, 9));

        projection.forEach((r, c) -> {
            final var value = projection.get(r, c);

            final var expected = "%d_%d_%d".formatted(3, r, c);
            assertThat(value).isEqualTo(expected);
        });
    }

    @Test
    public void projectionSliceRow() {
        final var grid = grid(new Extent3d(5, 7, 9));

        final var projection = grid
            .project(Projection3d.slice(3))
            .project(Projection2d.row(3));
        assertThat((Object)projection.extent()).isEqualTo(new Extent1d(9));

        projection.forEach(i -> {
            final var value = projection.get(i);

            final var expected = "%d_%d_%d".formatted(3, 3, i);
            assertThat(value).isEqualTo(expected);
        });
    }

    @Test
    public void projectionSliceCol() {
        final var grid = grid(new Extent3d(5, 7, 9));

        final var projection = grid
            .project(Projection3d.slice(3))
            .project(Projection2d.col(3));
        assertThat((Object)projection.extent()).isEqualTo(new Extent1d(7));

        projection.forEach(i -> {
            final var value = projection.get(i);

            final var expected = "%d_%d_%d".formatted(3, i, 3);
            assertThat(value).isEqualTo(expected);
        });
    }

    @Test
    public void projectionRow() {
        final var grid = grid(new Extent3d(5, 7, 9));

        final var projection = grid.project(Projection3d.row(3));
        assertThat(projection.extent()).isEqualTo(new Extent2d(5, 9));

        projection.forEach((r, c) -> {
            final var value = projection.get(r, c);

            final var expected = "%d_%d_%d".formatted(r, 3, c);
            assertThat(value).isEqualTo(expected);
        });
    }

    @Test
    public void projectionRowCol() {
        final var grid = grid(new Extent3d(5, 7, 9));

        final var projection = grid
            .project(Projection3d.row(3))
            .project(Projection2d.col(3));
        assertThat((Object)projection.extent()).isEqualTo(new Extent1d(5));

        projection.forEach(i -> {
            final var value = projection.get(i);

            final var expected = "%d_%d_%d".formatted(i, 3, 3);
            assertThat(value).isEqualTo(expected);
        });
    }

    @Test
    public void projectionCol() {
        final var grid = grid(new Extent3d(5, 7, 9));

        final var projection = grid.project(Projection3d.col(3));
        assertThat(projection.extent()).isEqualTo(new Extent2d(5, 7));

        projection.forEach((r, c) -> {
            final var value = projection.get(r, c);

            final var expected = "%d_%d_%d".formatted(r, c, 3);
            assertThat(value).isEqualTo(expected);
        });
    }

}
