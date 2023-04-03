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

/**
 * This package contains classes, which allows to use one dimensional arrays
 * (or array like structures) as storage for multidimensional lattices/grids.
 * The following code snippet shows how to do this for a 2-d double array.
 * <pre>{@code
 * // Define the structure + extent of your lattices.
 * final Structure2d structure = Structure2d.of(new Extent2d(10, 34));
 *
 * // The layout of your structure lets you calculate the array offset.
 * final Layout2d layout = structure.layout();
 *
 * // Create the `double[]` array, which stores the data.
 * final double[] values = new double[structure.extent().size()];
 *
 * // The 2-d coordinates you want to access.
 * final int row = 3;
 * final int col = 5;
 *
 * // The array offset which stores the value at (5, 3).
 * final int offset = layout.offset(row, col);
 *
 * // Write the value to the given coordinate.
 * values[offset] = Math.PI;
 *
 * // Get the index back from a given array offset.
 * final Index2d index = layout.index(offset);
 * assert index.row == row;
 * assert index.col == col;
 * }</pre>
 *
 * The {@code View} and {@code Projection} functions are used for manipulating
 * the <em>structure</em> objects.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
package io.jenetics.lattices.structure;
