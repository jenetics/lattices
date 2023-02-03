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
package io.jenetics.lattices.matrix.blas;

import io.jenetics.lattices.matrix.DoubleMatrix2d;

/**
 * Solver interface for linear systems for a matrix {@code A} which is part of
 * the solver instance.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public interface Solver {

    /**
     * Solves {@code A*X = B} and returns {@code X}.
     *
     * @param B a matrix with as many rows as {@code A} and any number of columns
     * @return {@code X} so that {@code L*L'*X = B}
     * @throws IllegalArgumentException if {@code B.rows() != A.rows()} or
     *         {@code !isSymmetricPositiveDefinite()}
     */
    DoubleMatrix2d solve(final DoubleMatrix2d B);

}
