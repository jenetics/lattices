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

import io.jenetics.lattices.function.Int2Consumer;
import io.jenetics.lattices.matrix.DoubleMatrix2d;

/**
 * Generic permutation methods.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
final class Permutations {
    private Permutations() {
    }

    /**
     * Permutes the rows of the given {@code matrix} according to the given
     * permutation {@code indexes}.
     *
     * @param matrix the matrix to permute
     * @param indexes the permutation {@code indexes}
     * @throws IllegalArgumentException if {@code matrix.rows() != indexes.length}
     */
    static void permuteRows(final DoubleMatrix2d matrix, final int[] indexes) {
        if (matrix.rows() != indexes.length) {
            throw new IllegalArgumentException(
                "Invalid permutation: %d != %d."
                    .formatted(matrix.rows(), indexes.length)
            );
        }

        permute((a, b) -> matrix.rowAt(a).swap(matrix.rowAt(b)), indexes);
    }

    /**
     * General permutation method.
     *
     * @param swapper the swapper function
     *  @param indexes the permutation {@code indexes}
     */
    static void permute(final Int2Consumer swapper, final int[] indexes) {
        final int[] tracks = new int[indexes.length];
        for (int i = 0; i < indexes.length; ++i) {
            tracks[i] = i;
        }
        final int[] pos = tracks.clone();

        for (int i = 0; i < indexes.length; ++i) {
            final int index = indexes[i];
            final int track = tracks[index];

            if (i != track) {
                swapper.accept(i, track);
                tracks[index] = i;
                tracks[pos[i]] = track;

                final int tmp = pos[i];
                pos[i] = pos[track];
                pos[track] = tmp;
            }
        }
    }

}
