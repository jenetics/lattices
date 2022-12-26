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

import io.jenetics.lattices.structure.Structure2d;

/**
 * Some helper methods for checking pre-conditions.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public final class Grids {
    private Grids() {
    }

    /**
     * Checks whether the two given grids have the same extent.
     *
     * @param a the first grid to check
     * @param b the second grid to check
     * @throws IllegalArgumentException if the given {@code other} extent doesn't
     *         match
     */
    public static void checkSameExtent(final Grid1d a, final Grid1d b) {
        if (!a.extent().equals(b.extent())) {
            throw new IllegalArgumentException(
                "Incompatible extent: %s != %s.".formatted(a.extent(), b.extent())
            );
        }
    }

    /**
     * Checks whether the two given grids have the same extent.
     *
     * @param a the first grid to check
     * @param b the second grid to check
     * @throws IllegalArgumentException if the given {@code other} extent doesn't
     *         match
     */
    public static void checkSameExtent(final Structural2d a, final Structural2d b) {
        if (!a.extent().equals(b.extent())) {
            throw new IllegalArgumentException(
                "Incompatible extent: %s != %s.".formatted(a.extent(), b.extent())
            );
        }
    }

    /**
     * Checks whether the two given grids have the same extent.
     *
     * @param a the first grid to check
     * @param b the second grid to check
     * @throws IllegalArgumentException if the given {@code other} extent doesn't
     *         match
     */
    public static void checkSameExtent(final Structure2d a, final Structure2d b) {
        if (!a.extent().equals(b.extent())) {
            throw new IllegalArgumentException(
                "Incompatible extent: %s != %s.".formatted(a.extent(), b.extent())
            );
        }
    }

    /**
     * A matrix {@code A} is <em>square</em> if it has the same number of rows
     * and columns.
     *
     * @param A the matrix to check
     * @return {@code true} if the {@code A} is square, {@code false} otherwise
     */
    public static boolean isSquare(final Structural2d A) {
        return A.rows() == A.cols();
    }

    /**
     * Checks whether the given matrix {@code A} is <em>square</em>.
     *
     * @param A the matrix to check
     * @throws IllegalArgumentException if {@code A.rows() != A.cols()}
     */
    public static void checkSquare(final Structural2d A) {
        if (!isSquare(A)) {
            throw new IllegalArgumentException(
                "Matrix must be square: " + A.extent()
            );
        }
    }

    /**
     * Checks whether the given matrix {@code A} is <em>rectangular</em>.
     *
     * @param A the matrix to check
     * @throws IllegalArgumentException if {@code A.rows() < A.cols()}
     */
    public static void checkRectangular(final Structural2d A) {
        if (A.rows() < A.cols()) {
            throw new IllegalArgumentException(
                "Matrix must be rectangular: " + A.extent()
            );
        }
    }
}
