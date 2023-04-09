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

import io.jenetics.lattices.array.DoubleArray;
import io.jenetics.lattices.lattice.DoubleLattice2d;
import io.jenetics.lattices.structure.Structure2d;

/**
 * Generic class for 2-d grids holding {@code double} elements. The
 * {@code DoubleGrid2d} is <em>just</em> a 2-d view onto a 1-d Java
 * {@code double[]} array. The following example shows how to create such a grid
 * view from a given {@code double[]} array.
 *
 * <pre>{@code
 * final var values = new double[50*100];
 * final var grid = new DoubleGrid2d(
 *     Structure2d.of(new Extent2d(50, 100)),
 *     new DenseDoubleArray(values)
 * );
 * }</pre>
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record DoubleGrid2d(Structure2d structure, DoubleArray array)
    implements DoubleLattice2d
{
}
