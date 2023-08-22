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
 * This package contains classes, which allows to use one-dimensional arrays
 * (or array like structures) as storage for multidimensional lattices/grids.
 * The following code snippet shows how to do this for a 2-d double array.
 * <pre>{@code
 * // Define the structure + extent of your lattices.
 * final Structure2d structure = Structure2d.of(new Extent2d(10, 34));
 *
 * // Create the `double[]` array, which stores the data.
 * final double[] values = new double[structure.extent().length()];
 *
 * // The 2-d coordinates you want to access.
 * final int row = 3;
 * final int col = 5;
 *
 * // The array offset which stores the value at (5, 3).
 * final int offset = structure.offset(row, col);
 *
 * // Write the value to the given coordinate.
 * values[offset] = Math.PI;
 *
 * // Get the index back from a given array offset.
 * final Index2d index = structure.index(offset);
 * assert index.row == row;
 * assert index.col == col;
 * }</pre>
 *
 * The {@code View} and {@code Projection} functions are used for manipulating
 * the <em>structure</em> objects.
 *
 * <h2>Structures</h2>
 * This section describes the main classes for creating structural mappings from
 * <em>n</em>-d <em>indexes</em> to 1-d <em>offsets</em>.
 *
 * <h3>Extent</h3>
 * The <em>extent</em> defines the extension of the <em>n</em>-dimensional
 * structure. For 2-d structures, the <em>extent</em>
 * ({@link io.jenetics.lattices.structure.Extent2d}) is a tuple (<em>nrows</em>,
 * <em>ncols</em>, <em>nbands</em>), where
 * <br>
 * <ul>
 *     <li><b>nrows</b>: the number of rows,</li>
 *     <li><b>ncols</b>: the number of columns and</li>
 *     <li><b>nbands</b>: the number of bands.</li>
 * </ul>
 * <pre>{@code
 * record Extent2d(int nrows, int ncols, int nbands) {
 * }
 * }</pre>
 * How to interpret these values is shown in the following sketch.
 * <p>
 * <img alt="Extent" src="doc-files/Extent.svg" width="400">
 * </p>
 * Each <em>element</em> can consists of <em>1..n</em> cells, which is defined
 * by the number of <em>bands</em>. The number of elements of a
 * {@link io.jenetics.lattices.structure.Structure2d} is therefor
 * {@code nrows*ncols} and the number of cells {@code nrows*ncols*nbands}.
 *
 * <h3>Layout</h3>
 * The <em>layout</em> defines the mapping of a <em>n</em>-dimensional index
 * to the <em>offset</em> of a one-dimensional array. It is essentially a tuple
 * (<em>start</em>, <em>strides</em>, <em>band</em>), where
 * <br>
 * <ul>
 *     <li><b>start</b>: the <em>n</em>-dimensional start index, usually
 *     the <em>zero</em>-vector,</li>
 *     <li><b>stride</b>: the stride vector, which defines stride for every
 *     dimension in the array and</li>
 *     <li><b>band</b>: the band index for which the offset should be calculated.</li>
 * </ul>
 * <pre>{@code
 * record Layout2d(Index2d start, Stride2d stride, Band band) {
 * }
 * }</pre>
 * Since the offset algorithm is fixed, these three parameters influence the
 * offset calculation. The following code snippet shows how the offset is
 * calculated for a 2-d layout.
 * <pre>{@code
 * int offset(int row, int col) {
 *     return
 *         start.row() + row*stride.row() +
 *         start.col() + col*stride.col() +
 *         band.value();
 * }
 * }</pre>
 * The <em>band</em> index is usually set to <em>zero</em>, since it is quite
 * easy to calculate the offset for a given band.
 * <pre>{@code
 * int offsetBand0 = layout.offset(10, 5);
 * int offsetBand1 = offsetBand0 + 1;
 * int offsetBand2 = offsetBand0 + 2;
 * }</pre>
 * It is sufficient to add one for getting the offset for band <em>one</em>, and
 * so on. The layout has no knowledge of the actual extent of a <em>structure</em>
 * and doesn't perform any pre-condition checks. It is assumed that the layout
 * is created with the correct parameters for the desired offset mapping of a
 * given structure.
 * <p>
 * <em><b>A layout is a kind of low-level helper type, used for offset
 * calculation. It is not meat to be used directly. The <em>structure</em> classes
 * are usually the entry point for creating proper offset mapping objects.</b></em>
 *
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
package io.jenetics.lattices.structure;
