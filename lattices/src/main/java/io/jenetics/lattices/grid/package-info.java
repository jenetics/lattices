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

/**
 * This package contains classes which allows to create rectangular
 * multidimensional data structures, aka grids, which map to one-dimensional
 * data structures, natively supported by Java. Grids, and its derivatives, are
 * views onto the <em>Java</em> arrays, which contains the actual structure. A
 * grid can also be seen as a combination of a grid <em>structure</em> and an
 * <em>array</em>, which contains the data. The <em>structure</em> defines the
 * element access and the extent of the grid. The following code example shows
 * the view-based design of this package/library.
 * <pre>{@code
 * // Double array, which is created somewhere else.
 * final var data = new double[10*15];
 * // Wrap the data into an array. This is just a view, no
 * // actual data are copied.
 * final var array = new DenseDoubleArray(data);
 *
 * // Define the structure (extent) of your 2-d grid.
 * final var structure = new Structure2d(new Extent2d(10, 15));
 * // Create the grid with your defined structure and data.
 * // The grid is a 2-d view onto your one-dimensional double array.
 * final var grid = new DoubleGrid2d(structure, array);
 *
 * // Assign each grid element the value 42.
 * grid.forEach((i, j) -> grid.set(i, j, 42.0));
 *
 * // The value is written to the underlying double[] array
 * for (var value : data) {
 *     assertThat(value).isEqualTo(42.0);
 * }
 * }</pre>
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
package io.jenetics.lattices.grid;
