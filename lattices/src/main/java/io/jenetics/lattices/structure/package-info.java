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
 *
 * <h2>Structures</h2>
 * This section describes the main classes for creating structural mappings from
 * <em>n</em>-d <em>indexes</em> to 1-d <em>offsets</em>.
 *
 * <h3>Extent</h3>
 * The <em>extent</em> defines the extension of the <em>n</em>-dimensional
 * structure. For 2-d structures, the <em>extent</em>
 * ({@link io.jenetics.lattices.structure.Extent2d}) is a tuple (<em>rows</em>,
 * <em>cols</em>, <em>bands</em>), where
 * <br>
 * <ul>
 *     <li><b>rows</b>: the number of rows,</li>
 *     <li><b>cols</b>: the number of columns and</li>
 *     <li><b>bands</b>: the number of bands.</li>
 * </ul>
 * <pre>{@code
 * record Extent2d(int rows, int cols, int bands) {
 * }
 * }</pre>
 * How to interpret these values is shown in the following sketch.
 * <p>
 * <img alt="Extent" src="doc-files/Extent.svg" width="400">
 * </p>
 * Each <em>element</em> can consists of <em>1..n</em> cells, which is defined
 * by the number of <em>bands</em>. The number of elements of a
 * {@link io.jenetics.lattices.structure.Structure2d} is therefor
 * {@code rows*cols} and the number of cells {@code rows*cols*bands}.
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
 * The offset is determined by these three parameters, and the calculation method
 * is shown in the code snippet below.
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
 * so on.
 * <p>
 * <b>The layout has no knowledge of the actual extent of a <em>structure</em>
 * and doesn't perform any pre-condition checks. It is assumed that the layout
 * is created with the correct parameters for the desired offset mapping of a
 * given structure.</b>
 *
 * <h3>Structure</h3>
 * The <em>structure</em> classes combines the extent and a layout.
 * <pre>{@code
 * record Structure2d(Extent2d extent, Layout2d layout) {
 * }
 * }</pre>
 * With the {@link io.jenetics.lattices.structure.Structure2d#Structure2d(Extent2d)}
 * constructor, you are able to create a properly initialized <em>structure</em>
 * object, as shown in the following code snippet.
 * <pre>{@code
 * // Define the structure + extent of your lattices.
 * final var structure = new Structure2d(10, 34);
 *
 * // Create the `double[]` array, which stores the data.
 * final double[] values = new double[structure.extent().cells()];
 *
 * // The 2-d coordinates you want to access.
 * final int row = 3;
 * final int col = 5;
 *
 * // The array offset which stores the value at (5, 3).
 * final int offset = structure.layout().offset(row, col);
 *
 * // Write the value to the given coordinate.
 * values[offset] = Math.PI;
 *
 * // Get the index back from a given array offset.
 * final Index2d index = structure.layout().index(offset);
 * assert index.row == row;
 * assert index.col == col;
 * }</pre>
 *
 * <h2>Transformations</h2>
 * The <em>transformation</em> classes allows to transform <em>structure</em>
 * objects, without the need for copying any data in the underlying data array.
 *
 * <h3>View</h3>
 * View transformations let you create a <em>structure</em> view of a given
 * <em>parent</em> structure. The dimensionality of the new structure view
 * will be the same.
 * <pre>{@code
 * // Create a new 20*20 2-d structure object.
 * final var structure = new Structure2d(20, 20);
 *
 * // Create a view of the given structure, which starts at (5, 5) and reaches
 * // to (15, 15) of the original structure.
 * final var range = new Range2d(
 *     new Index2d(5, 5),
 *     new Extent2d(10, 10)
 * );
 * final var view = View.of(range).apply(structure);
 *
 * // Coordinate (8, 8) of the original structure and
 * // coordinate (3, 3) point to the same element.
 * assert structure.layout().offset(8, 8) == view.layout().offset(3, 3);
 * }</pre>
 *
 * <h3>Projection</h3>
 * Projection transformations will reduce the dimensionality of the structure by
 * one. E.g., a 3-d structure will give you a 2-d structure view onto the
 * underlying one-dimensional data.
 *
 * <pre>{@code
 * // Create a new 3-d structure with 10 elements in each directions.
 * final var structure = new Structure3d(10, 10, 10);
 *
 * // Create a 2-d projection of the 5th slice.
 * final var projection = Projection3d.slice(4).apply(structure);
 *
 * // Coordinate (4, 7, 7) of the original structure and coordinate (7, 7)
 * // of the projected structure point to the same element.
 * assert structure.layout().offset(4, 7, 7) == projection.layout().offset(7, 7);
 * }</pre>
 * <p>
 * <b>View- and projection transformation can be combined in any desired order.</b>
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
package io.jenetics.lattices.structure;
