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
 * This package contains the commonly known <em>matrix</em> implementation
 * classes. The matrix classes share the same design principles as the grid
 * classes; they are mainly multidimensional view onto one-dimensional arrays.
 * The numerical precission of the matrix implementations is controlled via the
 * {@link io.jenetics.lattices.matrix.NumericalContext} class.
 * <p>
 * The default context is created with an epsilon of 10<sup>-9</sup>.
 * The default value can be changed with defining a Java <em>property</em> on
 * the command line.
 * <pre>
 *     $ java -Dio.jenetics.lattices.matrix.precision=12 ...
 * </pre>
 * The example above will change the epsilon of the default context object to
 * 10<sup>-12</sup>. It is also possible to change the numerical context only
 * for specific parts of your code. This might be useful in a testing
 * environment. The following example shows how to do this.
 * <pre>{@code
 * final DoubleMatrix2d A = ...;
 * final DoubleMatrix2d B = ...;
 *
 * // Executes the 'solve' operation with a different epsilon.
 * // Other parts of the program are not effected.
 * NumericalContext.using(new NumericalContext(0.001), () -> {
 *     final DoubleMatrix2d X = Algebra.solve(A, B);
 * });
 * }</pre>
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
package io.jenetics.lattices.matrix;
