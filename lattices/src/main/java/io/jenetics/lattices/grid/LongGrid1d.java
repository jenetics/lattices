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
import io.jenetics.lattices.lattice.LongLattice1d;
import io.jenetics.lattices.structure.Structure1d;

/**
 * Generic class for 1-d grids holding {@code long} elements. The
 * {@code LongGrid1d} is <em>just</em> a 1-d view onto a 1-d Java
 * {@code long[]} array. The following example shows how to create such a grid
 * view from a given {@code long[]} array.
 *
 * <pre>{@code
 * final var extent = new Extent1d(100);
 * final var values = new long[extent.size()];
 * final var grid = new LongGrid1d(Structure1d.of(extent), new DenseLongArray(values));
 * }</pre>
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record LongGrid1d(Structure1d structure, LongArray array)
    implements LongLattice1d
{
}
