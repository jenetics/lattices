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
package io.jenetics.lattices.structure;

/**
 * Some helper methods for checking pre-conditions.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public final class Structures {
    private Structures() {
    }

    /**
     * Check whether the structure has fewer elements than the storing array.
     *
     * @param structure the grid structure
     * @param length the array length
     * @throws IllegalArgumentException if the array has fewer elements than
     *         required
     */
    public static void checkArraySize(final Structure1d structure, final int length) {
        if (structure.extent().value() == 0) {
            return;
        }

        final var maxIndex = structure.offset(
            structure.extent().value() - 1
        );

        if (maxIndex >= length) {
            throw new IllegalArgumentException(
                "The array is smaller than the required maximal index: %d <= %d."
                    .formatted(length, maxIndex)
            );
        }
    }

    /**
     * Check whether the structure has fewer elements than the storing array.
     *
     * @param structure the grid structure
     * @param length the array length
     * @throws IllegalArgumentException if the array has fewer elements than
     *         required
     */
    public static void checkArraySize(final Structure2d structure, final int length) {
        if (structure.extent().size() == 0) {
            return;
        }

        final var maxIndex = structure.offset(
            structure.extent().rows() - 1,
            structure.extent().cols() - 1
        );

        if (maxIndex >= length) {
            throw new IllegalArgumentException(
                "The array is smaller than the required maximal index: %d <= %d."
                    .formatted(length, maxIndex)
            );
        }
    }

    /**
     * Check whether the structure has fewer elements than the storing array.
     *
     * @param structure the grid structure
     * @param length the array length
     * @throws IllegalArgumentException if the array has fewer elements than
     *         required
     */
    public static void checkArraySize(final Structure3d structure, final int length) {
        if (structure.extent().size() == 0) {
            return;
        }

        final var maxIndex = structure.offset(
            structure.extent().slices() - 1,
            structure.extent().rows() - 1,
            structure.extent().cols() - 1
        );

        if (maxIndex >= length) {
            throw new IllegalArgumentException(
                "The array is smaller than the required maximal index: %d <= %d."
                    .formatted(length, maxIndex)
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
    public static void checkSameExtent(final Extent1d a, final Extent1d b) {
        if (!a.equals(b)) {
            throw new IllegalArgumentException(
                "Incompatible extent: %s != %s.".formatted(a, b)
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
    public static void checkSameExtent(final Extent2d a, final Extent2d b) {
        if (!a.equals(b)) {
            throw new IllegalArgumentException(
                "Incompatible extent: %s != %s.".formatted(a, b)
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
    public static void checkSameExtent(final Extent3d a, final Extent3d b) {
        if (!a.equals(b)) {
            throw new IllegalArgumentException(
                "Incompatible extent: %s != %s.".formatted(a, b)
            );
        }
    }

    /**
     * Checks if a given extent is <em>square</em>; if it has the same number of
     * rows and columns.
     *
     * @param a the extent to check
     * @return {@code true} if the {@code a} is square, {@code false} otherwise
     */
    public static boolean isSquare(final Extent2d a) {
        return a.rows() == a.cols();
    }

    /**
     * Checks whether the given matrix {@code A} is <em>square</em>.
     *
     * @param a the matrix to check
     * @throws IllegalArgumentException if {@code A.rows() != A.cols()}
     */
    public static void checkSquare(final Extent2d a) {
        if (!isSquare(a)) {
            throw new IllegalArgumentException(
                "Grid extent must be square: " + a
            );
        }
    }

    /**
     * Checks whether the given extent {@code a} is <em>rectangular</em>.
     *
     * @param a the extent to check
     * @throws IllegalArgumentException if {@code a.rows() < a.cols()}
     */
    public static void checkRectangular(final Extent2d a) {
        if (a.rows() < a.cols()) {
            throw new IllegalArgumentException(
                "Grid extent must be rectangular: " + a
            );
        }
    }


    static boolean multNotSave(int a, int b) {
        long r = (long)a*(long)b;
        return (int)r != r;
    }

    static boolean multNotSave(int a, int b, int c) {
        final long r1 = (long)a*(long)b;
        if ((int)r1 == r1) {
            final long r2 = r1*(long)c;
            return (int)r2 != r2;
        } else {
            return true;
        }
    }

    static boolean multNotSave(int a, int b, int c, int d) {
        final long r1 = (long)a*(long)b;
        if ((int)r1 == r1) {
            final long r2 = r1*(long)c;
            if ((int)r2 == r2) {
                final long r3 = r2*(long)d;
                return (int)r3 != r3;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

}
