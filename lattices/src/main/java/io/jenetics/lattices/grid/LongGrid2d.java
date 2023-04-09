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

import io.jenetics.lattices.array.LongArray;
import io.jenetics.lattices.lattice.LongLattice2d;
import io.jenetics.lattices.structure.Structure2d;
import io.jenetics.lattices.structure.View2d;

/**
 * Generic class for 2-d grids holding {@code long} elements. The
 * {@code LongGrid2d} is <em>just</em> a 2-d view onto a 1-d Java
 * {@code long[]} array. The following example shows how to create such a grid
 * view from a given {@code long[]} array.
 *
 * <pre>{@code
 * final var extent = new Extent2d(50, 100);
 * final var values = new long[extent.size()];
 * final var grid = new LongGrid2d(Structure2d.of(extent), new DenseLongArray(values));
 * }</pre>
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record LongGrid2d(Structure2d structure, LongArray array)
    implements LongLattice2d, Grid2d<LongArray, LongGrid2d>
{

    @Override
    public LongGrid2d create(Structure2d structure, LongArray array) {
        return new LongGrid2d(structure, array);
    }

    @Override
    public void assign(LongGrid2d other) {
        LongLattice2d.super.assign(other);
    }

    /**
     * Create a new grid by applying the given {@code view} transformation.
     *
     * @param view the grid view transformation to apply
     * @return a new grid view
     */
    public LongGrid2d view(View2d view) {
        return new LongGrid2d(view.apply(structure), array);
    }


}
